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

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.utils.StringUtils;

class SexAdapter extends ArrayAdapter<Sex> {
    private final List<Sex> items = new ArrayList<>();

    public SexAdapter(Context context) {
        super(context, R.layout.sex_item);
        items.add(Sex.MALE);
        items.add(Sex.FEMALE);
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
            viewHolder = new ViewHolder();
            viewHolder.textView = ButterKnife.findById(view, android.R.id.text1);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Sex sex = getItem(position);
        String text = StringUtils.sex(getContext(), sex);

        viewHolder.textView.setText(text);

        return view;
    }

    private static class ViewHolder {
        TextView textView;
    }
}
