package ru.android.childdiary.presentation.main;

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

import java.util.List;

import butterknife.ButterKnife;
import ru.android.childdiary.R;

class AccountHeaderActionAdapter extends ArrayAdapter<IProfile> {
    private final IProfile[] items;

    public AccountHeaderActionAdapter(Context context, List<IProfile> profiles) {
        super(context, getLayoutResourceId());
        items = profiles.toArray(new IProfile[profiles.size()]);
    }

    @LayoutRes
    private static int getLayoutResourceId() {
        return R.layout.account_header_action_item;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Nullable
    @Override
    public IProfile getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return items[position].getIdentifier();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(getLayoutResourceId(), null);
        }

        IProfile profile = getItem(position);
        TextView textView = ButterKnife.findById(v, android.R.id.text1);
        textView.setText(profile.getName().getText());
        ImageView imageView = ButterKnife.findById(v, R.id.imageViewPhoto);
        Drawable icon = profile.getIcon() == null ? null : profile.getIcon().getIcon();
        imageView.setVisibility(icon == null ? View.GONE : View.VISIBLE);
        imageView.setImageDrawable(icon);

        return v;
    }
}
