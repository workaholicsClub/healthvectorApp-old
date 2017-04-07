package ru.android.childdiary.presentation.profile.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.adapters.BaseArrayAdapter;
import ru.android.childdiary.presentation.core.adapters.BaseViewHolder;
import ru.android.childdiary.utils.StringUtils;

public class SexAdapter extends BaseArrayAdapter<Sex, SexAdapter.ViewHolder> {
    public SexAdapter(Context context) {
        super(context, Arrays.asList(Sex.MALE, Sex.FEMALE));
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.sex_item;
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder extends BaseViewHolder<Sex> {
        @BindView(android.R.id.text1)
        TextView textView;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(Context context, int position, Sex item) {
            String text = StringUtils.sex(context, item);
            textView.setText(text);
        }
    }
}
