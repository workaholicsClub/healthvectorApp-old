package ru.android.childdiary.presentation.profile.edit.image;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.yalantis.ucrop.UCropActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.ui.ConfigUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CropActivity extends UCropActivity {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private Method rotateByAngle;

    private void rotateByAngle() {
        if (rotateByAngle != null) {
            try {
                rotateByAngle.invoke(this, 90);
            } catch (IllegalAccessException e) {
                logger.error("failed to access method", e);
            } catch (InvocationTargetException e) {
                logger.error("failed to invoke method", e);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ConfigUtils.setupOrientation(this);
        super.onCreate(savedInstanceState);

        final Toolbar toolbar = ButterKnife.findById(this, com.yalantis.ucrop.R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);

        final TextView toolbarTitle = ButterKnife.findById(this, com.yalantis.ucrop.R.id.toolbar_title);
        toolbarTitle.setText(R.string.crop_image_title);
        //noinspection deprecation
        toolbarTitle.setTextAppearance(this, R.style.ToolbarTitleTextAppearance);

        toolbar.setContentInsetStartWithNavigation(0);
        Toolbar.LayoutParams params = (Toolbar.LayoutParams) toolbarTitle.getLayoutParams();
        params.gravity = Gravity.START;
        toolbarTitle.setLayoutParams(params);

        try {
            rotateByAngle = UCropActivity.class.getDeclaredMethod("rotateByAngle", int.class);
            rotateByAngle.setAccessible(true);
        } catch (NoSuchMethodException e) {
            logger.error("method not found", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crop, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_crop:
                cropAndSaveImage();
                return true;
            case R.id.menu_rotate:
                rotateByAngle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
