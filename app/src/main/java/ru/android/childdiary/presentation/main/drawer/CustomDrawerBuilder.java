package ru.android.childdiary.presentation.main.drawer;

import com.mikepenz.materialdrawer.DrawerBuilder;

public class CustomDrawerBuilder extends DrawerBuilder {
    public void closeDrawerWithoutAnimation() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mDrawerGravity, false);
        }
    }
}
