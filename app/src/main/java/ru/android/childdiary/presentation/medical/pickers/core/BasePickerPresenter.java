package ru.android.childdiary.presentation.medical.pickers.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.presentation.core.bindings.SearchViewQueryTextChangeEventsObservable;
import ru.android.childdiary.presentation.core.bindings.SearchViewQueryTextEvent;

public abstract class BasePickerPresenter<T, V extends BasePickerView<T>> extends BasePresenter<V> {
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        requestList();
    }

    private void requestList() {
        unsubscribeOnDestroy(getAllItemsLoader(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showList, this::onUnexpectedError));
    }

    public Disposable listenForFieldsUpdate(@NonNull SearchViewQueryTextChangeEventsObservable searchObservable) {
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
                .subscribe(getViewState()::showList, this::onUnexpectedError);
    }

    private Observable<List<T>> getAllItemsLoader(@Nullable String filter) {
        return getAllItemsLoader()
                .map(list -> filter(list, filter));
    }

    private List<T> filter(@NonNull List<T> list, @Nullable String filter) {
        return Observable.fromIterable(list).filter(t -> filter(t, filter)).toList().blockingGet();
    }

    protected abstract Observable<List<T>> getAllItemsLoader();

    protected abstract boolean filter(@NonNull T item, @Nullable String filter);

    public void deleteItem(@NonNull T item) {
        unsubscribeOnDestroy(deleteItemLoader(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::itemDeleted, this::onUnexpectedError));
    }

    protected abstract Observable<T> deleteItemLoader(@NonNull T item);
}