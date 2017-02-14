package ru.android.childdiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.annimon.stream.Stream;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.entities.child.AntropometryEntity;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.data.entities.child.IAntropometry;
import ru.android.childdiary.data.entities.child.IChild;
import ru.android.childdiary.data.entities.child.Sex;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.db.DbUtils;
import ru.android.childdiary.utils.log.LogSystem;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DbTest {
    private static final String DB_NAME = BuildConfig.DB_NAME + "-test";
    private static final int DB_VERSION_DEFAULT = 1;
    private static final String DB_VERSION_KEY = "dbVersionKey";
    private static final Random RANDOM = new Random();
    private static final int CHILD_COUNT = 10;
    private static final int ANTROPOMETRY_COUNT = 10;
    private static ReactiveEntityStore<Persistable> dataStore;
    private final Logger logger = LoggerFactory.getLogger(toString());

    private static Context getContext() {
        // can write only in target context
        return InstrumentationRegistry.getTargetContext();
    }

    private static int getDbVersion() {
        SharedPreferences preferences = getContext().getSharedPreferences("dbVersion", Context.MODE_PRIVATE);
        int dbVersion = preferences.getInt(DB_VERSION_KEY, DB_VERSION_DEFAULT);
        preferences.edit().putInt(DB_VERSION_KEY, dbVersion + 1).apply();
        return dbVersion;
    }

    @BeforeClass
    public static void initApplication() {
        Context context = getContext();

        LogSystem.initLogger(context);

        JodaTimeAndroid.init(context);

        dataStore = DbUtils.getDataStore(context, DB_NAME, getDbVersion(), true);
    }

    @AfterClass
    public static void release() {
        dataStore.close();
    }

    private static List<ChildEntity> getChildEntities() {
        List<ChildEntity> result = new ArrayList<>();
        for (int i = 0; i < CHILD_COUNT; ++i) {
            ChildEntity childEntity = new ChildEntity();
            childEntity.setName("Test" + i);
            childEntity.setBirthDate(LocalDate.now().minusDays(i));
            childEntity.setBirthTime(LocalTime.now().minusHours(i));
            childEntity.setSex(Sex.MALE);
            childEntity.setImageFileName(null);
            childEntity.setHeight(RANDOM.nextDouble());
            childEntity.setWeight(RANDOM.nextDouble());
            result.add(childEntity);
        }
        return result;
    }

    @Test
    public void testDb() {
        List<ChildEntity> expected = getChildEntities();

        assertTrue("expected array isn't valid", expected != null && expected.size() > 0);

        // 1. insert child entities
        dataStore.insert(expected)
                .doOnSuccess(childEntities -> Stream.of(childEntities).forEach(childEntity -> logger.debug("value inserted: " + childEntity)))
                .doOnError(throwable -> logger.error("failed to insert child entities", throwable))
                .subscribe();

        // 2. select child entities
        List<ChildEntity> actual = dataStore.select(ChildEntity.class).get().toList();

        // 3. compare child entities
        assertTrue("sizes differ!", actual.size() == expected.size() && actual.size() == CHILD_COUNT);

        for (int i = 0; i < CHILD_COUNT; ++i) {
            ChildEntity a = actual.get(i), e = expected.get(i);
            assertTrue(String.format("objects at %d differ: expected %s, actual %s", i, e, a), ObjectUtils.equals(a, e));
        }

        // 1. insert antropometry entities
        for (int i = 0; i < CHILD_COUNT / 2; ++i) {
            ChildEntity childEntity = expected.get(i);
            for (int j = 0; j < ANTROPOMETRY_COUNT; ++j) {
                AntropometryEntity antropometryEntity = new AntropometryEntity();
                antropometryEntity.setChild(childEntity);
                antropometryEntity.setDate(LocalDate.now().minusDays(j));
                antropometryEntity.setHeight(childEntity.getHeight() - 0.1 * j);
                antropometryEntity.setWeight(childEntity.getWeight() - 0.1 * j);
                dataStore.insert(antropometryEntity)
                        .doOnSuccess(antropometryEntity1 -> logger.debug("value inserted: " + antropometryEntity1))
                        .doOnError(throwable -> logger.error("failed to insert antropometry entity", throwable))
                        .subscribe();
            }
        }

        // 2. select child entities
        expected = dataStore.select(ChildEntity.class).get().toList();

        // 3. compare
        for (ChildEntity e : expected) {
            for (IAntropometry antropometryEntity : e.getAntropometryList()) {
                IChild a = antropometryEntity.getChild();
                assertTrue(String.format("objects differ: expected %s, actual %s", e, a), ObjectUtils.equals(a, e));
            }
        }
    }
}
