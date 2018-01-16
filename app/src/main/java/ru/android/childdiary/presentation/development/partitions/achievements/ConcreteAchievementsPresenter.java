package ru.android.childdiary.presentation.development.partitions.achievements;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.types.AchievementType;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.child.ChildInteractor;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.core.requests.ChildResponse;
import ru.android.childdiary.domain.development.achievement.ConcreteAchievementInteractor;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.domain.development.achievement.requests.DeleteConcreteAchievementRequest;
import ru.android.childdiary.domain.development.achievement.requests.DeleteConcreteAchievementResponse;
import ru.android.childdiary.presentation.core.adapters.DeletedItemsManager;
import ru.android.childdiary.presentation.development.partitions.achievements.adapters.ConcreteAchievementItem;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryPresenter;

@InjectViewState
public class ConcreteAchievementsPresenter extends BaseDevelopmentDiaryPresenter<ConcreteAchievementsView> {
    private final DeletedItemsManager<ConcreteAchievement> deletedItemsManager = new DeletedItemsManager<>();

    @Inject
    ChildInteractor childInteractor;

    @Inject
    ConcreteAchievementInteractor concreteAchievementInteractor;

    private Disposable subscription;
    private Child child;
    private List<ConcreteAchievement> concreteAchievements;
    private Set<AchievementType> expandedGroups = new HashSet<>();

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(
                childInteractor.getActiveChild()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::requestData, this::onUnexpectedError));
    }

    private void requestData(@NonNull Child child) {
        this.child = child;
        unsubscribe(subscription);
        subscription = unsubscribeOnDestroy(concreteAchievementInteractor.getConcreteAchievements(child)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::map)
                .map(concreteAchievements -> ConcreteAchievementsState.builder()
                        .child(child)
                        .concreteAchievements(concreteAchievements)
                        .build())
                .subscribe(getViewState()::showConcreteAchievementsState, this::onUnexpectedError));
    }

    private List<ConcreteAchievementItem> map(@NonNull List<ConcreteAchievement> concreteAchievements) {
        this.concreteAchievements = concreteAchievements;
        List<ConcreteAchievementItem> items = new ArrayList<>();
        for (AchievementType achievementType : AchievementType.values()) {
            List<ConcreteAchievement> children = new ArrayList<>();
            //noinspection Convert2streamapi
            for (ConcreteAchievement concreteAchievement : concreteAchievements) {
                if (concreteAchievement.getAchievementType() == achievementType) {
                    children.add(concreteAchievement);
                }
            }

            if (children.isEmpty()) {
                continue;
            }

            boolean expanded = expandedGroups.contains(achievementType);
            items.add(new ConcreteAchievementItem(achievementType, expanded));
            if (expanded) {
                //noinspection Convert2streamapi
                for (ConcreteAchievement concreteAchievement : children) {
                    items.add(new ConcreteAchievementItem(concreteAchievement));
                }
            }
        }
        return items;
    }

    public void toggle(@NonNull AchievementType achievementType) {
        boolean expanded = expandedGroups.contains(achievementType);
        if (expanded) {
            collapse(achievementType);
        } else {
            expand(achievementType);
        }
    }

    public void expand(@NonNull AchievementType achievementType) {
        if (expandedGroups.contains(achievementType)) {
            return;
        }
        expandedGroups.add(achievementType);
        update();
    }

    public void collapse(@NonNull AchievementType achievementType) {
        if (!expandedGroups.contains(achievementType)) {
            return;
        }
        expandedGroups.remove(achievementType);
        update();
    }

    private void update() {
        if (child == null) {
            logger.error("child is null");
            return;
        }
        if (concreteAchievements == null) {
            logger.error("concrete achievements not loaded yet");
            return;
        }
        List<ConcreteAchievementItem> items = map(concreteAchievements);
        ConcreteAchievementsState state = ConcreteAchievementsState.builder()
                .child(child)
                .concreteAchievements(items)
                .build();
        getViewState().showConcreteAchievementsState(state);
    }

    public void edit(@NonNull ConcreteAchievement concreteAchievement) {
        if (deletedItemsManager.check(concreteAchievement)) {
            return;
        }
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                child -> getViewState().navigateToConcreteAchievement(child, concreteAchievement),
                                this::onUnexpectedError));
    }

    public void delete(@NonNull ConcreteAchievement concreteAchievement) {
        if (deletedItemsManager.check(concreteAchievement)) {
            return;
        }
        getViewState().confirmDelete(concreteAchievement);
    }

    public void forceDelete(@NonNull ConcreteAchievement concreteAchievement) {
        if (deletedItemsManager.checkAndAdd(concreteAchievement)) {
            return;
        }
        unsubscribeOnDestroy(
                concreteAchievementInteractor.delete(DeleteConcreteAchievementRequest.builder()
                        .concreteAchievement(concreteAchievement)
                        .build())
                        .map(DeleteConcreteAchievementResponse::getRequest)
                        .map(DeleteConcreteAchievementRequest::getConcreteAchievement)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::deleted, this::onUnexpectedError));
    }

    public void addConcreteAchievement() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMap(child -> concreteAchievementInteractor.getDefaultConcreteAchievement(child)
                                .map(item -> new ChildResponse<>(child, item)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getChild().getId() == null) {
                                        getViewState().noChildSpecified();
                                        return;
                                    }
                                    ConcreteAchievement concreteAchievement = response.getResponse();
                                    getViewState().navigateToConcreteAchievementAdd(concreteAchievement.getChild(), concreteAchievement);
                                },
                                this::onUnexpectedError));
    }

    public void addProfile() {
        getViewState().navigateToProfileAdd();
    }
}
