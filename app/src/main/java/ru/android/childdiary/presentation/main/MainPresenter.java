package ru.android.childdiary.presentation.main;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.LocalTime;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.repositories.core.events.ActiveChildChangedEvent;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.calendar.CalendarInteractor;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.domain.interactors.calendar.requests.AddDiaperRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.AddFeedRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.AddOtherRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.AddPumpRequest;
import ru.android.childdiary.domain.interactors.calendar.requests.AddSleepRequest;
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

    private boolean isFirstTime = true;

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
                .subscribe(this::onGetChildList, this::onUnexpectedError));
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    private void onGetChildList(@NonNull List<Child> childList) {
        logger.debug("onGetChildList: " + StringUtils.childList(childList));
        getViewState().showChildList(childList);
        if (childList.isEmpty()) {
            if (isFirstTime) {
                getViewState().navigateToProfileAdd();
            }
        } else {
            unsubscribeOnDestroy(childInteractor.getActiveChild()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::showChild, this::onUnexpectedError));
        }
        isFirstTime = false;
    }

    public void switchChild(@NonNull Child child) {
        logger.debug("user switch child: " + child);
        unsubscribeOnDestroy(childInteractor.setActiveChild(child)
                .ignoreElements()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, this::onUnexpectedError));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setActiveChild(ActiveChildChangedEvent event) {
        getViewState().showChild(event.getChild());
    }

    public void addChild() {
        getViewState().navigateToProfileAdd();
    }

    public void editChild() {
        unsubscribeOnDestroy(childInteractor.getActiveChild()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::navigateToProfileEdit, this::onUnexpectedError));
    }

    public void reviewChild() {
        getViewState().navigateToProfileAdd();
    }

    public void deleteChild() {
        unsubscribeOnDestroy(childInteractor.getActiveChild()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showDeleteChildConfirmation, this::onUnexpectedError));
    }

    public void deleteChild(@NonNull Child child) {
        unsubscribeOnDestroy(childInteractor.delete(child)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deletedChild -> logger.debug("onDeleteChild: " + deletedChild), this::onUnexpectedError));
    }

    public void addDiaperEvent() {
        Observable.combineLatest(
                calendarInteractor.getSelectedDate().map(date -> DiaperEvent.builder().dateTime(date.toDateTime(LocalTime.MIDNIGHT)).build()),
                childInteractor.getActiveChild().firstOrError().toObservable(),
                (event, child) -> AddDiaperRequest.builder().event(event).child(child).build())
                .flatMap(request -> calendarInteractor.add(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventAdded, this::onUnexpectedError);
    }

    public void addSleepEvent() {
        Observable.combineLatest(
                calendarInteractor.getSelectedDate().map(date -> SleepEvent.builder().dateTime(date.toDateTime(LocalTime.MIDNIGHT)).build()),
                childInteractor.getActiveChild().firstOrError().toObservable(),
                (event, child) -> AddSleepRequest.builder().event(event).child(child).build())
                .flatMap(request -> calendarInteractor.add(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventAdded, this::onUnexpectedError);
    }

    public void addFeedEvent() {
        Observable.combineLatest(
                calendarInteractor.getSelectedDate().map(date -> FeedEvent.builder().dateTime(date.toDateTime(LocalTime.MIDNIGHT)).build()),
                childInteractor.getActiveChild().firstOrError().toObservable(),
                (event, child) -> AddFeedRequest.builder().event(event).child(child).build())
                .flatMap(request -> calendarInteractor.add(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventAdded, this::onUnexpectedError);
    }

    public void addPumpEventClick() {
        Observable.combineLatest(
                calendarInteractor.getSelectedDate().map(date -> PumpEvent.builder().dateTime(date.toDateTime(LocalTime.MIDNIGHT)).build()),
                childInteractor.getActiveChild().firstOrError().toObservable(),
                (event, child) -> AddPumpRequest.builder().event(event).child(child).build())
                .flatMap(request -> calendarInteractor.add(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventAdded, this::onUnexpectedError);
    }

    public void addOtherEventClick() {
        Observable.combineLatest(
                calendarInteractor.getSelectedDate().map(date -> OtherEvent.builder().dateTime(date.toDateTime(LocalTime.MIDNIGHT)).build()),
                childInteractor.getActiveChild().firstOrError().toObservable(),
                (event, child) -> AddOtherRequest.builder().event(event).child(child).build())
                .flatMap(request -> calendarInteractor.add(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventAdded, this::onUnexpectedError);
    }

    private void onEventAdded(MasterEvent event) {
        logger.debug("onEventAdded: " + event);
    }
}
