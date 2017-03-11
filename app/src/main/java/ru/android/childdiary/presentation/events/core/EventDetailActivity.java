package ru.android.childdiary.presentation.events.core;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.widgets.CustomEditText;
import ru.android.childdiary.utils.KeyboardUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public abstract class EventDetailActivity<E extends MasterEvent> extends BaseMvpActivity implements EventDetailView<E> {
    @InjectPresenter
    public EventDetailPresenter presenter;

    protected E event;

    @BindView(R.id.editTextNote)
    CustomEditText editTextNote;

    @BindView(R.id.buttonDone)
    Button buttonDone;

    @BindView(R.id.dummy)
    View dummy;

    private ViewGroup eventDetailsView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        editTextNote.setOnKeyboardHiddenListener(this::hideKeyboardAndClearFocus);
    }

    @Override
    protected void setContentViewBeforeBind() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View contentView = inflater.inflate(getContentLayoutResourceId(), null);
        eventDetailsView = ButterKnife.findById(this, R.id.eventDetailsView);
        eventDetailsView.addView(contentView);
    }

    private void hideKeyboardAndClearFocus(View view) {
        KeyboardUtils.hideKeyboard(this, view);
        view.clearFocus();
        dummy.requestFocus();
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

    @OnClick(R.id.buttonDone)
    void onButtonDoneClick() {
        if (event == null) {
            addEvent();
        } else {
            updateEvent();
        }
    }

    @Override
    public void eventAdded(@NonNull MasterEvent event) {
        finish();
    }

    @Override
    public void eventUpdated(@NonNull MasterEvent event) {
        finish();
    }

    @LayoutRes
    protected abstract int getContentLayoutResourceId();

    protected abstract void addEvent();

    protected abstract void updateEvent();
}
