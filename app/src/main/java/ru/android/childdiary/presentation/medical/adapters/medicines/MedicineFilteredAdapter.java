package ru.android.childdiary.presentation.medical.adapters.medicines;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokenautocomplete.FilteredArrayAdapter;

import java.util.List;

import butterknife.ButterKnife;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

public class MedicineFilteredAdapter extends FilteredArrayAdapter<Medicine> {
    public MedicineFilteredAdapter(Context context, List<Medicine> medicines) {
        super(context, android.R.layout.simple_list_item_1, medicines);
    }

    @Override
    protected boolean keepObject(Medicine obj, String mask) {
        mask = mask.toLowerCase();
        return obj.getName() != null
                && (obj.getName().toLowerCase().startsWith(mask)
                || obj.getName().toLowerCase().startsWith(mask));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        Medicine medicine = getItem(position);

        TextView textView = ButterKnife.findById(view, android.R.id.text1);
        textView.setText(medicine.getName());

        return view;
    }
}
