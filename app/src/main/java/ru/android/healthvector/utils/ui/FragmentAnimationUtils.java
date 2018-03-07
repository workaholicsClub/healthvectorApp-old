package ru.android.healthvector.utils.ui;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ru.android.healthvector.R;

import static android.support.v4.app.FragmentTransaction.TRANSIT_UNSET;

public class FragmentAnimationUtils {
    public static void slideTo(Fragment fragment,
                               @IdRes int fragmentContainerId,
                               FragmentManager fragmentManager,
                               @Nullable Boolean forward) {
        // используются операции hide-add вместо replace,
        // т.к. анимация при remove некорректно работает
        // на некоторых устройствах Android 4.4.2 (Sony, БиЛайн)
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (forward == null) {
            transaction.setTransition(TRANSIT_UNSET);
        } else {
            if (forward) {
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            } else {
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        }
        Fragment oldFragment = fragmentManager.findFragmentById(fragmentContainerId);
        if (oldFragment != null) {
            transaction.hide(oldFragment);
        }
        transaction.add(fragmentContainerId, fragment);
        transaction.commit();
    }
}
