package ru.android.childdiary.presentation.events.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.FoodMeasure;
import ru.android.childdiary.presentation.core.BaseDialogFragment;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class FoodMeasureDialog extends BaseDialogFragment {
    private Listener listener;

    @Override
    @NonNull
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        init(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_notify_time, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(sex))
                .setView(view)
                .setTitle(R.string.food_measure_dialog_title)
                .setPositiveButton(R.string.OK, (dialog, which) -> {
                    if (listener != null) {
                        listener.onSetFoodMeasure(FoodMeasure.NULL);
                    }
                })
                .setNegativeButton(R.string.Cancel, null);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface Listener {
        void onSetFoodMeasure(@NonNull FoodMeasure foodMeasure);
    }
}
