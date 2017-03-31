package ru.android.childdiary.presentation.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.calendar.CalendarFragment;
import ru.android.childdiary.presentation.calendar.FabController;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.development.DevelopmentDiaryFragment;
import ru.android.childdiary.presentation.exercises.ExercisesFragment;
import ru.android.childdiary.presentation.help.HelpFragment;
import ru.android.childdiary.presentation.main.drawer.AccountHeaderActionAdapter;
import ru.android.childdiary.presentation.main.drawer.CloseMenuButtonClickListener;
import ru.android.childdiary.presentation.main.drawer.CustomAccountHeaderBuilder;
import ru.android.childdiary.presentation.main.drawer.CustomDrawerBuilder;
import ru.android.childdiary.presentation.main.drawer.CustomPrimaryDrawerItem;
import ru.android.childdiary.presentation.medical.MedicalDataFragment;
import ru.android.childdiary.presentation.profile.edit.ProfileEditActivity;
import ru.android.childdiary.presentation.profile.review.ProfileReviewActivity;
import ru.android.childdiary.presentation.settings.SettingsFragment;
import ru.android.childdiary.utils.StringUtils;
import ru.android.childdiary.utils.TimeUtils;
import ru.android.childdiary.utils.ui.ResourcesUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class MainActivity extends BaseMvpActivity implements MainView,
        Drawer.OnDrawerItemClickListener,
        AccountHeader.OnAccountHeaderProfileImageListener,
        AdapterView.OnItemClickListener,
        PopupWindow.OnDismissListener,
        CloseMenuButtonClickListener,
        View.OnClickListener,
        FabController {
    private static final int PROFILE_SETTINGS_EDIT = 1;
    private static final int PROFILE_SETTINGS_ADD = 2;
    private static final int PROFILE_SETTINGS_DELETE = 3;
    private static final int PROFILE_SETTINGS_USER = 10;

    private static final int FRAGMENT_CONTAINER_ID = R.id.mainContent;

    private final PrimaryDrawerItem[] drawerItems = new PrimaryDrawerItem[]{
            new CustomPrimaryDrawerItem()
                    .withTag(AppPartition.CALENDAR)
                    .withName(R.string.drawer_item_calendar)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withTag(AppPartition.DEVELOPMENT_DIARY)
                    .withName(R.string.drawer_item_development_diary)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withTag(AppPartition.EXERCISES)
                    .withName(R.string.drawer_item_exercises)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withTag(AppPartition.MEDICAL_DATA)
                    .withName(R.string.drawer_item_medical_data)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withTag(AppPartition.SETTINGS)
                    .withName(R.string.drawer_item_settings)
                    .withOnDrawerItemClickListener(this),
            new CustomPrimaryDrawerItem()
                    .withTag(AppPartition.HELP)
                    .withName(R.string.drawer_item_help)
                    .withOnDrawerItemClickListener(this)
    };

    @InjectPresenter
    MainPresenter presenter;

    private AccountHeader accountHeader;
    private Drawer drawer;
    private DrawerBuilder drawerBuilder;
    private ImageView switcherImage;
    private ListPopupWindow popupWindow;
    private AppPartition selectedPartition;

    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    private static IProfile mapToProfile(Context context, @NonNull Child child) {
        return new ProfileDrawerItem()
                .withName(child.getName())
                .withEmail(TimeUtils.age(context, child))
                .withNameShown(true)
                .withTag(child)
                .withIdentifier(mapToProfileId(child))
                .withIcon(ResourcesUtils.getChildIcon(context, child, true));
    }

    private static long mapToProfileId(@NonNull Child child) {
        return child.getId() + PROFILE_SETTINGS_USER;
    }

    private static long mapToChildId(long profileId) {
        return profileId - PROFILE_SETTINGS_USER;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildUi();
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_navigation_drawer);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        if (accountHeader != null) {
            accountHeader.setBackground(ThemeUtils.getColorPrimaryDrawable(this, getSex()));
        }
        updateDrawerItems(true);
    }

    private void updateDrawerItems(boolean notify) {
        int[] icons = ResourcesUtils.getNavigationDrawerItemResources(getSex());
        for (int i = 0; i < Math.min(icons.length, drawerItems.length); ++i) {
            drawerItems[i].withIcon(icons[i]);
        }
        if (notify && drawer != null) {
            drawer.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void showChildList(@NonNull List<Child> childList) {
        logger.debug("showChildList: " + StringUtils.childList(childList));

        List<IProfile> profiles = new ArrayList<>();
        if (!childList.isEmpty()) {
            profiles.add(new ProfileSettingDrawerItem()
                    .withName(getString(R.string.edit))
                    .withIdentifier(PROFILE_SETTINGS_EDIT));
        }
        profiles.add(new ProfileSettingDrawerItem()
                .withName(getString(R.string.add_profile))
                .withIdentifier(PROFILE_SETTINGS_ADD));
        if (!childList.isEmpty()) {
            profiles.add(new ProfileSettingDrawerItem()
                    .withName(getString(R.string.remove_child))
                    .withIdentifier(PROFILE_SETTINGS_DELETE));
        }

        profiles.addAll(Stream.of(childList)
                .map(child -> mapToProfile(this, child))
                .collect(Collectors.toList()));

        closeDrawerWithoutAnimation();
        if (accountHeader != null) {
            accountHeader.setProfiles(profiles);
        }
    }

    @Override
    public void showChild(@NonNull Child child) {
        logger.debug("showChild: " + child);
        changeThemeIfNeeded(child);
        if (child.getId() == null) {
            hideToolbarLogo();
            setupToolbarTitle(R.string.app_name);
        } else {
            setupToolbarLogo(ResourcesUtils.getChildIcon(this, child, true));
            setupToolbarTitle(child.getName());
            if (accountHeader != null) {
                accountHeader.setActiveProfile(mapToProfileId(child));
            }
        }
        if (selectedPartition == null) {
            drawer.setSelectionAtPosition(1, true);
        }
    }

    @Override
    public void navigateToProfileAdd() {
        Intent intent = ProfileEditActivity.getIntent(this, null);
        startActivity(intent);
    }

    @Override
    public void navigateToProfileEdit(@NonNull Child child) {
        Intent intent = ProfileEditActivity.getIntent(this, child);
        startActivity(intent);
    }

    @Override
    public void navigateToProfileReview() {
        Intent intent = ProfileReviewActivity.getIntent(this);
        startActivity(intent);
    }

    @Override
    public void showDeleteChildConfirmation(@NonNull Child child) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setTitle(getString(R.string.remove_child_confirmation_title, child.getName()))
                .setMessage(R.string.remove_child_confirmation_text)
                .setPositiveButton(R.string.Yes,
                        (DialogInterface dialog, int which) -> presenter.deleteChild(child))
                .setNegativeButton(R.string.Cancel, null)
                .show();
    }

    @Override
    public void navigateToCalendar(@NonNull Child child) {
        openAppPartition(AppPartition.CALENDAR, child, new CalendarFragment());
    }

    @Override
    public void navigateToDevelopmentDiary(@NonNull Child child) {
        openAppPartition(AppPartition.DEVELOPMENT_DIARY, child, new DevelopmentDiaryFragment());
    }

    @Override
    public void navigateToExercises(@NonNull Child child) {
        openAppPartition(AppPartition.EXERCISES, child, new ExercisesFragment());
    }

    @Override
    public void navigateToMedicalData(@NonNull Child child) {
        openAppPartition(AppPartition.MEDICAL_DATA, child, new MedicalDataFragment());
    }

    @Override
    public void navigateToSettings(@NonNull Child child) {
        openAppPartition(AppPartition.SETTINGS, child, new SettingsFragment());
    }

    @Override
    public void navigateToHelp(@NonNull Child child) {
        openAppPartition(AppPartition.HELP, child, new HelpFragment());
    }

    private void openAppPartition(AppPartition appPartition, @NonNull Child child, Fragment fragment) {
        selectedPartition = appPartition;

        Bundle arguments = new Bundle();
        arguments.putSerializable(ExtraConstants.EXTRA_CHILD, child);
        fragment.setArguments(arguments);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_UNSET);
        ft.replace(R.id.mainContent, fragment);
        ft.commit();

        invalidateOptionsMenu();

        closeDrawer();
    }

    @Override
    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
        presenter.reviewChild();
        return false;
    }

    @Override
    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == switcherImage && accountHeader != null) {
            dismissPopupWindow();
            animateSwitcherIn();
            List<IProfile> profiles = new ArrayList<>(accountHeader.getProfiles());
            profiles.remove(accountHeader.getActiveProfile());
            ListAdapter adapter = new AccountHeaderActionAdapter(this, profiles, this);
            View anchor = v;
            int width = profiles.size() == 1
                    ? getResources().getDimensionPixelSize(R.dimen.account_header_action_item_width_wide)
                    : getResources().getDimensionPixelSize(R.dimen.account_header_action_item_width_narrow);
            int gravity = Gravity.END;

            popupWindow = new ListPopupWindow(this, null, R.attr.actionOverflowMenuStyle, R.style.OverflowMenu);
            popupWindow.setWidth(width);
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setAdapter(adapter);
            popupWindow.setAnchorView(anchor);
            popupWindow.setDropDownGravity(gravity);
            popupWindow.setOnItemClickListener(this);
            popupWindow.setOnDismissListener(this);
            popupWindow.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismissPopupWindow();
        IProfile profile = ((AccountHeaderActionAdapter) parent.getAdapter()).getItems().get(position);
        if (profile instanceof ProfileDrawerItem) {
            Child child = (Child) ((ProfileDrawerItem) profile).getTag();
            presenter.switchChild(child);
        } else if (profile instanceof ProfileSettingDrawerItem) {
            if (id == PROFILE_SETTINGS_EDIT) {
                presenter.editChild();
            } else if (id == PROFILE_SETTINGS_ADD) {
                presenter.addChild();
            } else if (id == PROFILE_SETTINGS_DELETE) {
                presenter.deleteChild();
            }
        }
    }

    @Override
    public void closeMenu() {
        dismissPopupWindow();
    }

    @Override
    public void onDismiss() {
        animateSwitcherOut();
        dismissPopupWindow();
    }

    private boolean dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return true;
        }
        popupWindow = null;
        return false;
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        AppPartition tag = (AppPartition) drawerItem.getTag();
        if (selectedPartition == tag) {
            return false;
        }
        switch (tag) {
            case CALENDAR:
                presenter.openCalendar();
                return true;
            case DEVELOPMENT_DIARY:
                //presenter.openDevelopmentDiary();
                return true;
            case EXERCISES:
                //presenter.openExercises();
                return true;
            case MEDICAL_DATA:
                //presenter.openMedicalData();
                return true;
            case SETTINGS:
                //presenter.openSettings();
                return true;
            case HELP:
                //presenter.openHelp();
                return true;
            default:
                return false;
        }
    }

    private void buildUi() {
        buildHeader();
        buildDrawer();

        setupToolbar(getToolbar());

        switcherImage = ButterKnife.findById(accountHeader.getView(), R.id.material_drawer_account_header_text_switcher);
        switcherImage.setImageResource(R.drawable.arrow_down_white);
        switcherImage.setOnClickListener(this);
    }

    private void animateSwitcherIn() {
        switcherImage.clearAnimation();
        ViewCompat.animate(switcherImage).rotation(180).start();
    }

    private void animateSwitcherOut() {
        switcherImage.clearAnimation();
        ViewCompat.animate(switcherImage).rotation(0).start();
    }

    private void buildHeader() {
        accountHeader = new CustomAccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withOnAccountHeaderProfileImageListener(this)
                .withAccountHeader(R.layout.account_header)
                .withHeightRes(R.dimen.account_header_height)
                .withHeaderBackground(ThemeUtils.getColorPrimaryDrawable(this, getSex()))
                .build();
    }

    private void buildDrawer() {
        updateDrawerItems(false);
        drawerBuilder = new CustomDrawerBuilder()
                .withActivity(this)
                .withToolbar(getToolbar())
                .withAccountHeader(accountHeader)
                .addDrawerItems(drawerItems);
        drawer = drawerBuilder.build();
    }

    private void closeDrawerWithoutAnimation() {
        if (drawerBuilder != null && drawer != null && drawer.isDrawerOpen()) {
            ((CustomDrawerBuilder) drawerBuilder).closeDrawerWithoutAnimation();
        }
    }

    private boolean closeDrawer() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        boolean processed = dismissPopupWindow();
        if (processed) {
            return;
        }

        processed = closeDrawer();
        if (processed) {
            return;
        }

        processed = hideBar();
        if (processed) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (selectedPartition == AppPartition.CALENDAR) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (selectedPartition == AppPartition.CALENDAR) {
            switch (item.getItemId()) {
                case R.id.menu_filter:
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showFab() {
        FabController fabController = findFabController();
        if (fabController != null) {
            fabController.showFab();
        }
    }

    @Override
    public boolean hideBar() {
        FabController fabController = findFabController();
        if (fabController != null) {
            return fabController.hideBar();
        }
        return false;
    }

    @Override
    public void hideFabBar() {
        FabController fabController = findFabController();
        if (fabController != null) {
            fabController.hideFabBar();
        }
    }

    private FabController findFabController() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(FRAGMENT_CONTAINER_ID);
        return fragment != null && fragment instanceof FabController ? (FabController) fragment : null;
    }
}
