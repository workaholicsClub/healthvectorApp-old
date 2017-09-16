package ru.android.childdiary.presentation.core.dialogs;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.utils.ui.FontUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class ProgressDialogFragment extends BaseMvpDialogFragment<ProgressDialogArguments> {
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.line)
    View line;

    @BindView(R.id.message)
    TextView message;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return R.layout.dialog_progress;
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
        Typeface typeface = FontUtils.getTypefaceRegular(getContext());
        title.setText(dialogArguments.getTitle());
        message.setText(dialogArguments.getMessage());
        //noinspection deprecation
        title.setTextAppearance(getContext(), ResourcesUtils.getDialogTitleTextAppearance(dialogArguments.getSex()));
        title.setTypeface(typeface);
        line.setBackgroundColor(ThemeUtils.getColorPrimary(getContext(), dialogArguments.getSex()));
    }

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view, @Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex())) {
            @Override
            public void onBackPressed() {
            }
        };
        if (view != null) {
            dialog.setContentView(view);
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
