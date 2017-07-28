package ru.android.childdiary.presentation.calendar.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Arrays;
import java.util.Set;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.presentation.calendar.adapters.filter.EventFilterAdapter;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.presentation.core.BaseView;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class EventFilterDialogFragment extends BaseMvpDialogFragment<EventFilterDialogArguments>
        implements BaseView {
    @Nullable
    private Listener listener;

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
        RecyclerView recyclerView = new RecyclerView(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        EventFilterAdapter adapter = new EventFilterAdapter(getContext(), dialogArguments.getSelectedItems());
        recyclerView.setAdapter(adapter);

        adapter.setSex(dialogArguments.getSex());
        adapter.setItems(Arrays.asList(EventType.DIAPER, EventType.SLEEP, EventType.FEED, EventType.PUMP,
                EventType.EXERCISE, EventType.MEDICINE_TAKING, EventType.DOCTOR_VISIT, EventType.OTHER));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                .setView(recyclerView)
                .setTitle(R.string.menu_filter)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    if (listener != null) {
                        listener.onSetFilter(getTag(), adapter.getSelectedItems());
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else if (getParentFragment() instanceof Listener) {
            listener = (Listener) getParentFragment();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface Listener {
        void onSetFilter(String tag, @NonNull Set<EventType> selectedItems);
    }
}
