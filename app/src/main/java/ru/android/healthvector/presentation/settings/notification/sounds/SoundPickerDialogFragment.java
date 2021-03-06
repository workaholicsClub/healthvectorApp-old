package ru.android.healthvector.presentation.settings.notification.sounds;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import icepick.State;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.calendar.data.core.SoundInfo;
import ru.android.healthvector.presentation.core.BaseMvpDialogFragment;
import ru.android.healthvector.presentation.settings.notification.sounds.adapters.SoundInfoAdapter;
import ru.android.healthvector.presentation.settings.notification.sounds.adapters.SoundSelectedListener;
import ru.android.healthvector.utils.RingtoneUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class SoundPickerDialogFragment extends BaseMvpDialogFragment<SoundPickerDialogArguments>
        implements SoundSelectedListener {
    @State
    int selectedPosition = -1;

    private SoundHelper soundHelper;
    private SoundPlayer soundPlayer;
    private RecyclerView recyclerView;

    @Nullable
    private Listener listener;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return 0;
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
    }

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view, @Nullable Bundle savedInstanceState) {
        List<SoundInfo> sounds = soundHelper.getAllAvailableSounds();
        SoundInfo selectedSoundInfo = dialogArguments.getSelectedSoundInfo();
        if (selectedSoundInfo == null) {
            selectedSoundInfo = SoundInfo.NULL;
        }

        if (savedInstanceState == null) {
            for (int i = 0; i < sounds.size(); ++i) {
                if (selectedSoundInfo.equals(sounds.get(i))) {
                    selectedPosition = i;
                    break;
                }
            }
        }

        recyclerView = new RecyclerView(getContext());
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        SoundInfoAdapter adapter = new SoundInfoAdapter(getContext(), selectedPosition, this);
        recyclerView.setAdapter(adapter);

        adapter.setSex(dialogArguments.getSex());
        adapter.setItems(sounds);

        @SuppressWarnings("ConstantConditions")
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(recyclerView)
                .setTitle(R.string.select_sound)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    if (listener != null) {
                        listener.onSoundSelected(adapter.getSelectedItem());
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    @Override
    public void onSoundSelected(int position, @NonNull SoundInfo soundInfo) {
        selectedPosition = position;
        Uri soundUri = soundInfo == SoundInfo.NULL ? RingtoneUtils.getDefaultNotificationUri() : soundInfo.getSoundUri();
        if (soundUri == null) {
            logger.error("no sound uri found");
            return;
        }
        try {
            soundPlayer.play(soundUri);
        } catch (Exception e) {
            logger.error("failed to play sound", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().post(() -> ((LinearLayoutManager) recyclerView.getLayoutManager())
                .scrollToPositionWithOffset(selectedPosition, 0));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundHelper = new SoundHelper(getContext());
        soundPlayer = new SoundPlayer(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundPlayer.release();
    }

    public interface Listener {
        void onSoundSelected(@Nullable SoundInfo soundInfo);
    }
}
