package ru.android.childdiary.presentation.profile.edit.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.utils.StringUtils;

public class SexAdapter extends ArrayAdapter<Sex> {
    private final List<Sex> items;

    public SexAdapter(Context context) {
        super(context, getLayoutResourceId());
        items = Arrays.asList(Sex.MALE, Sex.FEMALE);
    }

    @LayoutRes
    private static int getLayoutResourceId() {
        return R.layout.sex_item;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public Sex getItem(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup viewGroup) {
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

        Sex sex = getItem(position);
        viewHolder.bind(position, sex);

        return view;
    }

    static class ViewHolder {
        @BindView(android.R.id.text1)
        TextView textView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        void bind(int position, Sex sex) {
            Context context = textView.getContext();
            String text = StringUtils.sex(context, sex);
            textView.setText(text);
        }
    }
}
