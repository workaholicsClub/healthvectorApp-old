package ru.android.childdiary.presentation.profile.edit.adapters;

import android.content.Context;
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
    protected ViewHolder createViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder extends BaseViewHolder<Sex> {
        @BindView(android.R.id.text1)
        TextView textView;

        @Override
        protected int getLayoutResourceId() {
            return R.layout.sex_item;
        }

        @Override
        public void bind(Context context, int position, Sex item) {
            String text = StringUtils.sex(context, item);
            textView.setText(text);
        }
    }
}
