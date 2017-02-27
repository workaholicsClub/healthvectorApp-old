package ru.android.childdiary.presentation.profile.edit;

import android.content.Context;
import android.content.res.TypedArray;
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
    private final int[] drawables;

    public ImagePickerAdapter(Context context) {
        super(context, getLayoutResourceId());
        items = context.getResources().getStringArray(R.array.image_picker_actions);

        TypedArray array = context.getResources().obtainTypedArray(R.array.image_picker_drawables);
        int count = array.length();
        drawables = new int[count];
        for (int i = 0; i < count; ++i) {
            drawables[i] = array.getResourceId(i, 0);
        }
        array.recycle();

        if (items.length != count) {
            throw new RuntimeException("invalid resources: image picker drawables");
        }
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
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(getLayoutResourceId(), null);
            viewHolder = new ViewHolder();
            viewHolder.textView = ButterKnife.findById(view, android.R.id.text1);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String text = getItem(position);
        int drawableRes = drawables[position];

        viewHolder.textView.setText(text);
        viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(drawableRes, 0, 0, 0);

        return view;
    }

    private static class ViewHolder {
        TextView textView;
    }
}
