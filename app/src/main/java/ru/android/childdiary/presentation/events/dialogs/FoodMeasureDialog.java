package ru.android.childdiary.presentation.events.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.ButterKnife;
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
        View view = inflater.inflate(R.layout.dialog_food_measure, null);
        EditText editText = ButterKnife.findById(view, R.id.editText);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(sex))
                .setView(view)
                .setTitle(R.string.food_measure_dialog_title)
                .setPositiveButton(R.string.OK, (dialog, which) -> {
                    String text = editText.getText().toString();
                    if (TextUtils.isEmpty(text)) {
                        return;
                    }
                    if (listener != null) {
                        FoodMeasure foodMeasure = FoodMeasure.builder().name(text).build();
                        listener.onSetFoodMeasure(getTag(), foodMeasure);
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
        void onSetFoodMeasure(String tag, @NonNull FoodMeasure foodMeasure);
    }
}
