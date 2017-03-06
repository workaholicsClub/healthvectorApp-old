package ru.android.childdiary.presentation.main.drawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.adapters.BaseArrayAdapter;
import ru.android.childdiary.presentation.core.adapters.BaseViewHolder;

public class AccountHeaderActionAdapter extends BaseArrayAdapter<IProfile, AccountHeaderActionAdapter.ViewHolder> {
    public AccountHeaderActionAdapter(Context context, List<IProfile> profiles) {
        super(context, profiles);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.account_header_action_item;
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getIdentifier();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    static class ViewHolder extends BaseViewHolder<IProfile> {
        @BindView(R.id.imageViewPhoto)
        ImageView imageViewPhoto;
        @BindView(android.R.id.text1)
        TextView textView;
        @BindView(R.id.imageViewDropdown)
        View imageViewDropdown;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(Context context, int position, IProfile profile) {
            String text = profile.getName().getText();
            Drawable icon = profile.getIcon() == null ? null : profile.getIcon().getIcon();
            imageViewPhoto.setVisibility(icon == null ? View.GONE : View.VISIBLE);
            imageViewPhoto.setImageDrawable(icon);
            textView.setText(text);
            imageViewDropdown.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        }
    }
}
