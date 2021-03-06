package ru.android.healthvector.presentation.core.fields.dialogs.add.food;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;

import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.dictionaries.food.data.Food;
import ru.android.healthvector.presentation.core.fields.dialogs.add.core.AddValueDialogFragment;

public class AddFoodDialogFragment
        extends AddValueDialogFragment<AddFoodDialogArguments, Food, AddFoodView>
        implements AddFoodView {
    @Getter
    @InjectPresenter
    AddFoodPresenter presenter;

    @Nullable
    private Listener listener;

    @Override
    protected int getMaxLength() {
        return getResources().getInteger(R.integer.max_length_name_medium);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.enter_food_name);
    }

    @Override
    protected Food buildItem(@NonNull String name) {
        return Food.builder().nameUser(name).build();
    }

    @Override
    public void added(@NonNull Food item) {
        getDialog().dismiss();
        if (listener != null) {
            listener.onSetFood(getTag(), item);
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
