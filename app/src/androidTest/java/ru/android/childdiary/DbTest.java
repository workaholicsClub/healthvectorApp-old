package ru.android.childdiary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.repositories.child.AntropometryDbService;
import ru.android.childdiary.data.repositories.child.ChildDbService;
import ru.android.childdiary.data.repositories.child.mappers.AntropometryMapper;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.child.Antropometry;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.log.LogSystem;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class DbTest {
    private static final String DB_NAME = BuildConfig.DB_NAME + "-test";
    private static final int DB_VERSION = 1;

    private static final Random RANDOM = new Random();

    private static final int CHILD_COUNT = 10;
    private static final int ANTROPOMETRY_COUNT = 10;

    private static ReactiveEntityStore<Persistable> dataStore;

    private static ChildDbService childDbService;
    private static AntropometryDbService antropometryDbService;

    private final Logger logger = LoggerFactory.getLogger(toString());

    private static Context getContext() {
        // can write only in target context
        return InstrumentationRegistry.getTargetContext();
    }

    @BeforeClass
    public static void initApplication() {
        Context context = getContext();

        LogSystem.initLogger(context);

        JodaTimeAndroid.init(context);

        dataStore = DbUtils.getDataStore(context, DB_NAME, DB_VERSION);

        ChildMapper childMapper = new ChildMapper();
        AntropometryMapper antropometryMapper = new AntropometryMapper(childMapper);
        childDbService = new ChildDbService(dataStore, childMapper);
        antropometryDbService = new AntropometryDbService(dataStore, antropometryMapper);
    }

    @AfterClass
    public static void release() {
        dataStore.close();
    }

    private void assertObjectEqual(Object o1, Object o2) {
        assertTrue(String.format(Locale.US, "objects differ:\nfirst %s\nsecond %s", o1, o2), ObjectUtils.equals(o1, o2));
    }

    private <T> void logOnNextSelect(T items) {
        logger.debug("values selected: " + items);
    }

    private void logOnErrorSelect(Throwable throwable) {
        logger.error("failed to select values", throwable);
    }

    private <T> void logOnNextInsert(T item) {
        logger.debug("value inserted: " + item);
    }

    private void logOnErrorInsert(Throwable throwable) {
        logger.error("failed to insert value", throwable);
    }

    private <T> void logOnNextUpdate(T item) {
        logger.debug("value updated: " + item);
    }

    private void logOnErrorUpdate(Throwable throwable) {
        logger.error("failed to update value", throwable);
    }

    private <T> void logOnNextDelete(T item) {
        logger.debug("value deleted: " + item);
    }

    private void logOnErrorDelete(Throwable throwable) {
        logger.error("failed to delete value", throwable);
    }

    private List<Child> checkInSelection(int count, Child... inserted) {
        List<Child> selected = new ArrayList<>();
        childDbService.getAll()
                .doOnNext(this::logOnNextSelect)
                .doOnNext(selected::addAll)
                .doOnError(this::logOnErrorSelect)
                .subscribe();
        selectedContainInserted(new ChildIdsComparator(), selected, count, inserted);
        return selected;
    }

    private List<Antropometry> checkInSelection(Child child, int count, Antropometry... inserted) {
        List<Antropometry> selected = new ArrayList<>();
        antropometryDbService.getAll(child)
                .doOnNext(this::logOnNextSelect)
                .doOnNext(selected::addAll)
                .doOnError(this::logOnErrorSelect)
                .subscribe();
        selectedContainInserted(new AntropometryIdsComparator(), selected, count, inserted);
        return selected;
    }

    @SafeVarargs
    private final <T> void selectedContainInserted(IdsComparator<T> idsEqual, List<T> selected, int count, T... items) {
        for (T insertedItem : items) {
            long found;
            found = Observable.fromIterable(selected).filter(item -> item.equals(insertedItem)).count().blockingGet();
            assertEquals("item value found in selection", count, found);
            found = Observable.fromIterable(selected).filter(item -> idsEqual.idsEqual(item, insertedItem)).count().blockingGet();
            assertEquals("item id found in selection", count, found);
        }
    }

    private Child add(Child item) {
        List<Child> inserted = new ArrayList<>();
        childDbService.add(item)
                .doOnNext(this::logOnNextInsert)
                .doOnNext(inserted::add)
                .doOnError(this::logOnErrorInsert)
                .subscribe();
        assertEquals("inserted values size", 1, inserted.size());
        return inserted.get(0);
    }

    private Antropometry add(Child child, Antropometry item) {
        List<Antropometry> inserted = new ArrayList<>();
        item = item.toBuilder().child(child).build();
        antropometryDbService.add(item)
                .doOnNext(this::logOnNextInsert)
                .doOnNext(inserted::add)
                .doOnError(this::logOnErrorInsert)
                .subscribe();
        assertEquals("inserted values size", 1, inserted.size());
        return inserted.get(0);
    }

    private Child delete(Child item) {
        List<Child> deleted = new ArrayList<>();
        childDbService.delete(item)
                .doOnNext(this::logOnNextDelete)
                .doOnNext(deleted::add)
                .doOnError(this::logOnErrorDelete)
                .subscribe();
        assertEquals("deleted values size", 1, deleted.size());
        assertEquals("deleted value", item, deleted.get(0));
        return item;
    }

    private Antropometry delete(Antropometry item) {
        List<Antropometry> deleted = new ArrayList<>();
        antropometryDbService.delete(item)
                .doOnNext(this::logOnNextDelete)
                .doOnNext(deleted::add)
                .doOnError(this::logOnErrorDelete)
                .subscribe();
        assertEquals("deleted values size", 1, deleted.size());
        assertEquals("deleted value", item, deleted.get(0));
        return item;
    }

    private Child update(Child item) {
        List<Child> updated = new ArrayList<>();
        childDbService.update(item)
                .doOnNext(this::logOnNextDelete)
                .doOnNext(updated::add)
                .doOnError(this::logOnErrorDelete)
                .subscribe();
        assertEquals("updated values size", 1, updated.size());
        assertEquals("updated value", item, updated.get(0));
        return item;
    }

    private Antropometry update(Antropometry item) {
        List<Antropometry> updated = new ArrayList<>();
        antropometryDbService.update(item)
                .doOnNext(this::logOnNextDelete)
                .doOnNext(updated::add)
                .doOnError(this::logOnErrorDelete)
                .subscribe();
        assertEquals("updated values size", 1, updated.size());
        assertEquals("updated value", item, updated.get(0));
        return item;
    }

    private Child getRandomChild() {
        LocalDateTime now = LocalDateTime.now();
        return Child.builder()
                .name("Child" + RANDOM.nextInt())
                .birthDate(now.toLocalDate().minusDays(RANDOM.nextInt()))
                .birthTime(new LocalTime(now.getHourOfDay(), now.getMinuteOfHour()).minusMinutes(RANDOM.nextInt()))
                .sex(RANDOM.nextBoolean() ? Sex.MALE : Sex.FEMALE)
                .imageFileName(null)
                .birthHeight(RANDOM.nextDouble())
                .birthWeight(RANDOM.nextDouble())
                .build();
    }

    private Antropometry getRandomAntropometry(int j) {
        LocalDateTime now = LocalDateTime.now();
        return Antropometry.builder()
                .date(now.toLocalDate().minusDays(j))
                .height(RANDOM.nextDouble())
                .weight(RANDOM.nextDouble())
                .build();
    }

    private Child insertRandomChild() {
        Child child = getRandomChild();
        child = add(child);
        checkInSelection(1, child);
        return child;
    }

    private List<Antropometry> insertRandomAntropometry(Child child) {
        List<Antropometry> inserted = new ArrayList<>();
        for (int j = 0; j < ANTROPOMETRY_COUNT; ++j) {
            Antropometry antropometry = getRandomAntropometry(j);
            antropometry = add(child, antropometry);
            inserted.add(antropometry);
        }
        assertEquals("inserted values size", ANTROPOMETRY_COUNT, inserted.size());
        List<Antropometry> selected = checkInSelection(child, 1, inserted.toArray(new Antropometry[inserted.size()]));
        Collections.sort(inserted, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        for (int i = 0; i < ANTROPOMETRY_COUNT; ++i) {
            assertObjectEqual(inserted.get(i), selected.get(i));
        }
        return inserted;
    }

    @Test
    public void testDelete() {
        Child child = insertRandomChild();
        List<Antropometry> selected1 = insertRandomAntropometry(child);
        Antropometry antropometry = selected1.get(0);

        delete(antropometry);
        List<Antropometry> selected2 = checkInSelection(child, 0, antropometry);
        assertEquals("after deletion: selected values size", selected1.size() - 1, selected2.size());

        delete(child);
        checkInSelection(0, child);
        List<Antropometry> selected = new ArrayList<>();
        antropometryDbService.getAll(child)
                .doOnNext(this::logOnNextSelect)
                .doOnNext(selected::addAll)
                .doOnError(this::logOnErrorSelect)
                .subscribe();
        assertEquals("cascade delete doesn't work", 0, selected.size());
    }

    @Test
    public void testUpdate() {
        final String updatedName = "UpdateTestTest";
        final double updatedHeight = 2;

        Child child = insertRandomChild();
        List<Antropometry> selected = insertRandomAntropometry(child);
        Antropometry antropometry = selected.get(0);

        Child updatedChild = child.toBuilder().name(updatedName).build();
        update(updatedChild);
        checkInSelection(1, updatedChild);

        Antropometry updatedAntropometry = antropometry.toBuilder().height(updatedHeight).build();
        update(updatedAntropometry);
        checkInSelection(updatedChild, 1, updatedAntropometry);
    }

    @Test
    public void testNegativeUpdateNonExistent() {
        Child child = getRandomChild();
        child = child.toBuilder().id(RANDOM.nextLong()).build();
        childDbService.update(child)
                .doOnNext(this::logOnNextUpdate)
                .doOnError(this::logOnErrorUpdate)
                .subscribe(item -> fail(), error -> logger.debug("expected error", error));
    }

    private interface IdsComparator<T> {
        boolean idsEqual(T item1, T item2);
    }

    private static class ChildIdsComparator implements IdsComparator<Child> {
        @Override
        public boolean idsEqual(Child item1, Child item2) {
            return ObjectUtils.equals(item1.getId(), item2.getId());
        }
    }

    private static class AntropometryIdsComparator implements IdsComparator<Antropometry> {
        @Override
        public boolean idsEqual(Antropometry item1, Antropometry item2) {
            return ObjectUtils.equals(item1.getId(), item2.getId());
        }
    }
}
