package ru.android.healthvector.presentation.settings.notification.sounds.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.calendar.data.core.SoundInfo;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.healthvector.utils.ObjectUtils;

public class SoundInfoAdapter extends BaseRecyclerViewAdapter<SoundInfo, SoundInfoViewHolder>
        implements SoundSelectedListener {
    private final SoundSelectedListener listener;
    @Getter
    private int selectedItemPosition;

    public SoundInfoAdapter(Context context, int selectedItemPosition, @NonNull SoundSelectedListener listener) {
        super(context);
        this.selectedItemPosition = selectedItemPosition;
        this.listener = listener;
    }

    @Nullable
    public SoundInfo getSelectedItem() {
        return selectedItemPosition < 0 ? null : items.get(selectedItemPosition);
    }

    @Override
    protected SoundInfoViewHolder createUserViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.sound_info_item, parent, false);
        return new SoundInfoViewHolder(v, this);
    }

    @Override
    protected final void bindUserViewHolder(SoundInfoViewHolder viewHolder, int position) {
        super.bindUserViewHolder(viewHolder, position);
        boolean selected = selectedItemPosition == position;
        viewHolder.setSelected(selected, sex);
    }

    @Override
    public boolean areItemsTheSame(SoundInfo oldItem, SoundInfo newItem) {
        return ObjectUtils.equals(oldItem, newItem);
    }

    @Override
    public void onSoundSelected(int position, @NonNull SoundInfo soundInfo) {
        int oldPosition = selectedItemPosition;
        selectedItemPosition = position;
        notifyItemChanged(oldPosition);
        notifyItemChanged(position);
        listener.onSoundSelected(position, soundInfo);
    }

    @Override
    public boolean paintDividers() {
        return false;
    }

    @Override
    public boolean useFooter() {
        return false;
    }
}
