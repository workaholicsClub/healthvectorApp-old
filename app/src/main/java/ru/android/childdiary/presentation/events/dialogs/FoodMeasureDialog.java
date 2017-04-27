package ru.android.childdiary.presentation.events.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.events.core.FoodMeasure;

public class FoodMeasureDialog extends AddValueDialog {
    private Listener listener;

    @Override
    protected int getMaxLength() {
        return getResources().getInteger(R.integer.max_length_event_detail_food_measure);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.food_measure_dialog_title);
    }

    @Override
    protected void addValue(String name) {
        if (listener != null) {
            listener.onSetFoodMeasure(getTag(), FoodMeasure.builder().name(name).build());
        }
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
