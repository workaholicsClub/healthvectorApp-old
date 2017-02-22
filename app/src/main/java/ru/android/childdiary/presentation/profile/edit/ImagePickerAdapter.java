package ru.android.childdiary.presentation.profile.edit;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import butterknife.ButterKnife;
import ru.android.childdiary.R;

class ImagePickerAdapter extends ArrayAdapter<String> {
    private final String[] items;

    public ImagePickerAdapter(Context context) {
        super(context, getLayoutResourceId());
        items = context.getResources().getStringArray(R.array.image_picker_actions);
    }

    @LayoutRes
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
        View v = convertView;
        if (v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(getLayoutResourceId(), null);
        }

        TextView textView = ButterKnife.findById(v, android.R.id.text1);
        textView.setText(getItem(position));

        return v;
    }
}
