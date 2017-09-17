package ru.android.childdiary.presentation.development.partitions.achievements;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.childdiary.presentation.development.partitions.achievements.adapters.ConcreteAchievementAdapter;
import ru.android.childdiary.presentation.development.partitions.achievements.adapters.ConcreteAchievementItem;
import ru.android.childdiary.presentation.development.partitions.achievements.adapters.ConcreteAchievementItemActionListener;
import ru.android.childdiary.presentation.development.partitions.achievements.add.AddConcreteAchievementActivity;
import ru.android.childdiary.presentation.development.partitions.achievements.edit.EditConcreteAchievementActivity;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryFragment;
import ru.android.childdiary.utils.HtmlUtils;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

import static android.app.Activity.RESULT_OK;

public class ConcreteAchievementsFragment extends BaseDevelopmentDiaryFragment<ConcreteAchievementsView>
        implements ConcreteAchievementsView, HtmlUtils.OnLinkClickListener,
        ConcreteAchievementItemActionListener {
    private static final int REQUEST_ADD = 1;
    private static final int REQUEST_UPDATE = 2;

    private static final String LINK_ADD = "add";

    @Getter
    @InjectPresenter
    ConcreteAchievementsPresenter presenter;

    @Getter
    private ConcreteAchievementAdapter adapter;
    private ConcreteAchievement changedItem;

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ConcreteAchievementAdapter(getContext(), this, fabController);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), true, true);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
        String text = getString(R.string.link_format,
                LINK_ADD,
                getString(R.string.add_achievement));
        HtmlUtils.setupClickableLinks(textViewIntention, text, this, ContextCompat.getColor(getContext(), R.color.intention_text));
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        adapter.setSex(getSex());
    }

    @Override
    public void showConcreteAchievementsState(@NonNull ConcreteAchievementsState state) {
        progressBar.setVisibility(View.GONE);

        Child child = state.getChild();
        showChild(child);

        List<ConcreteAchievementItem> items = state.getConcreteAchievements();
        adapter.setItems(items);
        adapter.setFabController(fabController);
        recyclerView.setVisibility(items.isEmpty() ? View.GONE : View.VISIBLE);

        textViewIntention.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);

        updateScrollPosition(items);
    }

    private void updateScrollPosition(@NonNull List<ConcreteAchievementItem> items) {
        if (changedItem == null) {
            return;
        }
        int i = 0;
        for (ConcreteAchievementItem item : items) {
            if (item.isChild() && ObjectUtils.equals(item.getConcreteAchievement().getId(), changedItem.getId())) {
                break;
            }
            ++i;
        }
        int position = i;
        new Handler().post(() -> ((LinearLayoutManager) recyclerView.getLayoutManager())
                .scrollToPositionWithOffset(position, 0));
        changedItem = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_ADD || requestCode == REQUEST_UPDATE)
                && resultCode == RESULT_OK) {
            changedItem = (ConcreteAchievement) data.getSerializableExtra(ExtraConstants.EXTRA_ITEM);
            presenter.expand(changedItem.getAchievementType());
        }
    }

    @Override
    public void navigateToConcreteAchievement(@NonNull Child child, @NonNull ConcreteAchievement concreteAchievement) {
        Intent intent = EditConcreteAchievementActivity.getIntent(getContext(), child, concreteAchievement);
        startActivityForResult(intent, REQUEST_UPDATE);
    }

    @Override
    public void deleted(@NonNull ConcreteAchievement concreteAchievement) {
    }

    @Override
    public void confirmDelete(@NonNull ConcreteAchievement concreteAchievement) {
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(R.string.delete_achievement_dialog_title)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> presenter.forceDelete(concreteAchievement))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void delete(ConcreteAchievementItem item) {
        if (item.isChild()) {
            presenter.delete(item.getConcreteAchievement());
        }
    }

    @Override
    public void edit(ConcreteAchievementItem item) {
        if (item.isGroup()) {
            presenter.toggle(item.getAchievementType());
        } else {
            presenter.edit(item.getConcreteAchievement());
        }
    }

    @Override
    public void onLinkClick(String url) {
        if (LINK_ADD.equals(url)) {
            presenter.addConcreteAchievement();
        }
    }

    @Override
    public void addItem() {
        presenter.addConcreteAchievement();
    }

    @Override
    public void navigateToConcreteAchievementAdd(@NonNull Child child, @NonNull ConcreteAchievement defaultConcreteAchievement) {
        Intent intent = AddConcreteAchievementActivity.getIntent(getContext(), child, defaultConcreteAchievement);
        startActivityForResult(intent, REQUEST_ADD);
    }

    @Override
    public void noChildSpecified() {
        showToast(getString(R.string.intention_add_child_profile));
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.closeAllItems(); // для одинакового поведения с Мед. данными
    }
}
