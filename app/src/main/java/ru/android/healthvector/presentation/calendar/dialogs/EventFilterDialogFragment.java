package ru.android.healthvector.presentation.calendar.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Arrays;
import java.util.Set;

import ru.android.healthvector.R;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.presentation.calendar.adapters.filter.EventFilterAdapter;
import ru.android.healthvector.presentation.core.BaseMvpDialogFragment;
import ru.android.healthvector.presentation.core.BaseView;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class EventFilterDialogFragment extends BaseMvpDialogFragment<EventFilterDialogArguments>
        implements BaseView {
    @Nullable
    private Listener listener;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return 0;
    }

    @Override
    protected void setupUi(@Nullable Bundle savedInstanceState) {
    }

    @NonNull
    @Override
    protected Dialog createDialog(@Nullable View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        EventFilterAdapter adapter = new EventFilterAdapter(getContext(), dialogArguments.getSelectedItems());
        recyclerView.setAdapter(adapter);

        adapter.setSex(dialogArguments.getSex());
        adapter.setItems(Arrays.asList(EventType.DIAPER, EventType.SLEEP, EventType.FEED, EventType.PUMP,
                EventType.EXERCISE, EventType.MEDICINE_TAKING, EventType.DOCTOR_VISIT, EventType.OTHER));

        @SuppressWarnings("ConstantConditions")
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
