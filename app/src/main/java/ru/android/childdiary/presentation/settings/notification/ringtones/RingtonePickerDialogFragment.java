package ru.android.childdiary.presentation.settings.notification.ringtones;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.io.IOException;
import java.util.HashMap;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;

public final class RingtonePickerDialogFragment extends BaseMvpDialogFragment<RingtonePickerDialogArguments> {
    @Nullable
    private Listener listener;
    //  private String mCurrentToneTitle;
    //  private Uri mCurrentToneUri;
    private HashMap<String, Uri> mRingTones;
    private RingtonePlayer mRingtonePlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRingtonePlayer = new RingtonePlayer(getActivity());

        mRingTones = new HashMap<>();
        RingtoneUtils.getRingTone(getActivity(), mRingTones);
        RingtoneUtils.getAlarmTones(getActivity(), mRingTones);
        RingtoneUtils.getMusic(getActivity(), mRingTones);
        RingtoneUtils.getNotificationTones(getActivity(), mRingTones);
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return 0;
    }

    @Override
    protected void setupUi() {
    }

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view) {
        int currentSelectionPos = -1;
        if (dialogArguments.getSoundUri() != null) {
            String title = RingtoneUtils.getRingtoneName(getContext(), dialogArguments.getSoundUri());
            if (title != null) {    //Ringtone found for the uri
                currentSelectionPos = getUriPosition(dialogArguments.getSoundUri());
            }
        }

        final String[] titles = mRingTones.keySet().toArray(new String[mRingTones.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                // TODO .setTitle(getArguments().getString(ARG_DIALOG_TITLE));
                .setSingleChoiceItems(titles,
                        currentSelectionPos,
                        (dialog, which) -> {
                            String title = titles[which];
                            Uri uri = mRingTones.get(title);

                            try {
                                mRingtonePlayer.playRingtone(uri);
                            } catch (IOException e) {
                                logger.error("failed to play ringtone", e);
                            }
                        })
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> listener.onRingtoneSelected(null, null))
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    private int getUriPosition(@NonNull Uri uri) {
        Uri[] values = mRingTones.values().toArray(new Uri[mRingTones.size()]);
        for (int i = 0; i < values.length; i++) {
            if (values[i].toString().equals(uri.toString())) {
                return i;
            }
        }
        return -1;
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
    public void onDestroy() {
        super.onDestroy();
        mRingtonePlayer.release();
    }

    public interface Listener {
        void onRingtoneSelected(String ringtoneName, Uri ringtoneUri);
    }
}
