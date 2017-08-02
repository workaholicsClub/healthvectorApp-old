package ru.android.childdiary.presentation.core.fields.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;

import com.tokenautocomplete.FilteredArrayAdapter;

import butterknife.BindView;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.widgets.CustomAutoCompleteTextView;

public class FieldEditTextWithImageAutocompleteView extends FieldEditTextWithImageBaseView {
    @Getter
    @BindView(R.id.editText)
    CustomAutoCompleteTextView editText;

    public FieldEditTextWithImageAutocompleteView(Context context) {
        super(context);
    }

    public FieldEditTextWithImageAutocompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldEditTextWithImageAutocompleteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @LayoutRes
    @Override
    protected int getLayoutResourceId() {
        return R.layout.field_edit_text_with_image_autocomplete;
    }

    public void setFilteredAdapter(FilteredArrayAdapter<String> adapter) {
        editText.setAdapter(adapter);
    }
}
