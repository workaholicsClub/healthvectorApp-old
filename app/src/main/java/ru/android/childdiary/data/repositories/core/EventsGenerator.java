package ru.android.childdiary.data.repositories.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.reactivex.functions.Function;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.core.RepeatParametersContainer;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.core.TimeUnit;
import ru.android.childdiary.utils.ObjectUtils;

public abstract class EventsGenerator<From extends RepeatParametersContainer> {
    protected final ReactiveEntityStore<Persistable> dataStore;
    protected final BlockingEntityStore<Persistable> blockingEntityStore;

    private final Logger logger = LoggerFactory.getLogger(toString());

    public EventsGenerator(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
    }

    public int generateEvents(@NonNull From from) {
        try {
            return generateEventsInternal(from);
        } catch (EventsGeneratorException e) {
            throw new RuntimeException("generating events: no events inserted", e);
        } catch (Exception e) {
            throw new RuntimeException("generating events: unexpected error", e);
        }
    }

    private int generateEventsInternal(@NonNull From from) throws Exception {
        RepeatParameters repeatParameters = from.getRepeatParameters();
        checkRepeatParameters(repeatParameters);

        DateTime dateTime = from.getDateTime();
        checkDateTime(dateTime);

        LinearGroups linearGroups = repeatParameters.getFrequency();
        checkLinearGroups(linearGroups);

        List<LocalTime> times = linearGroups.getTimes();
        if (times.size() == 0) {
            startInsertion();
            createEvent(from, dateTime, null);
            finishInsertion();
            return 1;
        }

        PeriodicityType periodicityType = repeatParameters.getPeriodicity();
        checkPeriodicityType(periodicityType);

        LengthValue lengthValue = repeatParameters.getLength();
        checkLengthValue(lengthValue);

        Parameters parameters = getStartDateTime(dateTime, times);
        DateTime startDateTime = parameters.getStartDateTime();
        int startIndex = parameters.getStartIndex();
        DateTime finishDateTime = getFinishDateTime(startDateTime, lengthValue);
        Function<DateTime, DateTime> increment = getIncrementFunction(periodicityType);

        startInsertion();
        int count = 0;
        boolean isFinished = false;
        while (!isFinished) {
            for (int linearGroup = startIndex; linearGroup < times.size(); ++linearGroup) {
                LocalTime time = times.get(linearGroup);
                DateTime linearGroupDateTime = startDateTime.withTime(time);

                if (linearGroupDateTime.isBefore(finishDateTime)) {
                    createEvent(from, linearGroupDateTime, linearGroup);
                    ++count;
                } else {
                    isFinished = true;
                    break;
                }
            }
            startDateTime = increment.apply(startDateTime);
            startIndex = 0;
        }
        finishInsertion();
        logger.debug("generating events: inserted " + count + " events");
        return count;
    }

    private void checkRepeatParameters(@Nullable RepeatParameters repeatParameters) {
        if (repeatParameters == null) {
            throw new EventsGeneratorException("Repeat parameters is null");
        }
    }

    private void checkDateTime(@Nullable DateTime dateTime) {
        if (dateTime == null) {
            throw new EventsGeneratorException("Date time is null");
        }
    }

    private void checkLinearGroups(@Nullable LinearGroups linearGroups) {
        if (linearGroups == null) {
            throw new EventsGeneratorException("Linear groups is null");
        }
    }

    private void checkPeriodicityType(@Nullable PeriodicityType periodicityType) {
        if (periodicityType == null) {
            throw new EventsGeneratorException("Periodicity type is null");
        }
    }

    private void checkLengthValue(@Nullable LengthValue lengthValue) {
        if (ObjectUtils.isEmpty(lengthValue)) {
            throw new EventsGeneratorException("Length value is empty: " + lengthValue);
        }
    }

    private Parameters getStartDateTime(@NonNull DateTime dateTime, List<LocalTime> times) {
        // Доработка логики: если дата не сегодня, то надо генерировать события "с утра",
        // а не с указанного времени
        boolean today = dateTime.toLocalDate().isEqual(LocalDate.now());
        if (!today) {
            return Parameters.builder()
                    .startDateTime(dateTime.withTime(times.get(0)))
                    .startIndex(0)
                    .build();
        }

        DateTime startDateTime = null;
        boolean isFinished = false;
        int startIndex = 0;
        for (int i = 0; !isFinished; ++i) {
            for (int j = 0; j < times.size(); ++j) {
                LocalTime time = times.get(j);
                startDateTime = dateTime.plusDays(i).withTime(time);
                if (startDateTime.isAfter(dateTime) || startDateTime.isEqual(dateTime)) {
                    isFinished = true;
                    startIndex = j;
                    break;
                }
            }
        }
        return Parameters.builder()
                .startDateTime(startDateTime)
                .startIndex(startIndex)
                .build();
    }

    private DateTime getFinishDateTime(@NonNull DateTime startDateTime, @NonNull LengthValue lengthValue) {
        int length = lengthValue.getLength();
        TimeUnit timeUnit = lengthValue.getTimeUnit();
        switch (timeUnit) {
            case DAY:
                return startDateTime.plusDays(length);
            case WEEK:
                return startDateTime.plusWeeks(length);
            case MONTH:
                return startDateTime.plusMonths(length);
            default:
                throw new EventsGeneratorException("Unsupported time unit");
        }
    }

    private Function<DateTime, DateTime> getIncrementFunction(@NonNull PeriodicityType periodicityType) {
        switch (periodicityType) {
            case DAILY:
                return x -> x.plusDays(1);
            case WEEKLY:
                return x -> x.plusWeeks(1);
            case MONTHLY:
                return x -> x.plusMonths(1);
        }
        throw new EventsGeneratorException("Unsupported periodicity type");
    }

    protected abstract void startInsertion();

    protected abstract void createEvent(@NonNull From from, @NonNull DateTime dateTime, @Nullable Integer linearGroup);

    protected abstract void finishInsertion();

    @Value
    @Builder
    private static class Parameters {
        DateTime startDateTime;
        int startIndex;
    }

    private static class EventsGeneratorException extends IllegalArgumentException {
        public EventsGeneratorException(String message) {
            super(message);
        }
    }
}
