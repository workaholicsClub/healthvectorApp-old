package ru.android.childdiary.presentation.settings.notification.ringtones;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.IOException;

class RingtonePlayer {
    private Context mContext;
    private MediaPlayer mMediaPlayer;

    RingtonePlayer(Context context) {
        mContext = context;
        mMediaPlayer = new MediaPlayer();
    }

    void playRingtone(@NonNull Uri uri) throws IOException,
            IllegalArgumentException,
            SecurityException,
            IllegalStateException {
        if (mMediaPlayer == null) {
            // TODO logger
            return;
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.reset();

        mMediaPlayer.setDataSource(mContext, uri);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }

    void release() {
        if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}
