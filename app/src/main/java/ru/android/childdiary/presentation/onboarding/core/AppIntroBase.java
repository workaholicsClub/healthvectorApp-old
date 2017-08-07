package ru.android.childdiary.presentation.onboarding.core;

import android.animation.ArgbEvaluator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.List;
import java.util.Vector;

import ru.android.childdiary.R;

public abstract class AppIntroBase extends AppCompatActivity implements
        AppIntroViewPager.OnNextPageRequestedListener {

    public static final int DEFAULT_COLOR = 1;

    private static final int DEFAULT_SCROLL_DURATION_FACTOR = 1;
    private static final int PERMISSIONS_REQUEST_ALL_PERMISSIONS = 1;
    private static final String INSTANCE_DATA_IMMERSIVE_MODE_ENABLED =
            "com.github.paolorotolo.appintro_immersive_mode_enabled";
    private static final String INSTANCE_DATA_IMMERSIVE_MODE_STICKY =
            "com.github.paolorotolo.appintro_immersive_mode_sticky";
    private static final String INSTANCE_DATA_COLOR_TRANSITIONS_ENABLED =
            "com.github.paolorotolo.appintro_color_transitions_enabled";
    protected final List<Fragment> fragments = new Vector<>();
    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    protected PagerAdapter mPagerAdapter;
    protected AppIntroViewPager pager;
    protected IndicatorController mController;
    protected int slidesNumber;
    protected int vibrateIntensity = 20;
    protected int selectedIndicatorColor = DEFAULT_COLOR;
    protected int unselectedIndicatorColor = DEFAULT_COLOR;
    protected View nextButton;
    protected View doneButton;
    protected View skipButton;
    protected int savedCurrentItem;
    protected boolean baseProgressButtonEnabled = true;
    protected boolean progressButtonEnabled = true;
    protected boolean skipButtonEnabled = true;
    protected boolean showBackButtonWithDone = false;
    private GestureDetectorCompat gestureDetector;
    private boolean isGoBackLockEnabled = false;
    private boolean isImmersiveModeEnabled = false;
    private boolean isImmersiveModeSticky = false;
    private boolean areColorTransitionsEnabled = false;
    private int currentlySelectedItem = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_intro);

        gestureDetector = new GestureDetectorCompat(this, new WindowGestureListener());

        nextButton = findViewById(R.id.next);
        doneButton = findViewById(R.id.done);
        skipButton = findViewById(R.id.skip);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        pager = (AppIntroViewPager) findViewById(R.id.view_pager);

        if (doneButton != null) {
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(@NonNull View v) {
                    changeSlide(true);
                }
            });
        }

        if (skipButton != null) {
            skipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(@NonNull View v) {
                    onSkipPressed(mPagerAdapter.getItem(pager.getCurrentItem()));
                }
            });
        }

        if (nextButton != null) {
            nextButton.setOnClickListener(new NextButtonOnClickListener());
        }

        pager.setAdapter(this.mPagerAdapter);
        pager.addOnPageChangeListener(new PagerOnPageChangeListener());
        pager.setOnNextPageRequestedListener(this);

        setScrollDurationFactor(DEFAULT_SCROLL_DURATION_FACTOR);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Call deprecated init method only if no fragments have been added through onCreate() or onStart()
        if (fragments.size() == 0) {
            init(null);
        }

        pager.setCurrentItem(savedCurrentItem);
        pager.post(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = mPagerAdapter.getItem(pager.getCurrentItem());
                // Fragment is null when no slides are passed to AppIntro
                if (fragment == null) {
                    finish();
                }
            }
        });

        slidesNumber = fragments.size();

        setProgressButtonEnabled(progressButtonEnabled);
        initController();
    }

    @Override
    public void onBackPressed() {
        // Do nothing if go back lock is enabled or slide has custom policy.
        if (!isGoBackLockEnabled) {
            if (!pager.isFirstSlide(fragments.size())) {
                pager.goToPreviousSlide();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && isImmersiveModeEnabled) {
            setImmersiveMode(true, isImmersiveModeSticky);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isImmersiveModeEnabled) {
            gestureDetector.onTouchEvent(event);
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("baseProgressButtonEnabled", baseProgressButtonEnabled);
        outState.putBoolean("progressButtonEnabled", progressButtonEnabled);
        outState.putBoolean("nextEnabled", pager.isPagingEnabled());
        outState.putBoolean("nextPagingEnabled", pager.isNextPagingEnabled());
        outState.putBoolean("skipButtonEnabled", skipButtonEnabled);
        outState.putInt("lockPage", pager.getLockPage());
        outState.putInt("currentItem", pager.getCurrentItem());

        outState.putBoolean(INSTANCE_DATA_IMMERSIVE_MODE_ENABLED, isImmersiveModeEnabled);
        outState.putBoolean(INSTANCE_DATA_IMMERSIVE_MODE_STICKY, isImmersiveModeSticky);
        outState.putBoolean(INSTANCE_DATA_COLOR_TRANSITIONS_ENABLED, areColorTransitionsEnabled);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        this.baseProgressButtonEnabled = savedInstanceState.getBoolean("baseProgressButtonEnabled");
        this.progressButtonEnabled = savedInstanceState.getBoolean("progressButtonEnabled");
        this.skipButtonEnabled = savedInstanceState.getBoolean("skipButtonEnabled");
        this.savedCurrentItem = savedInstanceState.getInt("currentItem");
        pager.setPagingEnabled(savedInstanceState.getBoolean("nextEnabled"));
        pager.setNextPagingEnabled(savedInstanceState.getBoolean("nextPagingEnabled"));
        pager.setLockPage(savedInstanceState.getInt("lockPage"));

        isImmersiveModeEnabled = savedInstanceState.getBoolean(INSTANCE_DATA_IMMERSIVE_MODE_ENABLED);
        isImmersiveModeSticky = savedInstanceState.getBoolean(INSTANCE_DATA_IMMERSIVE_MODE_STICKY);
        areColorTransitionsEnabled = savedInstanceState.getBoolean(
                INSTANCE_DATA_COLOR_TRANSITIONS_ENABLED);
    }

    @Override
    public boolean onCanRequestNextPage() {
        return true;
    }

    @Override
    public void onIllegallyRequestedNextPage() {
    }

    private void initController() {
        if (mController == null)
            mController = new DefaultIndicatorController();

        FrameLayout indicatorContainer = (FrameLayout) findViewById(R.id.indicator_container);
        indicatorContainer.addView(mController.newInstance(this));

        mController.initialize(slidesNumber);
        if (selectedIndicatorColor != DEFAULT_COLOR)
            mController.setSelectedIndicatorColor(selectedIndicatorColor);
        if (unselectedIndicatorColor != DEFAULT_COLOR)
            mController.setUnselectedIndicatorColor(unselectedIndicatorColor);

        mController.selectPosition(currentlySelectedItem);

        indicatorContainer.setVisibility(View.VISIBLE);
    }

    protected void onPageSelected(int position) {
        // Empty method
    }

    /**
     * Setting this to display or hide the Skip button. This is a static setting and
     * button state is maintained across slides until explicitly changed.
     *
     * @param showButton Set true to display. false to hide.
     */
    public void showSkipButton(boolean showButton) {
        this.skipButtonEnabled = showButton;
        setButtonState(skipButton, showButton);
    }

    public boolean isSkipButtonEnabled() {
        return skipButtonEnabled;
    }

    /**
     * Called when the user clicked the skip button
     *
     * @param currentFragment Instance of the currently displayed fragment
     */
    public void onSkipPressed(Fragment currentFragment) {
        onSkipPressed();
    }

    protected void setScrollDurationFactor(int factor) {
        pager.setScrollDurationFactor(factor);
    }

    /**
     * Helper method for displaying a view
     *
     * @param button View which visibility should be changed
     * @param show   Whether the view should be visible or not
     */
    protected void setButtonState(@Nullable View button, boolean show) {
        if (button == null) {
            return;
        }

        if (show) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Returns the used ViewPager instance
     *
     * @return Instance of the used ViewPager
     */
    public AppIntroViewPager getPager() {
        return pager;
    }

    /**
     * Returns all current slides.
     *
     * @return List of the current slides
     */
    @NonNull
    public List<Fragment> getSlides() {
        return mPagerAdapter.getFragments();
    }

    /**
     * Adds a new slide
     *
     * @param fragment Instance of Fragment which should be added as slide
     */
    public void addSlide(@NonNull Fragment fragment) {
        fragments.add(fragment);
        pager.setOffscreenPageLimit(fragments.size() - 1);
        mPagerAdapter.notifyDataSetChanged();
    }

    public boolean isProgressButtonEnabled() {
        return progressButtonEnabled;
    }

    /**
     * Setting to to display or hide the Next or Done button. This is a static setting and
     * button state is maintained across slides until explicitly changed.
     *
     * @param progressButtonEnabled Set true to display. False to hide.
     */
    public void setProgressButtonEnabled(boolean progressButtonEnabled) {

        this.progressButtonEnabled = progressButtonEnabled;
        if (progressButtonEnabled) {

            if (pager.getCurrentItem() == slidesNumber - 1) {
                setButtonState(nextButton, false);
                setButtonState(doneButton, true);
                setButtonState(skipButton, false);

            } else {
                setButtonState(nextButton, true);
                setButtonState(doneButton, false);
                setButtonState(skipButton, skipButtonEnabled);
            }
        } else {
            setButtonState(nextButton, false);
            setButtonState(doneButton, false);
            setButtonState(skipButton, false);

        }
    }

    /**
     * @param savedInstanceState Null
     * @deprecated It is strongly recommended to use {@link #onCreate(Bundle)} instead. Be sure calling super.onCreate() in your method.
     * Please note that this method WILL NOT be called when the activity gets recreated i.e. the fragment instances get restored.
     * The method will only be called when there are no fragments registered to the intro at all.
     */
    public void init(@Nullable Bundle savedInstanceState) {

    }

    /**
     * Called when the user clicked the next button which triggered a fragment change
     *
     * @deprecated Obsolete, use {@link #onSlideChanged(Fragment, Fragment)} instead
     */
    public void onNextPressed() {

    }

    /**
     * Called when the user clicked the done button
     *
     * @deprecated Override {@link #onDonePressed(Fragment)} instead
     */
    public void onDonePressed() {

    }

    /**
     * Called when the user clicked the skip button
     *
     * @deprecated Override {@link #onSkipPressed(Fragment)} instead
     */
    public void onSkipPressed() {

    }

    /**
     * Called when the selected fragment changed
     *
     * @deprecated Override {@link #onSlideChanged(Fragment, Fragment)} instead
     */
    public void onSlideChanged() {

    }

    /**
     * Called when the user clicked the done button
     *
     * @param currentFragment Instance of the currently displayed fragment
     */
    public void onDonePressed(Fragment currentFragment) {
        onDonePressed();
    }

    /**
     * Called when the selected fragment changed. This will be called automatically if the into starts or is finished via the done button.
     *
     * @param oldFragment Instance of the fragment which was displayed before. This might be null if the the intro has just started.
     * @param newFragment Instance of the fragment which is displayed now. This might be null if the intro has finished
     */
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        onSlideChanged();
    }

    @Override
    public boolean onKeyDown(int code, KeyEvent event) {
        if (code == KeyEvent.KEYCODE_ENTER || code == KeyEvent.KEYCODE_BUTTON_A ||
                code == KeyEvent.KEYCODE_DPAD_CENTER) {
            ViewPager vp = (ViewPager) this.findViewById(R.id.view_pager);
            if (vp.getCurrentItem() == vp.getAdapter().getCount() - 1) {
                onDonePressed(fragments.get(vp.getCurrentItem()));
            } else {
                vp.setCurrentItem(vp.getCurrentItem() + 1);
            }

            return false;
        }

        return super.onKeyDown(code, event);
    }

    /**
     * Allows the user to set the nav bar color of their app intro
     *
     * @param Color string form of color in 3 or 6 digit hex form (#ffffff)
     */
    public void setNavBarColor(String Color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(android.graphics.Color.parseColor(Color));
        }
    }

    /**
     * Allows the user to set the nav bar color of their app intro
     *
     * @param color int form of color. pass your color resource to here (R.color.your_color)
     */
    public void setNavBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, color));
        }
    }

    /**
     * Allows for setting statusbar visibility (true by default)
     *
     * @param isVisible put true to show status bar, and false to hide it
     */
    public void showStatusBar(boolean isVisible) {
        if (!isVisible) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * sets wizard mode
     *
     * @param show on/off
     */
    public void setBackButtonVisibilityWithDone(boolean show) {
        this.showBackButtonWithDone = show;
    }

    /**
     * sets vibration intensity
     *
     * @param intensity desired intensity
     */
    public void setVibrateIntensity(int intensity) {
        this.vibrateIntensity = intensity;
    }

    /**
     * Set a progress indicator instead of dots. This is recommended for a large amount of slides. In this case there
     * could not be enough space to display all dots on smaller device screens.
     */
/*    public void setProgressIndicator() {
        mController = new ProgressIndicatorController();
    }
*/
    public void setCustomIndicator(@NonNull IndicatorController controller) {
        mController = controller;
    }

    /**
     * Sets whether color transitions between slides should be enabled.
     * Please note, that all slides have to implement ISlideBackgroundColorHolder.
     *
     * @param colorTransitionsEnabled Whether color transitions should be enabled
     */
    public void setColorTransitionsEnabled(boolean colorTransitionsEnabled) {
        areColorTransitionsEnabled = colorTransitionsEnabled;
    }

    /**
     * Sets the animation of the intro to a fade animation
     */
    /*public void setFadeAnimation() {
        pager.setPageTransformer(true, new ViewPageTransformer(FADE));
    }

    /**
     * Sets the animation of the intro to a zoom animation
     */
  /*  public void setZoomAnimation() {
        pager.setPageTransformer(true, new ViewPageTransformer(ZOOM));
    }

    /**
     * Sets the animation of the intro to a flow animation
     */
  /*  public void setFlowAnimation() {
        pager.setPageTransformer(true, new ViewPageTransformer(FLOW));
    }

    /**
     * Sets the animation of the intro to a Slide Over animation
     */
 /*   public void setSlideOverAnimation() {
        pager.setPageTransformer(true, new ViewPageTransformer(SLIDE_OVER));
    }

    /**
     * Sets the animation of the intro to a Depth animation
     */
  /*  public void setDepthAnimation() {
        pager.setPageTransformer(true, new ViewPageTransformer(DEPTH));
    }

    /**
     * Overrides viewpager transformer
     *
     * @param transformer your custom transformer
     */
  /*  public void setCustomTransformer(@Nullable ViewPager.PageTransformer transformer) {
        pager.setPageTransformer(true, transformer);
    }

    /**
     * Overrides color of selected and unselected indicator colors
     * <p>
     * Set DEFAULT_COLOR for color value if you don't want to change it
     *
     * @param selectedIndicatorColor   your selected color
     * @param unselectedIndicatorColor your unselected color
     */
    public void setIndicatorColor(int selectedIndicatorColor, int unselectedIndicatorColor) {
        this.selectedIndicatorColor = selectedIndicatorColor;
        this.unselectedIndicatorColor = unselectedIndicatorColor;

        if (mController != null) {
            if (selectedIndicatorColor != DEFAULT_COLOR)
                mController.setSelectedIndicatorColor(selectedIndicatorColor);
            if (unselectedIndicatorColor != DEFAULT_COLOR)
                mController.setUnselectedIndicatorColor(unselectedIndicatorColor);
        }
    }

    /**
     * Setting to disable forward swiping right on current page and allow swiping left. If a swipe
     * left occurs, the lock state is reset and swiping is re-enabled. (one shot disable) This also
     * hides/shows the Next and Done buttons accordingly.
     *
     * @param lockEnable Set true to disable forward swiping. False to enable.
     */
    public void setNextPageSwipeLock(boolean lockEnable) {
        if (lockEnable) {
            // if locking, save current progress button visibility
            baseProgressButtonEnabled = progressButtonEnabled;
            setProgressButtonEnabled(false);
        } else {
            // if unlocking, restore original button visibility
            setProgressButtonEnabled(baseProgressButtonEnabled);
        }
        pager.setNextPagingEnabled(!lockEnable);
    }

    /**
     * Setting to disable swiping left and right on current page. This also
     * hides/shows the Next and Done buttons accordingly.
     *
     * @param lockEnable Set true to disable forward swiping. False to enable.
     */
    public void setSwipeLock(boolean lockEnable) {
        if (lockEnable) {
            // if locking, save current progress button visibility
            baseProgressButtonEnabled = progressButtonEnabled;
            //setProgressButtonEnabled(!lockEnable);
        } else {
            // if unlocking, restore original button visibility
            setProgressButtonEnabled(baseProgressButtonEnabled);
        }
        pager.setPagingEnabled(!lockEnable);
    }

    /**
     * Enables/Disables leaving the intro via the device's software/hardware back button.
     * Note, that does does NOT lock swiping back through the slides.
     *
     * @param lockEnabled Whether leaving the intro via the phone's back button is locked.
     */
    public void setGoBackLock(boolean lockEnabled) {
        isGoBackLockEnabled = lockEnabled;
    }

    /**
     * Specifies whether to enable the non-sticky immersive mode.
     * This is a shorthand method for {@link #setImmersiveMode(boolean, boolean)} the second parameter set to false.
     * If you want to enable the sticky immersive mode (translucent bars), use {@link #setImmersiveMode(boolean, boolean)} instead.
     * Note that immersive mode is only supported on Kitkat and newer.
     *
     * @param isEnabled Whether the immersive mode (non-sticky) should be enabled or not.
     */
    public void setImmersiveMode(boolean isEnabled) {
        setImmersiveMode(isEnabled, false);
    }

    /**
     * Specifies whether to enable the immersive mode.
     * Note that immersive mode is only supported on Kitkat and newer.
     *
     * @param isEnabled Whether the immersive mode should be enabled or not.
     * @param isSticky  Whether to use the sticky immersive mode or not
     */
    public void setImmersiveMode(boolean isEnabled, boolean isSticky) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!isEnabled && isImmersiveModeEnabled) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

                isImmersiveModeEnabled = false;
            } else if (isEnabled) {

                int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;

                if (isSticky) {
                    flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                    isImmersiveModeSticky = true;
                } else {
                    flags |= View.SYSTEM_UI_FLAG_IMMERSIVE;
                    isImmersiveModeSticky = false;
                }

                getWindow().getDecorView().setSystemUiVisibility(flags);

                isImmersiveModeEnabled = true;
            }
        }
    }

    private void changeSlide(boolean isLastSlide) {
        if (isLastSlide) {
            Fragment currentFragment = mPagerAdapter.getItem(pager.getCurrentItem());
            onDonePressed(currentFragment);
        } else {
            pager.goToNextSlide();
            onNextPressed();
        }
    }

    private final class NextButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            changeSlide(false);
        }
    }

    public class PagerOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (areColorTransitionsEnabled) {
                if (position < mPagerAdapter.getCount() - 1) {
                  /*  if (mPagerAdapter.getItem(position) instanceof ISlideBackgroundColorHolder &&
                            mPagerAdapter.getItem(position + 1) instanceof ISlideBackgroundColorHolder) {
                        Fragment currentSlide = mPagerAdapter.getItem(position);
                        Fragment nextSlide = mPagerAdapter.getItem(position + 1);

                        ISlideBackgroundColorHolder currentSlideCasted =
                                (ISlideBackgroundColorHolder) currentSlide;
                        ISlideBackgroundColorHolder nextSlideCasted =
                                (ISlideBackgroundColorHolder) nextSlide;

                        // Check if both fragments are attached to an activity,
                        // otherwise getDefaultBackgroundColor may fail.
                        if (currentSlide.isAdded() && nextSlide.isAdded()) {
                            int newColor = (int) argbEvaluator.evaluate(positionOffset,
                                    currentSlideCasted.getDefaultBackgroundColor(),
                                    nextSlideCasted.getDefaultBackgroundColor());

                            currentSlideCasted.setBackgroundColor(newColor);
                            nextSlideCasted.setBackgroundColor(newColor);
                        }
                    } else {
                        throw new IllegalStateException("Color transitions are only available if all slides implement ISlideBackgroundColorHolder.");
                    }*/
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (slidesNumber > 1)
                mController.selectPosition(position);

            // Allow the swipe to be re-enabled if a user swipes to a previous slide. Restore
            // state of progress button depending on global progress button setting
            if (!pager.isNextPagingEnabled()) {
                if (pager.getCurrentItem() != pager.getLockPage()) {
                    setProgressButtonEnabled(baseProgressButtonEnabled);
                    pager.setNextPagingEnabled(true);
                } else {
                    setProgressButtonEnabled(progressButtonEnabled);
                }
            } else {
                setProgressButtonEnabled(progressButtonEnabled);
            }

            AppIntroBase.this.onPageSelected(position);

            currentlySelectedItem = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private final class WindowGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (isImmersiveModeEnabled && !isImmersiveModeSticky) {
                setImmersiveMode(true, false);
            }

            return false;
        }
    }
}
