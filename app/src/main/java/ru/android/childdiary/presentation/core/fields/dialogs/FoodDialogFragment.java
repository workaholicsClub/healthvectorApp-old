package ru.android.childdiary.presentation.core.fields.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.dictionaries.food.data.Food;

public class FoodDialogFragment extends AddValueDialogFragment<FoodDialogArguments> {
    @Nullable
    private Listener listener;

    @Override
    protected int getMaxLength() {
        return getResources().getInteger(R.integer.max_length_name_small);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.enter_food_name);
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
