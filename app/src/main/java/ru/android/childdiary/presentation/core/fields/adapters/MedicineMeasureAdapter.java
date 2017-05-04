package ru.android.childdiary.presentation.core.fields.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;

// TODO remove
public class MedicineMeasureAdapter extends SpinnerItemAdapter<MedicineMeasure, MedicineMeasureAdapter.ViewHolder> {
    public MedicineMeasureAdapter(Context context, List<MedicineMeasure> medicineMeasureList) {
        super(context, medicineMeasureList);
    }

    @Override
    protected MedicineMeasureAdapter.ViewHolder createViewHolder(View view) {
        return new MedicineMeasureAdapter.ViewHolder(view);
    }

    static class ViewHolder extends SpinnerItemViewHolder<MedicineMeasure> {
        public ViewHolder(View view) {
            super(view);
        }

        @Override
        protected String getTextForValue(Context context, @NonNull MedicineMeasure item) {
            return item == MedicineMeasure.NULL
                    ? context.getString(R.string.food_measure_other)
                    : item.getName();
        }
    }
}
