package ru.android.childdiary.presentation.medical.adapters.visits;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokenautocomplete.FilteredArrayAdapter;

import java.util.List;

import butterknife.ButterKnife;
import ru.android.childdiary.domain.interactors.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.utils.strings.StringUtils;

public class DoctorFilteredAdapter extends FilteredArrayAdapter<Doctor> {
    public DoctorFilteredAdapter(Context context, List<Doctor> doctors) {
        super(context, android.R.layout.simple_list_item_1, doctors);
    }

    @Override
    protected boolean keepObject(Doctor obj, String mask) {
        return StringUtils.contains(obj.getName(), mask, false);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        Doctor doctor = getItem(position);

        TextView textView = ButterKnife.findById(view, android.R.id.text1);
        textView.setText(doctor == null ? null : doctor.getName());

        return view;
    }
}
