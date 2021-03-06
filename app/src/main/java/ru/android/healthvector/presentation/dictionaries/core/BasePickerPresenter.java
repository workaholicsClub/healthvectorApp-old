package ru.android.healthvector.presentation.dictionaries.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import lombok.Value;
import ru.android.healthvector.domain.dictionaries.core.DictionaryInteractor;
import ru.android.healthvector.presentation.core.BasePresenter;
import ru.android.healthvector.presentation.core.adapters.DeletedItemsManager;
import ru.android.healthvector.presentation.core.bindings.SearchViewQueryTextChangeEventsObservable;
import ru.android.healthvector.presentation.core.bindings.SearchViewQueryTextEvent;

public abstract class BasePickerPresenter<T, V extends BasePickerView<T>> extends BasePresenter<V> {
    private final DeletedItemsManager<T> deletedItemsManager = new DeletedItemsManager<>();

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        requestList();
    }

    private void requestList() {
        unsubscribeOnDestroy(getAllItemsLoader(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> getViewState().showList(response.getResult(), response.isFiltering()),
                        this::onUnexpectedError));
    }

    public final Disposable listenForFieldsUpdate(@NonNull SearchViewQueryTextChangeEventsObservable searchObservable) {
        return searchObservable
                .map(searchEvent -> {
                    getViewState().processSearchEvent(searchEvent);
                    return searchEvent;
                })
                .map(SearchViewQueryTextEvent::getQueryText)
                .debounce(200, TimeUnit.MILLISECONDS)
                .map(CharSequence::toString)
                .map(String::trim)
                .flatMap(this::getAllItemsLoader)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> getViewState().showList(response.getResult(), response.isFiltering()),
                        this::onUnexpectedError);
    }

    private Observable<Response<T>> getAllItemsLoader(@Nullable String filter) {
        return getInteractor().getAll()
                .map(list -> filter(list, filter))
                .map(list -> new Response<>(filter, list));
    }

    private List<T> filter(@NonNull List<T> list, @Nullable String filter) {
        return Observable.fromIterable(list).filter(t -> filter(t, filter)).toList().blockingGet();
    }

    public final void edit(@NonNull T item) {
        if (deletedItemsManager.check(item)) {
            return;
        }
        getViewState().navigateTo(item);
    }

    public final void delete(@NonNull T item) {
        if (deletedItemsManager.check(item)) {
            return;
        }
        getViewState().confirmDelete(item);
    }

    public final void forceDelete(@NonNull T item) {
        if (deletedItemsManager.checkAndAdd(item)) {
            return;
        }
        unsubscribeOnDestroy(getInteractor().delete(item)
                .doOnError(throwable -> logger.error("failed to delete", throwable))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::deleted, throwable -> getViewState().deletionRestricted()));
    }

    protected abstract DictionaryInteractor<T> getInteractor();

    protected abstract boolean filter(@NonNull T item, @Nullable String filter);

    @Value
    @AllArgsConstructor(suppressConstructorProperties = true)
    private static class Response<T> {
        @Nullable
        String filter;
        @NonNull
        List<T> result;

        public boolean isFiltering() {
            return !TextUtils.isEmpty(filter);
        }
    }
}
