package ru.android.childdiary.presentation.events.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public abstract class EventDetailActivity<EventType extends MasterEvent> extends BaseMvpActivity implements EventDetailView<EventType> {
    @InjectPresenter
    public EventDetailPresenter presenter;

    protected EventType event;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.buttonDone)
    Button buttonDone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        scrollView.addView(getContentView());
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setNavigationIcon(R.drawable.toolbar_action_close);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        buttonDone.setBackgroundResource(ResourcesUtils.getButtonBackgroundRes(sex, true));
    }

    protected abstract View getContentView();
}
