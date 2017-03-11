package ru.android.childdiary.presentation.events;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.events.core.EventDetailActivity;
import ru.android.childdiary.utils.ui.ResourcesUtils;

public class FeedEventDetailActivity extends EventDetailActivity<FeedEvent> {
    public static Intent getIntent(Context context, @Nullable Long masterEventId) {
        Intent intent = new Intent(context, FeedEventDetailActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_EVENT_ID, masterEventId);
        return intent;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setLogo(ResourcesUtils.getFeedEventLogoRes(sex));
        getSupportActionBar().setTitle(R.string.event_feed);
    }

    @Override
    protected View getContentView() {
        return new View(this);
    }

    @Override
    public void showEventDetail(@NonNull FeedEvent event) {
    }
}
