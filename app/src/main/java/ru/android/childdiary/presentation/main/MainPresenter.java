package ru.android.childdiary.presentation.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.core.events.ActiveChildChangedEvent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.utils.StringUtils;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    CalendarInteractor calendarInteractor;

    @Inject
    EventBus bus;

    private List<Child> childList;
    private Child activeChild;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(childInteractor.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(childResponse -> {
                    onGetChildList(childResponse.getChildList());
                    setActiveChild(childResponse.getActiveChild());
                }, this::onUnexpectedError));
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    public void toggleChild(@Nullable Long id) {
        unsubscribeOnDestroy(childInteractor.setActiveChild(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setActiveChild, this::onUnexpectedError));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setActiveChild(ActiveChildChangedEvent event) {
        setActiveChild(event.getChild());
    }

    private void onGetChildList(@NonNull List<Child> childList) {
        logger.debug("onGetChildList: " + StringUtils.childList(childList));
        boolean isFirstTime = this.childList == null;
        this.childList = childList;
        getViewState().showChildList(childList);
        if (childList.isEmpty() && isFirstTime) {
            getViewState().addChild();
        }
    }

    private void setActiveChild(@NonNull Child child) {
        logger.debug("setActiveChild: " + child);
        if (!child.equals(activeChild)) {
            logger.debug("active child changed");
            if (child == Child.NULL) {
                activeChild = null;
            } else {
                activeChild = child;
            }
            getViewState().setActive(activeChild);
        }
    }

    public void addChild() {
        getViewState().addChild();
    }

    public void editChild() {
        if (activeChild == null) {
            logger.warn("editChild: active child is null");
            return;
        }
        getViewState().editChild(activeChild);
    }

    public void reviewChild() {
        if (activeChild == null) {
            logger.warn("reviewChild: active child is null");
            return;
        }
        getViewState().reviewChild(activeChild);
    }

    public void deleteChild() {
        if (activeChild == null) {
            logger.warn("deleteChild: active child is null");
            return;
        }
        getViewState().confirmDeleteChild(activeChild);
    }

    public void deleteChild(@NonNull Child child) {
        unsubscribeOnDestroy(childInteractor.delete(child)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDeleteChild, this::onUnexpectedError));
    }

    private void onDeleteChild(Child child) {
        logger.debug("onDeleteChild: " + child);
    }

    public void addDiaperEvent() {
        unsubscribeOnDestroy(calendarInteractor.add(activeChild, DiaperEvent.builder().dateTime(DateTime.now()).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventAdded, this::onUnexpectedError));
    }

    public void addSleepEvent() {
        unsubscribeOnDestroy(calendarInteractor.add(activeChild, SleepEvent.builder().dateTime(DateTime.now()).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventAdded, this::onUnexpectedError));
    }

    public void addFeedEvent() {
        unsubscribeOnDestroy(calendarInteractor.add(activeChild, FeedEvent.builder().dateTime(DateTime.now()).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventAdded, this::onUnexpectedError));
    }

    public void addPumpEventClick() {
        unsubscribeOnDestroy(calendarInteractor.add(activeChild, PumpEvent.builder().dateTime(DateTime.now()).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventAdded, this::onUnexpectedError));
    }

    public void addOtherEventClick() {
        unsubscribeOnDestroy(calendarInteractor.add(activeChild, OtherEvent.builder().dateTime(DateTime.now()).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventAdded, this::onUnexpectedError));
    }

    private void onEventAdded(MasterEvent event) {
        logger.debug("onEventAdded: " + event);
    }
}
