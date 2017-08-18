package ru.android.childdiary.presentation.settings.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.calendar.data.core.EventNotification;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.childdiary.presentation.settings.notification.NotificationActivity;
import ru.android.childdiary.presentation.settings.notifications.adapters.EventNotificationAdapter;
import ru.android.childdiary.presentation.settings.notifications.adapters.ItemClickedListener;

public class NotificationsActivity extends BaseMvpActivity implements NotificationsView,
        ItemClickedListener {
    @InjectPresenter
    NotificationsPresenter presenter;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.textViewIntention)
    TextView textViewIntention;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private EventNotificationAdapter adapter;

    public static Intent getIntent(Context context, @Nullable Sex sex) {
        return new Intent(context, NotificationsActivity.class)
                .putExtra(ExtraConstants.EXTRA_SEX, sex);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EventNotificationAdapter(this, this);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(this, adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter.setSex(getSex());
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.settings_setup_notifications);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        if (adapter != null) {
            adapter.setSex(getSex());
        }
    }

    @Override
    public void showNotificationSettings(@NonNull List<EventNotification> eventNotifications) {
        progressBar.setVisibility(View.GONE);
        adapter.setItems(eventNotifications);
        recyclerView.setVisibility(eventNotifications.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onItemClicked(@NonNull EventNotification eventNotification) {
        startActivity(NotificationActivity.getIntent(this, getSex(), eventNotification.getEventType()));
    }
}
