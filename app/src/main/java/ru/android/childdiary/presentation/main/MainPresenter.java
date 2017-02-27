package ru.android.childdiary.presentation.main;

import com.annimon.stream.Stream;
import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.utils.ObjectUtils;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {
    @Inject
    ChildInteractor childInteractor;

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
                .subscribe(this::onChildListLoaded, this::onUnexpectedError));
    }

    private void onChildListLoaded(List<Child> childList) {
        logger.debug("onChildListLoaded");
        // TODO: брать последний активный профиль из настроек
        // TODO: сохранять в настройки последнего добавленного ребенка
        boolean isFirstTime = this.childList == null;
        this.childList = childList;
        if (activeChild != null) {
            // проверяем, все еще ли элемент находится в списке
            activeChild = Stream.of(childList)
                    .filter(c -> ObjectUtils.equals(c.getId(), activeChild.getId()))
                    .findFirst().orElse(null);
        }
        if (activeChild == null) {
            activeChild = childList.isEmpty() ? null : childList.get(0);
        }
        getViewState().childListLoaded(childList);
        getViewState().setActive(activeChild);
        if (childList.isEmpty() && isFirstTime) {
            getViewState().addChild();
        }
    }

    public void addChild() {
        getViewState().addChild();
    }

    public void editChild() {
        if (activeChild == null) {
            logger.warn("editChild: active child is null");
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

    public void toggleChild(Long id) {
        Child child = Stream.of(childList)
                .filter(c -> ObjectUtils.equals(c.getId(), id))
                .findFirst().orElse(null);
        if (child == null) {
            logger.warn("editChild: child with id=" + id + " not found");
        }
        activeChild = child;
        getViewState().setActive(child);
    }

    public void deleteChild() {
        if (activeChild == null) {
            logger.warn("deleteChild: active child is null");
            return;
        }
        unsubscribeOnDestroy(childInteractor.delete(activeChild)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDeleteChild, this::onUnexpectedError));
    }

    private void onDeleteChild(Child child) {
        logger.debug("onDeleteChild");
    }
}
