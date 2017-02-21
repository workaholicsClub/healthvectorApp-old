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
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import ru.android.childdiary.R;

class SpinnerSexArrayAdapter extends ArrayAdapter<String> {
    private final List<String> items = new ArrayList<>();
    private boolean showDefault;

    public SpinnerSexArrayAdapter(Context context, boolean showDefault) {
        super(context, R.layout.spinner_item);
        items.addAll(Arrays.asList(context.getResources().getStringArray(R.array.sex_variants)));
        this.showDefault = showDefault;
        if (!showDefault) {
            items.remove(0);
        }
    }

    @LayoutRes
    private static int getLayoutResourceId() {
        return R.layout.spinner_item;
    }

    @LayoutRes
    private static int getDropDownLayoutResourceId() {
        return R.layout.spinner_dropdown_item;
    }

    public boolean hideDefault() {
        if (showDefault) {
            showDefault = false;
            items.remove(0);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View contentView, @NonNull ViewGroup viewGroup) {
        return getView(position, contentView, false);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, true);
    }

    private View getView(int position, @Nullable View convertView, boolean isDropDownView) {
        View v = convertView;
        if (v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(isDropDownView ? getDropDownLayoutResourceId() : getLayoutResourceId(), null);
        }

        TextView textView = ButterKnife.findById(v, android.R.id.text1);
        textView.setText(getItem(position));

        if (showDefault && !isDropDownView && position == 0) {
            textView.setTextColor(getContext().getResources().getColor(R.color.disabledTextColor));
        } else {
            textView.setTextColor(getContext().getResources().getColor(R.color.enabledTextColor));
        }

        return v;
    }
}
