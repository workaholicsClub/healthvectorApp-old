package ru.android.childdiary.presentation.medical.adapters.core;

import android.content.Context;

import com.tokenautocomplete.FilteredArrayAdapter;

import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.utils.strings.StringUtils;

public class StringFilteredAdapter extends FilteredArrayAdapter<String> {
    private final FilterType filterType;

    public StringFilteredAdapter(Context context, List<String> strings, FilterType filterType) {
        super(context, R.layout.auto_complete_item, strings);
        this.filterType = filterType;
    }

    @Override
    protected boolean keepObject(String obj, String mask) {
        switch (filterType) {
            case CONTAINS:
                return StringUtils.contains(obj, mask, false);
            case STARTS:
                return StringUtils.starts(obj, mask, false);
        }
        return false;
    }

    public enum FilterType {
        CONTAINS, STARTS
    }
}
