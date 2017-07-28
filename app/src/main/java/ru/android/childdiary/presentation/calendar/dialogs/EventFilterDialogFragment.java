package ru.android.childdiary.presentation.calendar.dialogs;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;

public class EventFilterDialogFragment extends BaseMvpDialogFragment<EventFilterDialogArguments> {
    @Override
    protected int getLayoutResourceId() {
        return 0;
    }

    @Override
    protected void setupUi() {
    }

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view) {
        return null;
    }
}
