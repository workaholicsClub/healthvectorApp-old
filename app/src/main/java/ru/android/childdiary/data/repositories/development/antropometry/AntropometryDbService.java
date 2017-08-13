package ru.android.childdiary.data.repositories.development.antropometry;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.query.WhereAndOr;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;
import lombok.val;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.db.entities.development.AntropometryEntity;
import ru.android.childdiary.data.repositories.development.antropometry.mappers.AntropometryMapper;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.antropometry.data.Antropometry;
import ru.android.childdiary.domain.development.antropometry.requests.AntropometryListRequest;

@Singleton
public class AntropometryDbService {
    private final ReactiveEntityStore<Persistable> dataStore;
    private final BlockingEntityStore<Persistable> blockingEntityStore;
    private final AntropometryMapper antropometryMapper;

    @Inject
    public AntropometryDbService(ReactiveEntityStore<Persistable> dataStore,
                                 AntropometryMapper antropometryMapper) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
        this.antropometryMapper = antropometryMapper;
    }

    public Observable<List<Antropometry>> getAntropometryList(@NonNull AntropometryListRequest request) {
        Child child = request.getChild();
        LocalDate birthday = child.getBirthDate();

        val order = request.isAscending() ? AntropometryEntity.ANTROPOMETRY_DATE.asc() : AntropometryEntity.ANTROPOMETRY_DATE.desc();
        WhereAndOr<ReactiveResult<AntropometryEntity>> select = dataStore.select(AntropometryEntity.class)
                .where(AntropometryEntity.CHILD_ID.eq(child.getId()));

        if (request.isStartFromBirthday() && birthday != null) {
            select = select.and(AntropometryEntity.ANTROPOMETRY_DATE.greaterThan(birthday));
        }

        return select.orderBy(order, AntropometryEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, antropometryMapper))
                .map(antropometryList -> mapAntropometryList(antropometryList, request));
    }

    private List<Antropometry> mapAntropometryList(@NonNull List<Antropometry> antropometryList,
                                                   @NonNull AntropometryListRequest request) {
        Child child = request.getChild();
        LocalDate birthday = child.getBirthDate();

        if (request.isStartFromBirthday() && birthday != null) {
            Antropometry birthdayAntropometry = Antropometry.builder()
                    .child(child)
                    .date(birthday)
                    .weight(child.getBirthWeight())
                    .height(child.getBirthHeight())
                    .build();
            if (request.isAscending()) {
                antropometryList.add(0, birthdayAntropometry);
            } else {
                antropometryList.add(birthdayAntropometry);
            }
        }
        return antropometryList;
    }

    public Observable<Antropometry> add(@NonNull Antropometry antropometry) {
        return DbUtils.insertObservable(blockingEntityStore, antropometry, antropometryMapper);
    }

    public Observable<Antropometry> update(@NonNull Antropometry antropometry) {
        return DbUtils.updateObservable(blockingEntityStore, antropometry, antropometryMapper);
    }

    public Observable<Antropometry> delete(@NonNull Antropometry antropometry) {
        return DbUtils.deleteObservable(blockingEntityStore, AntropometryEntity.class, antropometry, antropometry.getId());
    }
}
