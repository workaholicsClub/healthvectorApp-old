package ru.android.childdiary.presentation.development.partitions.achievements.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.AchievementType;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.presentation.core.fields.widgets.FieldAchievementTypeRadioView;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class AchievementTypeDialogFragment extends BaseMvpDialogFragment<AchievementTypeDialogArguments> {
    @BindView(R.id.achievementTypeRadioView)
    FieldAchievementTypeRadioView achievementTypeRadioView;

    @Nullable
    private Listener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else if (getParentFragment() instanceof Listener) {
            listener = (Listener) getParentFragment();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.dialog_achievement_type;
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        achievementTypeRadioView.setValues(AchievementType.values());
        achievementTypeRadioView.setSelected(dialogArguments.getAchievementType());
        achievementTypeRadioView.setSex(dialogArguments.getSex());
    }

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view, @Nullable Bundle savedInstanceState) {
        @SuppressWarnings("ConstantConditions")
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(view)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    AchievementType achievementType = achievementTypeRadioView.getSelected();
                    if (listener != null) {
                        listener.onAchievementTypeSet(achievementType);
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    public interface Listener {
        void onAchievementTypeSet(@NonNull AchievementType achievementType);
    }
}
