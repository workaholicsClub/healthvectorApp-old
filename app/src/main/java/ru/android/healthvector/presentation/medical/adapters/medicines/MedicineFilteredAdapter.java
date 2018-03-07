package ru.android.healthvector.presentation.medical.adapters.medicines;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokenautocomplete.FilteredArrayAdapter;

import java.util.List;

import butterknife.ButterKnife;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.utils.strings.StringUtils;

public class MedicineFilteredAdapter extends FilteredArrayAdapter<Medicine> {
    public MedicineFilteredAdapter(Context context, List<Medicine> medicines) {
        super(context, R.layout.auto_complete_item, medicines);
    }

    @Override
    protected boolean keepObject(Medicine obj, String mask) {
        return StringUtils.contains(obj.getName(), mask, false);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        Medicine medicine = getItem(position);

        TextView textView = ButterKnife.findById(view, android.R.id.text1);
        textView.setText(medicine == null ? null : medicine.getName());

        return view;
    }
}
