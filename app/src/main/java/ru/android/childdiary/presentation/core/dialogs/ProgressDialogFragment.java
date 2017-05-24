package ru.android.childdiary.presentation.core.dialogs;

import android.app.Dialog;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.utils.ui.FontUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class ProgressDialogFragment extends BaseMvpDialogFragment<ProgressDialogArguments> {
    private final Typeface typeface = FontUtils.getTypefaceRegular(getContext());

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.line)
    View line;

    @BindView(R.id.message)
    TextView message;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_progress;
    }

    @Override
    protected void setupUi() {
        title.setText(dialogArguments.getTitle());
        message.setText(dialogArguments.getMessage());
        //noinspection deprecation
        title.setTextAppearance(getContext(), ResourcesUtils.getDialogTitleTextAppearance(dialogArguments.getSex()));
        title.setTypeface(typeface);
        line.setBackgroundColor(ThemeUtils.getColorPrimary(getContext(), dialogArguments.getSex()));
    }

    @NonNull
    @Override
    protected Dialog createDialog(View view) {
        Dialog dialog = new Dialog(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex())) {
            @Override
            public void onBackPressed() {
            }
        };
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
