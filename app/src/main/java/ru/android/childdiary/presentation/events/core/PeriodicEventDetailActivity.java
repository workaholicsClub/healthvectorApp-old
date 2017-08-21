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
import ru.android.childdiary.domain.core.data.RepeatParametersContainer;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.fields.dialogs.LengthValueDialogArguments;
import ru.android.childdiary.presentation.core.fields.dialogs.LengthValueDialogFragment;
import ru.android.childdiary.utils.strings.EventUtils;

public abstract class PeriodicEventDetailActivity<V extends PeriodicEventDetailView<T>, T extends MasterEvent & ContentObject<T> & LinearGroupItem<T> & RepeatParametersContainer>
        extends EventDetailActivity<V, T> implements PeriodicEventDetailView<T>,
        LengthValueDialogFragment.Listener {
    private static final String TAG_LENGTH_VALUE_DIALOG = "TAG_LENGTH_VALUE_DIALOG";
    private static final String TAG_PROGRESS_DIALOG_GENERATING_EVENTS = "TAG_PROGRESS_DIALOG_GENERATING_EVENTS";

    private T linearGroupFinishedEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection unchecked
        linearGroupFinishedEvent = (T) getIntent().getSerializableExtra(ExtraConstants.EXTRA_LINEAR_GROUP_FINISHED_EVENT);
        if (linearGroupFinishedEvent != null) {
            getPresenter().requestLengthValueDialog(linearGroupFinishedEvent);
        }
    }

    protected abstract PeriodicEventDetailPresenter<V, T> getPresenter();

    @Override
    public void showLengthValueDialog(@NonNull Map<TimeUnit, List<Integer>> timeUnitValues) {
        String eventStr = EventUtils.getTitleAndDescription(this, linearGroupFinishedEvent);
        LengthValue lengthValue = linearGroupFinishedEvent.getRepeatParameters().getLength();
        LengthValueDialogFragment dialogFragment = new LengthValueDialogFragment();
        dialogFragment.showAllowingStateLoss(getSupportFragmentManager(), TAG_LENGTH_VALUE_DIALOG,
                LengthValueDialogArguments.builder()
                        .sex(getSex())
                        .timeUnitValues(timeUnitValues)
                        .lengthValue(lengthValue)
                        .description(getString(R.string.linear_group_finished_dialog_description, eventStr))
                        .cancelable(false)
                        .build());
    }

    @Override
    public void onSetLengthValue(String tag, @NonNull LengthValue lengthValue) {
        getPresenter().continueLinearGroup(linearGroupFinishedEvent, lengthValue);
    }

    @Override
    public void eventsAdded(int count) {
        showToast(getResources().getQuantityString(R.plurals.numberOfAddedEvents, count, count));
    }

    @Override
    public void showGeneratingEvents(boolean loading) {
        if (loading) {
            showProgress(TAG_PROGRESS_DIALOG_GENERATING_EVENTS,
                    getString(R.string.please_wait),
                    getString(R.string.events_generating));
        } else {
            hideProgress(TAG_PROGRESS_DIALOG_GENERATING_EVENTS);
        }
    }
}
