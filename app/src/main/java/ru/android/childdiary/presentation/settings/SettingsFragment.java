package ru.android.childdiary.presentation.settings;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.AppPartitionFragment;
import ru.android.childdiary.presentation.settings.adapters.SettingsAdapter;
import ru.android.childdiary.presentation.settings.adapters.items.BaseSettingsItem;
import ru.android.childdiary.presentation.settings.adapters.items.DelimiterSettingsItem;
import ru.android.childdiary.presentation.settings.adapters.items.GroupSettingsItem;
import ru.android.childdiary.presentation.settings.adapters.items.IntentSettingsItem;

public class SettingsFragment extends AppPartitionFragment implements SettingsView {
    @InjectPresenter
    SettingsPresenter presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private SettingsAdapter settingsAdapter;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void setupUi() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        settingsAdapter = new SettingsAdapter(getContext());
        settingsAdapter.setItems(generateItems());
        recyclerView.setAdapter(settingsAdapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
    }

    private List<BaseSettingsItem> generateItems() {
        List<BaseSettingsItem> items = new ArrayList<>();
        int id = 0;
        // TODO: generate child profiles items with special ids
        items.add(GroupSettingsItem.builder()
                .id(++id)
                .title(getString(R.string.settings_notifications))
                .build());
        items.add(IntentSettingsItem.builder()
                .id(++id)
                .title(getString(R.string.settings_setup_notifications))
                .iconRes(R.drawable.ic_notify_time)
                .intent(new Intent())
                .build());
        items.add(DelimiterSettingsItem.builder()
                .id(++id)
                .build());
        items.add(GroupSettingsItem.builder()
                .id(++id)
                .title(getString(R.string.settings_data_control))
                .build());
        items.add(IntentSettingsItem.builder()
                .id(++id)
                .title(getString(R.string.settings_account))
                .iconRes(R.drawable.ic_settings_account)
                .intent(new Intent())
                .build());
        items.add(IntentSettingsItem.builder()
                .id(++id)
                .title(getString(R.string.settings_backup_data))
                .iconRes(R.drawable.ic_settings_backup_data)
                .intent(new Intent())
                .build());
        items.add(IntentSettingsItem.builder()
                .id(++id)
                .title(getString(R.string.settings_restore_data))
                .iconRes(R.drawable.ic_settings_restore_data)
                .intent(new Intent())
                .build());
        items.add(DelimiterSettingsItem.builder()
                .id(++id)
                .build());
        items.add(GroupSettingsItem.builder()
                .id(++id)
                .title(getString(R.string.settings_additional))
                .build());
        items.add(IntentSettingsItem.builder()
                .id(++id)
                .title(getString(R.string.settings_day_length))
                .iconRes(R.drawable.ic_length)
                .intent(new Intent())
                .build());
        items.add(IntentSettingsItem.builder()
                .id(++id)
                .title(getString(R.string.settings_food_measure))
                .iconRes(R.drawable.ic_food_measure)
                .intent(new Intent())
                .build());
        items.add(IntentSettingsItem.builder()
                .id(++id)
                .title(getString(R.string.settings_food))
                .iconRes(R.drawable.ic_food)
                .intent(new Intent())
                .build());
        return items;
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        settingsAdapter.setSex(getSex());
    }
}
