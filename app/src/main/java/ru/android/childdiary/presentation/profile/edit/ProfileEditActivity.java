package ru.android.childdiary.presentation.profile.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseActivity;

public class ProfileEditActivity extends BaseActivity<ProfileEditPresenter> implements ProfileEditView {
    @InjectPresenter
    ProfileEditPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static Intent getIntent(Context context, @Nullable Child child) {
        return new Intent(context, ProfileEditActivity.class);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.add_child);
    }
}
