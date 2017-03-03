package ru.android.childdiary.presentation.profile.edit.image;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;

class ImagePickerAdapter extends ArrayAdapter<ImagePickerAction> {
    private final List<ImagePickerAction> actions;

    public ImagePickerAdapter(Context context, List<ImagePickerAction> actions) {
        super(context, getLayoutResourceId());
        this.actions = Collections.unmodifiableList(actions);
    }

    @LayoutRes
    private static int getLayoutResourceId() {
        return R.layout.image_picker_item;
    }

    @Override
    public int getCount() {
        return actions.size();
    }

    @Nullable
    @Override
    public ImagePickerAction getItem(int position) {
        return actions.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(getLayoutResourceId(), null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ImagePickerAction action = getItem(position);
        viewHolder.bind(position, action);

        return view;
    }

    static class ViewHolder {
        @BindView(android.R.id.text1)
        TextView textView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        void bind(int position, ImagePickerAction action) {
            textView.setText(action.getTitleResourceId());
            textView.setCompoundDrawablesWithIntrinsicBounds(action.getIconResourceId(), 0, 0, 0);
        }
    }
}
