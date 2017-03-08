package ru.android.childdiary.presentation.profile.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.profile.edit.ProfileEditActivity;
import ru.android.childdiary.utils.DateUtils;
import ru.android.childdiary.utils.DoubleUtils;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class ProfileReviewActivity extends BaseMvpActivity<ProfileReviewPresenter> implements ProfileReviewView {
    @InjectPresenter
    ProfileReviewPresenter presenter;

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
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        getSupportActionBar().setTitle(R.string.profile);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        topPanel.setBackgroundResource(ThemeUtils.getColorPrimaryRes(sex));
        buttonEdit.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(sex, true));
    }

    private void setupViews(@NonNull Child child) {
        imageViewPhoto.setImageDrawable(ResourcesUtils.getChildIcon(this, child));

        textViewName.setText(child.getName());

        textViewSex.setText(StringUtils.sex(this, child.getSex()));

        textViewDate.setText(DateUtils.date(child.getBirthDate()));
        textViewTime.setText(DateUtils.time(child.getBirthTime()));

        textViewBirthHeight.setText(DoubleUtils.heightReview(this, child.getBirthHeight()));
        textViewBirthWeight.setText(DoubleUtils.weightReview(this, child.getBirthWeight()));
    }

    @Override
    public void showChild(@NonNull Child child) {
        changeThemeIfNeeded(child);
        setupViews(child);
    }

    @Override
    public void editChild(@NonNull Child child) {
        navigateToProfileEdit(child);
    }

    @OnClick(R.id.buttonEdit)
    void onButtonEditClick() {
        presenter.editChild();
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

    private void navigateToProfileEdit(@NonNull Child child) {
        Intent intent = ProfileEditActivity.getIntent(this, child);
        startActivity(intent);
    }
}
