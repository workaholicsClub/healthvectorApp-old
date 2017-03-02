package ru.android.childdiary.presentation.main.drawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import ru.android.childdiary.R;

public class AccountHeaderActionAdapter extends ArrayAdapter<IProfile> {
    private final List<IProfile> items;

    public AccountHeaderActionAdapter(Context context, List<IProfile> profiles) {
        super(context, getLayoutResourceId());
        items = Collections.unmodifiableList(profiles);
    }

    @LayoutRes
    private static int getLayoutResourceId() {
        return R.layout.account_header_action_item;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public IProfile getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getIdentifier();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(getLayoutResourceId(), null);
            viewHolder = new ViewHolder();
            viewHolder.textView = ButterKnife.findById(view, android.R.id.text1);
            viewHolder.imageViewPhoto = ButterKnife.findById(view, R.id.imageViewPhoto);
            viewHolder.imageViewDropdown = ButterKnife.findById(view, R.id.imageViewDropdown);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        IProfile profile = getItem(position);
        String text = profile.getName().getText();
        Drawable icon = profile.getIcon() == null ? null : profile.getIcon().getIcon();

        viewHolder.imageViewPhoto.setVisibility(icon == null ? View.GONE : View.VISIBLE);
        viewHolder.imageViewPhoto.setImageDrawable(icon);
        viewHolder.textView.setText(text);
        viewHolder.imageViewDropdown.setVisibility(position == 0 ? View.VISIBLE : View.GONE);

        return view;
    }

    private static class ViewHolder {
        ImageView imageViewPhoto;
        TextView textView;
        View imageViewDropdown;
    }
}
