package ru.android.childdiary.presentation.core.image;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.adapters.BaseArrayAdapter;
import ru.android.childdiary.presentation.core.adapters.BaseViewHolder;

class ImagePickerActionAdapter extends BaseArrayAdapter<ImagePickerAction, ImagePickerActionAdapter.ViewHolder> {
    public ImagePickerActionAdapter(Context context, List<ImagePickerAction> actions) {
        super(context, actions);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.image_picker_action_item;
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder extends BaseViewHolder<ImagePickerAction> {
        @BindView(android.R.id.text1)
        TextView textView;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(Context context, int position, ImagePickerAction item) {
            textView.setText(item.getTitleResourceId());
            textView.setCompoundDrawablesWithIntrinsicBounds(item.getIconResourceId(), 0, 0, 0);
        }
    }
}
