package ru.android.childdiary.presentation.profile.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.format.DateTimeFormatter;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.di.modules.ApplicationModule;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.profile.edit.ProfileEditActivity;
import ru.android.childdiary.utils.DoubleUtils;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class ProfileReviewActivity extends BaseMvpActivity<ProfileReviewPresenter> implements ProfileReviewView {
    @InjectPresenter
    ProfileReviewPresenter presenter;

    @Inject
    @Named(ApplicationModule.DATE_FORMATTER)
    DateTimeFormatter dateFormatter;

    @Inject
    @Named(ApplicationModule.TIME_FORMATTER)
    DateTimeFormatter timeFormatter;

    @BindView(R.id.topPanel)
    View topPanel;

    @BindView(R.id.buttonEdit)
    Button buttonEdit;

    @BindView(R.id.imageViewPhoto)
    ImageView imageViewPhoto;

    @BindView(R.id.textViewChildName)
    TextView textViewName;

    @BindView(R.id.textViewSex)
    TextView textViewSex;

    @BindView(R.id.textViewDate)
    TextView textViewDate;

    @BindView(R.id.textViewTime)
    TextView textViewTime;

    @BindView(R.id.textViewBirthHeight)
    TextView textViewBirthHeight;

    @BindView(R.id.textViewBirthWeight)
    TextView textViewBirthWeight;

    public static Intent getIntent(Context context, @NonNull Child child) {
        Intent intent = new Intent(context, ProfileReviewActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_CHILD, child);
        return intent;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_review);

        Child child = getIntent().getParcelableExtra(ExtraConstants.EXTRA_CHILD);
        presenter.loadChild(child.getId());

        getSupportActionBar().setTitle(R.string.profile);
        buttonEdit.setOnClickListener(v -> navigateToProfileEdit(child));

        setupViews(child);
    }

    private void setupViews(Child child) {
        imageViewPhoto.setImageDrawable(ThemeUtils.getChildIcon(this, child));

        textViewName.setText(child.getName());

        textViewSex.setText(StringUtils.print(this, child.getSex()));

        textViewDate.setText(StringUtils.print(child.getBirthDate(), dateFormatter));
        textViewTime.setText(StringUtils.print(child.getBirthTime(), timeFormatter));

        // TODO: форматировать "кг", "см"
        textViewBirthHeight.setText(DoubleUtils.toString(child.getHeight()));
        textViewBirthWeight.setText(DoubleUtils.toString(child.getWeight()));
    }

    @Override
    protected void themeChangedCustom() {
        topPanel.setBackgroundResource(ThemeUtils.getHeaderDrawableRes(this, sex));
        buttonEdit.setBackgroundResource(ThemeUtils.getButtonBackgroundRes(this, sex));
    }

    @Override
    public void childLoaded(@NonNull Child child) {
        changeThemeIfNeeded(child);
        setupViews(child);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToProfileEdit(@Nullable Child child) {
        Intent intent = ProfileEditActivity.getIntent(this, child);
        startActivity(intent);
    }
}
