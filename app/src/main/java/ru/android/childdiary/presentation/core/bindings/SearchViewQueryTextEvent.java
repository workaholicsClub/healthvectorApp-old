package ru.android.childdiary.presentation.core.bindings;

import android.support.v7.widget.SearchView;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor(suppressConstructorProperties = true)
public class SearchViewQueryTextEvent {
    @NonNull
    SearchView view;
    @NonNull
    CharSequence queryText;
    boolean isSubmitted;
}
