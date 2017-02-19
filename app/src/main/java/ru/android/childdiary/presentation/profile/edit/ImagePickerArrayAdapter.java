package ru.android.childdiary.presentation.profile.edit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import ru.android.childdiary.R;

class ImagePickerArrayAdapter extends ArrayAdapter<String> {
    private final String[] items;

    public ImagePickerArrayAdapter(Context context) {
        super(context, getLayoutResourceId());
        items = context.getResources().getStringArray(R.array.image_picker_actions);
    }

    private static int getLayoutResourceId() {
        return R.layout.image_picker_item;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return items[position];
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
