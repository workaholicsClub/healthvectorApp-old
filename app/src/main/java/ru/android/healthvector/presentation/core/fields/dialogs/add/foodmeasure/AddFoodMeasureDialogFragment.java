package ru.android.healthvector.presentation.core.fields.dialogs.add.foodmeasure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;

import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.healthvector.presentation.core.fields.dialogs.add.core.AddValueDialogFragment;

public class AddFoodMeasureDialogFragment
        extends AddValueDialogFragment<AddFoodMeasureDialogArguments, FoodMeasure, AddFoodMeasureView>
        implements AddFoodMeasureView {
    @Getter
    @InjectPresenter
    AddFoodMeasurePresenter presenter;

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
    protected FoodMeasure buildItem(@NonNull String name) {
        return FoodMeasure.builder().nameUser(name).build();
    }

    @Override
    public void added(@NonNull FoodMeasure item) {
        getDialog().dismiss();
        if (listener != null) {
            listener.onSetFoodMeasure(getTag(), item);
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
