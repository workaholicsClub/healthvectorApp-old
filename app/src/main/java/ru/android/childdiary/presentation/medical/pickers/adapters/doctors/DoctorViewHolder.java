package ru.android.childdiary.presentation.medical.pickers.adapters.doctors;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;

public class DoctorViewHolder extends BaseRecyclerViewHolder<Doctor> {
    @BindView(R.id.textView)
    TextView textView;

    public DoctorViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(Context context, Sex sex, Doctor item) {
        super.bind(context, sex, item);
        textView.setText(item.getName());
    }
}
