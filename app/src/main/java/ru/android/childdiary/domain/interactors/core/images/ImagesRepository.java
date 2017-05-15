package ru.android.childdiary.domain.interactors.core.images;

import android.content.Context;
import android.net.Uri;

import java.io.File;

public interface ImagesRepository {
    String createUniqueImageFile(Context context, Uri resultUri);

    File getCroppedImageFile(Context context);

    File createCapturedImageFile(Context context);
}
