package ru.android.childdiary.presentation.development.partitions.achievements;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindDimen;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.achievement.ConcreteAchievement;
import ru.android.childdiary.presentation.core.adapters.decorators.DividerItemDecoration;
import ru.android.childdiary.presentation.development.partitions.achievements.adapters.ConcreteAchievementActionListener;
import ru.android.childdiary.presentation.development.partitions.achievements.adapters.ConcreteAchievementAdapter;
import ru.android.childdiary.presentation.development.partitions.achievements.edit.EditConcreteAchievementActivity;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class ConcreteAchievementsFragment extends BaseDevelopmentDiaryFragment<ConcreteAchievementsView>
        implements ConcreteAchievementsView, ConcreteAchievementActionListener {
    @BindDimen(R.dimen.divider_padding)
    int DIVIDER_PADDING;

    @Getter
    @InjectPresenter
    ConcreteAchievementsPresenter presenter;

    @Getter
    ConcreteAchievementAdapter adapter;

    @Override
    protected void setupUi() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        Drawable divider = ContextCompat.getDrawable(getContext(), R.drawable.divider);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(divider, DIVIDER_PADDING);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new ConcreteAchievementAdapter(getContext(), this, fabController);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        textViewIntention.setVisibility(View.GONE);
        textViewIntention.setText(R.string.no_achievements);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        recyclerViewChips.setVisibility(View.GONE);

        line.setVisibility(View.GONE);
    }

    @Override
    public void showConcreteAchievementsState(@NonNull ConcreteAchievementsState state) {
        progressBar.setVisibility(View.GONE);

        Child child = state.getChild();
        showChild(child);

        List<ConcreteAchievement> concreteAchievements = state.getConcreteAchievements();
        adapter.setItems(concreteAchievements);
        adapter.setFabController(child.getId() == null ? null : fabController, isSelected());
        recyclerView.setVisibility(concreteAchievements.isEmpty() ? View.GONE : View.VISIBLE);

        line.setVisibility(concreteAchievements.isEmpty() ? View.GONE : View.VISIBLE);
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
}
