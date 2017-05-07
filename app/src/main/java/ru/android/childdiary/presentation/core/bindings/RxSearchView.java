package ru.android.childdiary.presentation.core.bindings;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

public final class RxSearchView {
    @CheckResult
    @NonNull
    public static SearchViewQueryTextChangeEventsObservable queryTextChangeEvents(
            @NonNull SearchView view) {
        checkNotNull(view, "view == null");
        return new SearchViewQueryTextChangeEventsObservable(view);
    }

    private RxSearchView() {
        throw new AssertionError("No instances.");
    }
}
