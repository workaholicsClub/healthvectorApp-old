package ru.android.childdiary.presentation.main;

import com.mikepenz.materialdrawer.DrawerBuilder;

class CustomDrawerBuilder extends DrawerBuilder {
    public void closeDrawerWithoutAnimation() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mDrawerGravity, false);
        }
    }
}
