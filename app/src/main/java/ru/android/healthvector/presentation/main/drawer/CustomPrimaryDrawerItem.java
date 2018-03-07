package ru.android.healthvector.presentation.main.drawer;

import android.content.Context;
import android.widget.TextView;

import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

import java.util.List;

import butterknife.ButterKnife;
import ru.android.healthvector.R;
import ru.android.healthvector.utils.ui.FontUtils;

public class CustomPrimaryDrawerItem extends PrimaryDrawerItem {
    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);

        Context context = viewHolder.itemView.getContext();

        TextView textView = ButterKnife.findById(viewHolder.itemView, R.id.material_drawer_name);
        //noinspection deprecation
        textView.setTextAppearance(context, R.style.DrawerItemTextAppearance);
        textView.setTypeface(FontUtils.getTypefaceBold(context));
        viewHolder.itemView.setBackgroundResource(R.drawable.background_selectable);

        onPostBindView(this, viewHolder.itemView);
    }
}
