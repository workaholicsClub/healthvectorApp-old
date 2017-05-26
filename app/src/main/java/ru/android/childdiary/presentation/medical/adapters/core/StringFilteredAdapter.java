package ru.android.childdiary.presentation.medical.adapters.core;

import android.content.Context;

import com.tokenautocomplete.FilteredArrayAdapter;

import java.util.List;

public class StringFilteredAdapter extends FilteredArrayAdapter<String> {
    public StringFilteredAdapter(Context context, List<String> strings) {
        super(context, android.R.layout.simple_list_item_1, strings);
    }

    @Override
    protected boolean keepObject(String obj, String mask) {
        mask = mask.toLowerCase();
        return obj != null && obj.toLowerCase().startsWith(mask);
    }
}
