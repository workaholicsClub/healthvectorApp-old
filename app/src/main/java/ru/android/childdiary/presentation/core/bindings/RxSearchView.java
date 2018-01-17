package ru.android.childdiary.presentation.core.bindings;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;

public final class RxSearchView {
    private RxSearchView() {
        throw new AssertionError("No instances.");
    }

    @CheckResult
    @NonNull
    public static SearchViewQueryTextChangeEventsObservable queryTextChangeEvents(
            @NonNull SearchView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new SearchViewQueryTextChangeEventsObservable(view);
    }
}
