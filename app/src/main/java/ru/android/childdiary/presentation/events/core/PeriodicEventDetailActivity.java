package ru.android.childdiary.presentation.events.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Map;

import ru.android.childdiary.R;
import ru.android.childdiary.domain.calendar.data.core.LengthValue;
import ru.android.childdiary.domain.calendar.data.core.LinearGroupItem;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.data.core.TimeUnit;
import ru.android.childdiary.domain.core.data.ContentObject;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.dialogs.LengthValueDialogArguments;
import ru.android.childdiary.presentation.core.fields.dialogs.LengthValueDialogFragment;
import ru.android.childdiary.utils.strings.EventUtils;

public abstract class PeriodicEventDetailActivity<V extends PeriodicEventDetailView<T>, T extends MasterEvent & ContentObject<T> & LinearGroupItem<T>>
        extends EventDetailActivity<V, T> implements PeriodicEventDetailView<T> {
    private static final String TAG_LENGTH_VALUE_DIALOG = "TAG_LENGTH_VALUE_DIALOG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isLinearGroupFinished = getIntent().getBooleanExtra(ExtraConstants.EXTRA_IS_LINEAR_GROUP_FINISHED, false);
        if (isLinearGroupFinished) {
            getPresenter().requestLengthValueDialog();
        }
    }

    protected abstract PeriodicEventDetailPresenter<V, T> getPresenter();

    @Override
    public void showLengthValueDialog(@NonNull Map<TimeUnit, List<Integer>> timeUnitValues) {
        MasterEvent event = (MasterEvent) getIntent().getSerializableExtra(ExtraConstants.EXTRA_MASTER_EVENT); // TODO
        String eventStr = EventUtils.getTitleAndDescription(this, event);
        LengthValue lengthValue = LengthValue.builder().length(4).timeUnit(TimeUnit.DAY).build(); // TODO
        LengthValueDialogFragment dialogFragment = new LengthValueDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_LENGTH_VALUE_DIALOG,
                LengthValueDialogArguments.builder()
                        .sex(getSex())
                        .timeUnitValues(timeUnitValues)
                        .lengthValue(lengthValue)
                        .description(getString(R.string.linear_group_finished_dialog_description, eventStr))
                        .build());
    }
}
