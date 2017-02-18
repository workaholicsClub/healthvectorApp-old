package ru.android.childdiary.presentation.core;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseDialogFragment extends DialogFragment {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    public BaseDialogFragment() {
    }

    public final void showAllowingStateLoss(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
