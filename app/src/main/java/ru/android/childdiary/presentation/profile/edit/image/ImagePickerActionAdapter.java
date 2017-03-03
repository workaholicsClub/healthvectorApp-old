package ru.android.childdiary.presentation.profile.edit.image;

import android.content.Context;
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
    protected ViewHolder createViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder extends BaseViewHolder<ImagePickerAction> {
        @BindView(android.R.id.text1)
        TextView textView;

        @Override
        protected int getLayoutResourceId() {
            return R.layout.image_picker_item;
        }

        @Override
        public void bind(Context context, int position, ImagePickerAction item) {
            textView.setText(item.getTitleResourceId());
            textView.setCompoundDrawablesWithIntrinsicBounds(item.getIconResourceId(), 0, 0, 0);
        }
    }
}
