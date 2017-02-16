package ru.android.childdiary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.annimon.stream.Stream;

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

        dataStore = DbUtils.getDataStore(context, DB_NAME, DB_VERSION, true);

        childDbService = new ChildDbService(dataStore);
        antropometryDbService = new AntropometryDbService(dataStore);
    }

    @AfterClass
    public static void release() {
        dataStore.close();
    }

    private static Observable<Child> generateChildren() {
        LocalDateTime now = LocalDateTime.now();

        List<Child> result = new ArrayList<>();
        for (int i = 0; i < CHILD_COUNT; ++i) {
            now = now.minusDays(i);
            now = now.minusHours(i);

            Child child = Child.builder()
                    .name("Test" + i)
                    .birthDate(now.toLocalDate())
                    .birthTime(new LocalTime(now.getHourOfDay(), now.getMinuteOfHour()))
                    .sex(Sex.MALE)
                    .imageFileName(null)
                    .height(RANDOM.nextDouble())
                    .weight(RANDOM.nextDouble())
                    .build();

            result.add(child);
        }

        return Observable.fromArray(result.toArray(new Child[result.size()]));
    }

    private static void assertObjectEqual(Object o1, Object o2) {
        assertTrue(String.format(Locale.US, "objects differ:\nfirst %s\nsecond %s", o1, o2), ObjectUtils.equals(o1, o2));
    }

    @Test
    public void testChild() {
        // 1. insert children
        List<Child> inserted = new ArrayList<>();
        generateChildren()
                .flatMap(child -> childDbService.add(child))
                .doOnNext(child -> logger.debug("value inserted: " + child))
                .doOnNext(inserted::add)
                .doOnError(error -> logger.error("failed to insert value", error))
                .subscribe();

        assertEquals("inserted values size", inserted.size(), CHILD_COUNT);

        // 2. select children
        List<Child> selected = new ArrayList<>();
        childDbService.getAll()
                .doOnNext(selected::addAll)
                .doOnError(error -> logger.error("failed to select values"))
                .subscribe();

        assertEquals("selected values size", selected.size(), CHILD_COUNT);

        // 3. compare inserted and selected
        for (int i = 0; i < CHILD_COUNT; ++i) {
            assertObjectEqual(inserted.get(i), selected.get(i));
        }
    }

    @Test
    public void testNegativeUpdateNonExistent() {
        LocalDateTime now = LocalDateTime.now();

        Child child = Child.builder()
                .name("UpdateNonExistent")
                .birthDate(now.toLocalDate())
                .birthTime(new LocalTime(now.getHourOfDay(), now.getMinuteOfHour()))
                .sex(Sex.FEMALE)
                .imageFileName(null)
                .height(RANDOM.nextDouble())
                .weight(RANDOM.nextDouble())
                .build();

        childDbService.update(child)
                .doOnNext(item -> logger.debug("value inserted: " + item))
                .doOnError(error -> logger.error("failed to insert value", error))
                .subscribe(item -> fail(), error -> logger.debug("expected error", error));
    }

    @Test
    public void testAntropometry() {
        LocalDateTime now = LocalDateTime.now();

        // 0. build child
        List<Child> insertedChildren = new ArrayList<>();
        {
            Child child = Child.builder()
                    .name("AntropometryTest")
                    .birthDate(now.toLocalDate())
                    .birthTime(new LocalTime(now.getHourOfDay(), now.getMinuteOfHour()))
                    .sex(Sex.FEMALE)
                    .imageFileName(null)
                    .height(RANDOM.nextDouble())
                    .weight(RANDOM.nextDouble())
                    .build();

            childDbService.add(child)
                    .doOnNext(item -> logger.debug("value inserted: " + item))
                    .doOnNext(insertedChildren::add)
                    .doOnError(error -> logger.error("failed to insert value", error))
                    .subscribe();

            assertEquals("child wasn't inserted once", insertedChildren.size(), 1);
        }
        Child insertedChild = insertedChildren.get(0);

        // 1. insert antropometry
        List<Antropometry> inserted = new ArrayList<>();
        for (int j = 0; j < ANTROPOMETRY_COUNT; ++j) {
            Antropometry antropometry = Antropometry.builder()
                    .child(insertedChild)
                    .date(now.minusDays(j).toLocalDate())
                    .height(insertedChild.getHeight() - 0.1 * j)
                    .weight(insertedChild.getWeight() - 0.1 * j)
                    .build();
            antropometryDbService.add(antropometry)
                    .doOnNext(item -> logger.debug("value inserted: " + item))
                    .doOnNext(inserted::add)
                    .doOnError(error -> logger.error("failed to insert value", error))
                    .subscribe();
        }

        assertEquals("inserted values size", inserted.size(), ANTROPOMETRY_COUNT);

        Collections.sort(inserted, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));

        // 2. select antropometry
        List<Antropometry> selected = new ArrayList<>();
        antropometryDbService.getAll(insertedChild)
                .doOnNext(selected::addAll)
                .doOnError(error -> logger.error("failed to select values"))
                .subscribe();

        assertEquals("selected values size", selected.size(), ANTROPOMETRY_COUNT);

        // 3. compare inserted and selected
        for (int i = 0; i < ANTROPOMETRY_COUNT; ++i) {
            assertObjectEqual(inserted.get(i), selected.get(i));
        }

        // 4. select children
        List<Child> selectedChildren = new ArrayList<>();
        childDbService.getAll()
                .doOnNext(selectedChildren::addAll)
                .doOnError(error -> logger.error("failed to select values"))
                .subscribe();

        long found = Stream.of(selectedChildren).filter(selectedChild -> selectedChild.equals(insertedChild)).count();
        assertEquals("chosen child wasn't inserted once", found, 1);

        // delete some antropometry
        antropometryDbService.delete(selected.get(0))
                .doOnNext(item -> logger.debug("value deleted: " + item))
                .doOnError(error -> logger.error("failed to delete value", error))
                .subscribe();

        // select antropometry
        selected = new ArrayList<>();
        antropometryDbService.getAll(insertedChild)
                .doOnNext(selected::addAll)
                .doOnError(error -> logger.error("failed to select values"))
                .subscribe();

        assertEquals("after single antropometry deletion: selected values size", selected.size(), ANTROPOMETRY_COUNT - 1);

        // 5. cascade delete
        childDbService.delete(insertedChild)
                .doOnNext(item -> logger.debug("value deleted: " + item))
                .doOnError(error -> logger.error("failed to delete value", error))
                .subscribe();

        // 6. select children
        selectedChildren = new ArrayList<>();
        childDbService.getAll()
                .doOnNext(selectedChildren::addAll)
                .doOnError(error -> logger.error("failed to select values"))
                .subscribe();

        found = Stream.of(selectedChildren).filter(selectedChild -> selectedChild.equals(insertedChild)).count();
        assertEquals("chosen child wasn't deleted", found, 0);

        // 7. select antropometry
        selected = new ArrayList<>();
        antropometryDbService.getAll(insertedChild)
                .doOnNext(selected::addAll)
                .doOnError(error -> logger.error("failed to select values"))
                .subscribe();

        assertEquals("cascade delete doesn't work", selected.size(), 0);
    }
}
