package ru.android.childdiary.presentation.events.core;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Map;

import ru.android.childdiary.domain.calendar.data.core.LengthValue;
import ru.android.childdiary.domain.calendar.data.core.LinearGroupItem;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.data.core.TimeUnit;
import ru.android.childdiary.domain.core.data.ContentObject;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.dialogs.LengthValueDialogArguments;
import ru.android.childdiary.presentation.core.fields.dialogs.LengthValueDialogFragment;

public abstract class PeriodicEventDetailActivity<V extends PeriodicEventDetailView<T>, T extends MasterEvent & ContentObject<T> & LinearGroupItem<T>>
        extends EventDetailActivity<V, T> implements PeriodicEventDetailView<T> {
    private static final String TAG_LENGTH_VALUE_DIALOG = "TAG_LENGTH_VALUE_DIALOG";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        boolean isLinearGroupFinished = getIntent().getBooleanExtra(ExtraConstants.EXTRA_IS_LINEAR_GROUP_FINISHED, false);
        if (isLinearGroupFinished) {
            getPresenter().requestLengthValueDialog();
        }
    }

    protected abstract PeriodicEventDetailPresenter<V, T> getPresenter();

    @Override
    public void showLengthValueDialog(@NonNull Map<TimeUnit, List<Integer>> timeUnitValues) {
        LengthValue lengthValue = LengthValue.builder().length(4).timeUnit(TimeUnit.DAY).build(); // TODO
        LengthValueDialogFragment dialogFragment = new LengthValueDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_LENGTH_VALUE_DIALOG,
                LengthValueDialogArguments.builder()
                        .sex(getSex())
                        .timeUnitValues(timeUnitValues)
                        .lengthValue(lengthValue)
                        .build());
    }
}
