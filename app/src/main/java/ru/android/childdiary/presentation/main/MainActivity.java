package ru.android.childdiary.presentation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.joda.time.LocalTime;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Antropometry;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseActivity;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {
    @InjectPresenter
    MainPresenter presenter;

    @BindView(R.id.textViewChild)
    TextView textViewChild;

    @BindView(R.id.textViewAntropometry)
    TextView textViewAntropometry;

    public static Intent getIntent(AppCompatActivity activityContext) {
        return new Intent(activityContext, MainActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    @OnClick(R.id.buttonAddChild)
    void onAddChild() {
        presenter.addChild(Child.builder().name("test" + System.currentTimeMillis()).birthTime(LocalTime.now()).build());
    }

    @OnClick(R.id.buttonDeleteChild)
    void onDeleteChild() {

    }

    @OnClick(R.id.buttonAddAntropometry)
    void onAddAntropometry() {

    }

    @OnClick(R.id.buttonDeleteAntropometry)
    void onDeleteAntropometry() {

    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showChildList(List<Child> childList) {
        String text = Arrays.toString(childList.toArray(new Child[childList.size()]));
        textViewChild.setText(text);
    }

    @Override
    public void showAntropometryList(List<Antropometry> antropometryList) {
        String text = Arrays.toString(antropometryList.toArray(new Antropometry[antropometryList.size()]));
        textViewAntropometry.setText(text);
    }
}
