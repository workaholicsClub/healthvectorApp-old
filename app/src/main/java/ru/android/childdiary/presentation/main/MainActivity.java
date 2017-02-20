package ru.android.childdiary.presentation.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
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

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.profile.edit.ProfileEditActivity;
import ru.android.childdiary.utils.UiUtils;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainView,
        Drawer.OnDrawerItemClickListener, AccountHeader.OnAccountHeaderListener {
    private static final int REQUEST_PROFILE_EDIT = 1;

    private static final int PROFILE_SETTING_ADD = 1;
    private static final int PROFILE_SETTINGS_EDIT = 2;
    private static final int PROFILE_SETTINGS_USER = 10;

    @InjectPresenter
    MainPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AccountHeader accountHeader;
    private Drawer drawer;

    public static Intent getIntent(Context context, Child lastActiveChild) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_CHILD, lastActiveChild);
        return intent;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Child lastActiveChild = getIntent().getParcelableExtra(ExtraConstants.EXTRA_CHILD);
        setTheme(UiUtils.getPreferredTheme(lastActiveChild));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(toolbar);
    }

    @Override
    public void childListLoaded(@Nullable Child activeChild, List<Child> childList) {
        Drawable headerColor = UiUtils.getPreferredAccountHeaderColor(this, activeChild);

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

        buildUi(headerColor, profiles);
    }

    @Override
    public void addChild() {
        navigateToProfileEdit(null);
    }

    @Override
    public void editChild(@NonNull Child child) {
        navigateToProfileEdit(child);
    }

    private void childAdded(Child child) {
        // TODO через презентер?
        accountHeader.addProfile(mapToProfile(child), 0);
        setActive(child);
    }

    private void childUpdated(Child child) {
        // TODO через презентер?
        accountHeader.updateProfile(mapToProfile(child));
    }

    @Override
    public void setActive(@Nullable Child child) {
        // TODO: switch theme and color if needed
        Drawable headerColor = UiUtils.getPreferredAccountHeaderColor(this, child);
        accountHeader.setBackground(headerColor);
        if (child == null) {
            getSupportActionBar().setTitle(R.string.app_name);
        } else {
            accountHeader.setActiveProfile(mapToProfileId(child));
            getSupportActionBar().setTitle(child.getName());
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
                .withIcon(mapToProfileIcon(child));
    }

    private long mapToProfileId(@NonNull Child child) {
        return child.getId() + PROFILE_SETTINGS_USER;
    }

    private long mapToChildId(IProfile profile) {
        return profile.getIdentifier() - PROFILE_SETTINGS_USER;
    }

    private Drawable mapToProfileIcon(@NonNull Child child) {
        // TODO: default icon for girl and boy
        if (child.getImageFileName() == null) {
            if (child.getSex() == Sex.MALE) {
                return getResources().getDrawable(R.color.colorPrimaryDarkBoy);
            } else {
                return getResources().getDrawable(R.color.colorPrimaryDarkGirl);
            }
        }
        return Drawable.createFromPath(child.getImageFileName());
    }

    private void buildUi(Drawable headerColor, List<IProfile> profiles) {
        buildHeader(headerColor, profiles);
        buildDrawer();
    }

    private void buildHeader(Drawable headerColor, List<IProfile> profiles) {
        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withOnAccountHeaderListener(this)
                .withHeaderBackground(headerColor)
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

    protected final void navigateToProfileEdit(@Nullable Child child) {
        Intent intent = ProfileEditActivity.getIntent(this, child);
        startActivityForResult(intent, REQUEST_PROFILE_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PROFILE_EDIT) {
            if (resultCode == ProfileEditActivity.RESULT_ADDED) {
                Child child = data.getParcelableExtra(ExtraConstants.EXTRA_CHILD);
                childAdded(child);
            } else if (resultCode == ProfileEditActivity.RESULT_UPDATED) {
                Child child = data.getParcelableExtra(ExtraConstants.EXTRA_CHILD);
                childUpdated(child);
            }
        }
    }
}
