package ru.android.childdiary.presentation.development.partitions.achievements;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.AchievementType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.childdiary.presentation.development.partitions.achievements.adapters.AchievementGroup;
import ru.android.childdiary.presentation.development.partitions.achievements.adapters.ConcreteAchievementAdapter;
import ru.android.childdiary.presentation.development.partitions.achievements.adapters.ConcreteAchievementItemActionListener;
import ru.android.childdiary.presentation.development.partitions.achievements.add.AddConcreteAchievementActivity;
import ru.android.childdiary.presentation.development.partitions.achievements.edit.EditConcreteAchievementActivity;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import ru.android.childdiary.presentation.development.partitions.achievements.expandablerecyclerview.models.ExpandableGroup;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryFragment;
import ru.android.childdiary.utils.HtmlUtils;
import ru.android.childdiary.utils.strings.StringUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class ConcreteAchievementsFragment extends BaseDevelopmentDiaryFragment<ConcreteAchievementsView>
        implements ConcreteAchievementsView, HtmlUtils.OnLinkClickListener,
        ConcreteAchievementItemActionListener, GroupExpandCollapseListener {
    private static final String LINK_ADD = "add";

    @Inject
    RxSharedPreferences preferences;

    @Getter
    @InjectPresenter
    ConcreteAchievementsPresenter presenter;

    @Getter
    private ConcreteAchievementAdapter adapter;
    private Bundle savedState;
    private boolean firstTime = true;

    @Override
    protected void injectFragment(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ConcreteAchievementAdapter(getContext(), Collections.emptyList(), this, fabController);
        adapter.setGroupExpandCollapseListener(this);
        adapter.setSex(getSex());
        adapter.onRestoreInstanceState(savedInstanceState);
        savedState = savedInstanceState;
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
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
                .flatMapSingle(achievementType -> Observable.fromIterable(concreteAchievements)
                        .filter(concreteAchievement -> concreteAchievement.getAchievementType() == achievementType)
                        .toList()
                        .map(children -> new AchievementGroup(StringUtils.achievementType(getContext(), achievementType), children)))
                .filter(achievementGroup -> achievementGroup.getItemCount() > 0)
                .toList()
                .blockingGet();
        adapter = new ConcreteAchievementAdapter(getContext(), groups, this, fabController);
        adapter.setGroupExpandCollapseListener(this);
        adapter.setSex(getSex());
        if (firstTime) {
            adapter.onRestoreInstanceState(savedState);
            firstTime = false;
        } else {
            boolean[] indexes = readIndexes();
            if (indexes != null) {
                adapter.setExpandedGroupIndexes(indexes);
            }
        }
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
    public void onGroupExpanded(ExpandableGroup group) {
        saveIndexes();
    }

    @Override
    public void onGroupCollapsed(ExpandableGroup group) {
        saveIndexes();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.closeAllItems(); // для одинакового поведения с Мед. данными
    }

    private void saveIndexes() {
        boolean[] indexes = adapter.getExpandedGroupIndexes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indexes.length; ++i) {
            sb.append(indexes[i]);
            sb.append(i == indexes.length - 1 ? "" : ",");
        }
        preferences.getString("sdf").set(sb.toString());
    }

    private boolean[] readIndexes() {
        String s = preferences.getString("sdf").get();
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        String[] parts = s.split(",");
        boolean[] indexes = new boolean[parts.length];
        for (int i = 0; i < parts.length; ++i) {
            boolean b = Boolean.parseBoolean(parts[i]);
            indexes[i] = b;
        }
        return indexes;
    }
}
