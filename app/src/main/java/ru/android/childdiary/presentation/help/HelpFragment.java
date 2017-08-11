package ru.android.childdiary.presentation.help;

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
import ru.android.childdiary.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.childdiary.presentation.onboarding.AppIntroActivity;
import ru.android.childdiary.presentation.settings.adapters.SettingsAdapter;
import ru.android.childdiary.presentation.settings.adapters.items.BaseSettingsItem;
import ru.android.childdiary.presentation.settings.adapters.items.IntentSettingsItem;

public class HelpFragment extends AppPartitionFragment implements HelpView,
        IntentSettingsItem.Listener {
    @InjectPresenter
    HelpPresenter presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<BaseSettingsItem> fixedItems;
    private SettingsAdapter settingsAdapter;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void setupUi() {
        initItems();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), false);
        recyclerView.addItemDecoration(dividerItemDecoration);
        settingsAdapter = new SettingsAdapter(getContext());
        settingsAdapter.setItems(fixedItems);
        recyclerView.setAdapter(settingsAdapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        settingsAdapter.setSex(getSex());
    }

    private void initItems() {
        if (fixedItems == null) {
            fixedItems = generateFixedItems();
        }
    }

    private List<BaseSettingsItem> generateFixedItems() {
        List<BaseSettingsItem> items = new ArrayList<>();
        items.add(IntentSettingsItem.builder()
                .id(Intention.ONBOARDING.ordinal())
                .title(getString(R.string.onboarding))
                .iconRes(R.drawable.ic_onboarding)
                .listener(this)
                .enabled(true)
                .build());
        return items;
    }

    @Override
    public void onClick(IntentSettingsItem item) {
        Intention intention = Intention.values()[(int) item.getId()];
        switch (intention) {
            case ONBOARDING:
                Intent intent = AppIntroActivity.getIntent(getContext(), false);
                startActivity(intent);
                break;
            default:
                throw new IllegalArgumentException("Unsupported intention");
        }
    }

    private enum Intention {
        ONBOARDING
    }
}
