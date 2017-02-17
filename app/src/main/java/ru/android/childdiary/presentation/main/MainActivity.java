package ru.android.childdiary.presentation.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import ru.android.childdiary.presentation.core.BaseActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.utils.ParcelableUtils;

// TODO: доработать
public class MainActivity extends BaseActivity<MainPresenter> implements MainView, Drawer.OnDrawerItemClickListener, AccountHeader.OnAccountHeaderListener {
    private static final int PROFILE_SETTING_ADD = -1;

    @InjectPresenter
    MainPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AccountHeader accountHeader;
    private Drawer drawer;

    private Child activeChild;
    private List<Child> childList;
    private List<IProfile> profiles;

    public static Intent getIntent(Context context, Child child, List<Child> childList) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_CHILD, child);
        ParcelableUtils.wrap(intent, ExtraConstants.EXTRA_CHILD_LIST, childList);
        return intent;
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activeChild = getIntent().getParcelableExtra(ExtraConstants.EXTRA_CHILD);
        childList = ParcelableUtils.unwrap(getIntent(), ExtraConstants.EXTRA_CHILD_LIST, Child.class);
        profiles = Stream.of(childList).map(child -> mapToProfile(child)).collect(Collectors.toList());

        profiles.add(new ProfileSettingDrawerItem()
                .withName(getString(R.string.add_child))
                .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add)
                        .actionBar()
                        .paddingDp(5)
                        .colorRes(R.color.material_drawer_dark_primary_text))
                .withIdentifier(PROFILE_SETTING_ADD));

        if (activeChild == null) {
            activeChild = childList.size() > 0 ? childList.get(0) : null;
        }

        setTheme(getPreferredTheme(activeChild));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(toolbar);

        buildHeader(false, savedInstanceState);
        buildDrawer(savedInstanceState);

        updateUi();
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        showToast(((PrimaryDrawerItem) drawerItem).getName().getText());
        return false;
    }

    @Override
    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
        if (profile.getIdentifier() == PROFILE_SETTING_ADD) {
            showToast("Add profile");
        } else {
            long id = profile.getIdentifier();
            Child activeChild = Stream.of(childList).filter(child -> child.getId() == id).findFirst().get();
            toggleChild(activeChild);
        }

        return false;
    }

    private void toggleChild(Child child) {
        if (activeChild != null && child != null && activeChild.getSex() != child.getSex()) {
            finish();
            navigateToMain(child, childList);
            return;
        }

        activeChild = child;
        updateUi();
    }

    private void updateUi() {
        getSupportActionBar().setTitle(activeChild == null ? getString(R.string.app_name) : activeChild.getName());
    }

    private IProfile mapToProfile(Child child) {
        return new ProfileDrawerItem()
                .withName(child.getName())
                .withEmail("age")
                .withIcon(mapToIcon(child))
                .withIdentifier(child.getId());
    }

    private Drawable mapToIcon(Child child) {
        return child.getSex() == Sex.MALE
                ? getResources().getDrawable(R.color.colorPrimaryDarkBoy)
                : getResources().getDrawable(R.color.colorPrimaryGirl);
    }

    private void buildHeader(boolean compact, Bundle savedInstanceState) {
        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(getPreferedAccountHeaderColor(activeChild))
                .withCompactStyle(compact)
                .withOnAccountHeaderListener(this)
                .withSavedInstance(savedInstanceState)
                .addProfiles(profiles.toArray(new IProfile[profiles.size()]))
                .build();
        if (activeChild != null) {
            accountHeader.setActiveProfile(activeChild.getId());
        }
    }

    private void buildDrawer(Bundle savedInstanceState) {
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
                .withSavedInstance(savedInstanceState)
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
        switch (item.getItemId()) {
            case R.id.menu_1:
                IconicsDrawable icon = new IconicsDrawable(this, GoogleMaterial.Icon.gmd_android)
                        .backgroundColorRes(R.color.accent)
                        .sizeDp(48)
                        .paddingDp(4);
                profiles.get(0).withIcon(icon);
                accountHeader.updateProfile(profiles.get(0));
                return true;
            case R.id.menu_2:
                drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                return true;
            case R.id.menu_3:
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                return true;
            case R.id.menu_4:
                buildHeader(true, null);
                drawer.setHeader(accountHeader.getView());
                accountHeader.setDrawer(drawer);
                return true;
            case R.id.menu_5:
                buildHeader(false, null);
                drawer.setHeader(accountHeader.getView());
                accountHeader.setDrawer(drawer);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = drawer.saveInstanceState(outState);
        outState = accountHeader.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
