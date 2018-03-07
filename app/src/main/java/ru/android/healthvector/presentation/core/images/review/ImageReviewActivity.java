package ru.android.healthvector.presentation.core.images.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import butterknife.BindView;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.presentation.core.BaseMvpActivity;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.utils.ui.ResourcesUtils;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class ImageReviewActivity extends BaseMvpActivity {
    @BindView(R.id.imageView)
    ImageView imageView;

    private boolean readOnly;

    public static Intent getIntent(Context context, String relativePath, @Nullable Sex sex,
                                   boolean readOnly) {
        return new Intent(context, ImageReviewActivity.class)
                .putExtra(ExtraConstants.EXTRA_RELATIVE_PATH, relativePath)
                .putExtra(ExtraConstants.EXTRA_SEX, sex)
                .putExtra(ExtraConstants.EXTRA_READ_ONLY, readOnly);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        String relativePath = getIntent().getStringExtra(ExtraConstants.EXTRA_RELATIVE_PATH);
        imageView.setImageDrawable(ResourcesUtils.getPhotoDrawable(this, relativePath));
        readOnly = getIntent().getBooleanExtra(ExtraConstants.EXTRA_READ_ONLY, false);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.image_review);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (readOnly) {
            return super.onCreateOptionsMenu(menu);
        }
        removeToolbarMargin();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (readOnly) {
            return super.onOptionsItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.menu_delete:
                new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                        .setTitle(R.string.delete_photo_confirmation_dialog_title)
                        .setPositiveButton(R.string.delete,
                                (dialog, which) -> {
                                    setResult(RESULT_OK);
                                    finish();
                                })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
