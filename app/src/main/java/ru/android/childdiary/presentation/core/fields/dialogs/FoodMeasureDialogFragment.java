package ru.android.childdiary.presentation.core.fields.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.dictionaries.foodmeasure.data.FoodMeasure;

public class FoodMeasureDialogFragment extends AddValueDialogFragment<FoodMeasureDialogArguments> {
    @Nullable
    private Listener listener;

    @Override
    protected int getMaxLength() {
        return getResources().getInteger(R.integer.max_length_name_small);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.enter_measure_name);
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
