package ru.android.childdiary.presentation.core.bindings;

import android.support.v7.widget.SearchView;

import com.jakewharton.rxbinding2.InitialValueObservable;

import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

public final class SearchViewQueryTextChangeEventsObservable
        extends InitialValueObservable<SearchViewQueryTextEvent> {
    private final SearchView view;

    public SearchViewQueryTextChangeEventsObservable(SearchView view) {
        this.view = view;
    }

    @Override
    protected void subscribeListener(Observer<? super SearchViewQueryTextEvent> observer) {
        if (!Preconditions.checkMainThread(observer)) {
            return;
        }
        Listener listener = new Listener(view, observer);
        view.setOnQueryTextListener(listener);
        observer.onSubscribe(listener);
    }

    @Override
    protected SearchViewQueryTextEvent getInitialValue() {
        return new SearchViewQueryTextEvent(view, view.getQuery(), false);
    }

    private static final class Listener extends MainThreadDisposable implements SearchView.OnQueryTextListener {
        private final SearchView view;
        private final Observer<? super SearchViewQueryTextEvent> observer;

        public Listener(SearchView view, Observer<? super SearchViewQueryTextEvent> observer) {
            this.observer = observer;
            this.view = view;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (!isDisposed()) {
                observer.onNext(new SearchViewQueryTextEvent(view, s, false));
                return true;
            }
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            if (!isDisposed()) {
                observer.onNext(new SearchViewQueryTextEvent(view, query, true));
                return true;
            }
            return false;
        }

        @Override
        protected void onDispose() {
            view.setOnQueryTextListener(null);
        }
    }
}
