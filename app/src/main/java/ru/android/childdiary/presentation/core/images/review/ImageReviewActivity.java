package ru.android.childdiary.presentation.core.images.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class ImageReviewActivity extends BaseMvpActivity {
    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
    }

    public static Intent getIntent(Context context, String relativePath) {
        Intent intent = new Intent(context, ImageReviewActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_RELATIVE_PATH, relativePath);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        String relativePath = getIntent().getStringExtra(ExtraConstants.EXTRA_RELATIVE_PATH);
        imageView.setImageDrawable(ResourcesUtils.getPhotoDrawable(this, relativePath));
    }
}
