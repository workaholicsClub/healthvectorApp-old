package ru.android.childdiary.presentation.events.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.calendar.events.core.Food;

public class FoodDialog extends AddValueDialog {
    private Listener listener;

    @Override
    protected int getMaxLength() {
        return getResources().getInteger(R.integer.max_length_event_detail_food);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.food_dialog_title);
    }

    @Override
    protected void addValue(String name) {
        if (listener != null) {
            listener.onSetFood(getTag(), Food.builder().name(name).build());
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
        void onSetFood(String tag, @NonNull Food food);
    }
}
