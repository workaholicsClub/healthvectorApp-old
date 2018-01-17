package ru.android.childdiary;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.util.List;

import io.reactivex.Observable;
import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.utils.TimesMatcher;

public class TimesMatcherTest {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm");

    private static final TestData[] TEST_DATA = new TestData[]{
            TestData.builder().startTime(new LocalTime(8, 0)).finishTime(new LocalTime(22, 0)).count(5).build(),
            TestData.builder().startTime(new LocalTime(8, 0)).finishTime(new LocalTime(22, 0)).count(4).build(),
            TestData.builder().startTime(new LocalTime(8, 0)).finishTime(new LocalTime(22, 0)).count(3).build(),
            TestData.builder().startTime(new LocalTime(8, 0)).finishTime(new LocalTime(22, 0)).count(2).build(),
            TestData.builder().startTime(new LocalTime(8, 0)).finishTime(new LocalTime(22, 0)).count(1).build(),
            TestData.builder().startTime(new LocalTime(8, 0)).finishTime(new LocalTime(9, 0)).count(5).build(),
            TestData.builder().startTime(new LocalTime(8, 0)).finishTime(new LocalTime(7, 0)).count(5).build(),
            TestData.builder().startTime(new LocalTime(8, 0)).finishTime(new LocalTime(8, 20)).count(5).build(),
            TestData.builder().startTime(new LocalTime(8, 41)).finishTime(new LocalTime(8, 47)).count(5).build(),
            TestData.builder().startTime(new LocalTime(8, 41)).finishTime(new LocalTime(8, 43)).count(5).build(),
            TestData.builder().startTime(new LocalTime(8, 41)).finishTime(new LocalTime(8, 43)).count(1).build()
    };

    private static String join(List<String> strings) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < strings.size(); ++i) {
            result.append(strings.get(i))
                    .append(i == strings.size() - 1 ? "" : ", ");
        }
        return result.toString();
    }

    @Test
    public void test() {
        for (TestData testData : TEST_DATA) {
            System.out.println("start: " + TIME_FORMATTER.print(testData.getStartTime())
                    + "; finish: " + TIME_FORMATTER.print(testData.getFinishTime())
                    + "; count: " + testData.getCount());
            List<LocalTime> times = TimesMatcher.match(testData.getStartTime(), testData.getFinishTime(), testData.getCount());
            List<String> strings = Observable.fromIterable(times)
                    .map(TIME_FORMATTER::print)
                    .toList()
                    .blockingGet();
            System.out.println(join(strings));
            System.out.println();
        }
    }

    @Value
    @Builder
    private static class TestData {
        LocalTime startTime;
        LocalTime finishTime;
        int count;
    }
}
