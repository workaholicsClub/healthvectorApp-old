package ru.android.childdiary.presentation.development.partitions.achievements;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.AchievementType;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.childdiary.presentation.development.partitions.achievements.adapters.AchievementGroup;
import ru.android.childdiary.presentation.development.partitions.achievements.adapters.ConcreteAchievementAdapter;
import ru.android.childdiary.presentation.development.partitions.achievements.adapters.ConcreteAchievementItemActionListener;
import ru.android.childdiary.presentation.development.partitions.achievements.add.AddConcreteAchievementActivity;
import ru.android.childdiary.presentation.development.partitions.achievements.edit.EditConcreteAchievementActivity;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryFragment;
import ru.android.childdiary.utils.HtmlUtils;
import ru.android.childdiary.utils.strings.AchievementUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class ConcreteAchievementsFragment extends BaseDevelopmentDiaryFragment<ConcreteAchievementsView>
        implements ConcreteAchievementsView, HtmlUtils.OnLinkClickListener, ConcreteAchievementItemActionListener {
    private static final String LINK_ADD = "add";

    @Getter
    @InjectPresenter
    ConcreteAchievementsPresenter presenter;

    @Getter
    private ConcreteAchievementAdapter adapter;

    @Override
    protected void setupUi() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ConcreteAchievementAdapter(getContext(), Collections.emptyList(), this, fabController);
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

        List<ConcreteAchievement> concreteAchievements = state.getConcreteAchievements();
        List<AchievementGroup> groups = Observable.fromArray(AchievementType.values())
                .map(achievementType -> {
                    List<ConcreteAchievement> children = Observable.fromIterable(concreteAchievements)
                            .filter(concreteAchievement -> concreteAchievement.getAchievementType() == achievementType)
                            .toList()
                            .blockingGet();
                    return new AchievementGroup(AchievementUtils.toString(getContext(), achievementType), children);
                })
                .toList()
                .blockingGet();
        adapter = new ConcreteAchievementAdapter(getContext(), groups, this, fabController);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(concreteAchievements.isEmpty() ? View.GONE : View.VISIBLE);

        textViewIntention.setVisibility(concreteAchievements.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void navigateToConcreteAchievement(@NonNull Child child, @NonNull ConcreteAchievement concreteAchievement) {
        Intent intent = EditConcreteAchievementActivity.getIntent(getContext(), child, concreteAchievement);
        startActivity(intent);
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
    public void deletionRestrictedAchievement() {
        new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.restrict_delete_achievement_message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    public void delete(ConcreteAchievement item) {
        presenter.delete(item);
    }

    @Override
    public void edit(ConcreteAchievement item) {
        presenter.edit(item);
    }

    @Override
    public void onLinkClick(String url) {
        if (LINK_ADD.equals(url)) {
            presenter.addConcreteAchievement();
        }
    }

    @Override
    public void navigateToConcreteAchievementAdd(@NonNull Child child, @NonNull ConcreteAchievement defaultConcreteAchievement) {
        Intent intent = AddConcreteAchievementActivity.getIntent(getContext(), child, defaultConcreteAchievement);
        startActivity(intent);
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
