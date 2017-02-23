package ru.android.childdiary.presentation.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.profile.edit.ProfileEditActivity;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainView,
        Drawer.OnDrawerItemClickListener, AccountHeader.OnAccountHeaderListener {
    private static final int PROFILE_SETTING_ADD = 1;
    private static final int PROFILE_SETTINGS_EDIT = 2;
    private static final int PROFILE_SETTINGS_DELETE = 3;
    private static final int PROFILE_SETTINGS_USER = 10;

    @InjectPresenter
    MainPresenter presenter;

    private AccountHeader accountHeader;
    private Drawer drawer;

    public static Intent getIntent(Context context, @Nullable Sex sex) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_SEX, sex);
        return intent;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setNavigationIcon(R.drawable.toolbar_menu);
        toolbar.setOverflowIcon(ThemeUtils.getDrawable(this, R.drawable.toolbar_overflow));
    }

    @Override
    public void childListLoaded(@Nullable Child activeChild, List<Child> childList) {
        List<IProfile> profiles = Stream.of(childList).map(this::mapToProfile).collect(Collectors.toList());
        profiles.add(new ProfileSettingDrawerItem()
                .withName(getString(R.string.add_child))
                .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add)
                        .actionBar()
                        .paddingDp(5)
                        .colorRes(R.color.material_drawer_dark_primary_text))
                .withIdentifier(PROFILE_SETTING_ADD));
        profiles.add(new ProfileSettingDrawerItem()
                .withName(getString(R.string.edit_child))
                .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_edit)
                        .actionBar()
                        .paddingDp(5)
                        .colorRes(R.color.material_drawer_dark_primary_text))
                .withIdentifier(PROFILE_SETTINGS_EDIT));
        profiles.add(new ProfileSettingDrawerItem()
                .withName(getString(R.string.remove_child))
                .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_remove)
                        .actionBar()
                        .paddingDp(5)
                        .colorRes(R.color.material_drawer_dark_primary_text))
                .withIdentifier(PROFILE_SETTINGS_DELETE));

        buildUi(profiles);
        setActive(activeChild);
    }

    @Override
    public void addChild() {
        navigateToProfileEdit(null);
    }

    @Override
    public void editChild(@Nullable Child child) {
        navigateToProfileEdit(child);
    }

    @Override
    public void setActive(@Nullable Child child) {
        setSex(child);
        if (child == null) {
            getSupportActionBar().setTitle(R.string.app_name);
        } else {
            accountHeader.setActiveProfile(mapToProfileId(child));
            getSupportActionBar().setTitle(child.getName());
        }
    }

    @Override
    protected void themeChangedCustom() {
        if (accountHeader != null) {
            accountHeader.setBackground(ThemeUtils.getHeaderDrawable(this, sex));
        }
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        // TODO: navigation
        showToast(((PrimaryDrawerItem) drawerItem).getName().getText());
        return false;
    }

    @Override
    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
        long itemId = profile.getIdentifier();
        if (itemId > PROFILE_SETTINGS_USER) {
            presenter.toggleChild(mapToChildId(profile));
        } else if (itemId == PROFILE_SETTING_ADD) {
            presenter.addChild();
        } else if (itemId == PROFILE_SETTINGS_EDIT) {
            presenter.editChild();
        } else if (itemId == PROFILE_SETTINGS_DELETE) {
            // TODO: confirmation alert dialog
            presenter.deleteChild();
        }

        return false;
    }

    private IProfile mapToProfile(@NonNull Child child) {
        // TODO: calculate age and print as formatted string
        return new ProfileDrawerItem()
                .withName(child.getName())
                .withNameShown(true)
                .withEmail("age")
                .withIdentifier(mapToProfileId(child))
                .withIcon(ThemeUtils.getChildIcon(this, child));
    }

    private long mapToProfileId(@NonNull Child child) {
        return child.getId() + PROFILE_SETTINGS_USER;
    }

    private long mapToChildId(IProfile profile) {
        return profile.getIdentifier() - PROFILE_SETTINGS_USER;
    }

    private void buildUi(List<IProfile> profiles) {
        buildHeader(profiles);
        buildDrawer();
    }

    private void buildHeader(List<IProfile> profiles) {
        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withOnAccountHeaderListener(this)
                .withHeaderBackground(ThemeUtils.getHeaderDrawable(this, sex))
                .addProfiles(profiles.toArray(new IProfile[profiles.size()]))
                .build();
    }

    private void buildDrawer() {
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_calendar)
                                .withIcon(FontAwesome.Icon.faw_cart_plus)
                                .withOnDrawerItemClickListener(this),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_development_diary)
                                .withIcon(FontAwesome.Icon.faw_database)
                                .withOnDrawerItemClickListener(this),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_exercises)
                                .withIcon(FontAwesome.Icon.faw_github)
                                .withOnDrawerItemClickListener(this),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_medical_data)
                                .withIcon(FontAwesome.Icon.faw_amazon)
                                .withOnDrawerItemClickListener(this),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_settings)
                                .withIcon(FontAwesome.Icon.faw_cog)
                                .withOnDrawerItemClickListener(this),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_help)
                                .withIcon(FontAwesome.Icon.faw_question)
                                .withOnDrawerItemClickListener(this)
                )
                .build();
        setupToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: menu
        switch (item.getItemId()) {
            case R.id.menu_1:
                return true;
            case R.id.menu_2:
                return true;
            case R.id.menu_3:
                return true;
            case R.id.menu_4:
                return true;
            case R.id.menu_5:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void navigateToProfileEdit(@Nullable Child child) {
        Intent intent = ProfileEditActivity.getIntent(this, child);
        startActivity(intent);
    }
}
