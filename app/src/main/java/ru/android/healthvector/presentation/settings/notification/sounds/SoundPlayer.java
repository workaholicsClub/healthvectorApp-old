package ru.android.healthvector.presentation.settings.notification.sounds;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;

class SoundPlayer {
    private final Context context;
    private MediaPlayer mediaPlayer;

    public SoundPlayer(Context context) {
        this.context = context;
        mediaPlayer = new MediaPlayer();
    }

    public void play(@NonNull Uri soundUri) throws Exception {
        if (mediaPlayer == null) {
            throw new IllegalStateException("Media player is already released");
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();

        mediaPlayer.setDataSource(context, soundUri);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    public void release() {
        if (mediaPlayer == null) {
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
