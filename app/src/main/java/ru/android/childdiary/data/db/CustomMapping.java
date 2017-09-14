package ru.android.childdiary.data.db;

import io.requery.android.DefaultMapping;
import io.requery.sql.Platform;
import ru.android.childdiary.data.db.converters.AchievementTypeEnumConverter;
import ru.android.childdiary.data.db.converters.BreastEnumConverter;
import ru.android.childdiary.data.db.converters.DiaperStateEnumConverter;
import ru.android.childdiary.data.db.converters.EventTypeEnumConverter;
import ru.android.childdiary.data.db.converters.FeedTypeEnumConverter;
import ru.android.childdiary.data.db.converters.JodaDateTimeConverter;
import ru.android.childdiary.data.db.converters.JodaLocalDateConverter;
import ru.android.childdiary.data.db.converters.JodaLocalTimeConverter;
import ru.android.childdiary.data.db.converters.LengthValueConverter;
import ru.android.childdiary.data.db.converters.LinearGroupsConverter;
import ru.android.childdiary.data.db.converters.PeriodicityTypeEnumConverter;
import ru.android.childdiary.data.db.converters.SexEnumConverter;
import ru.android.childdiary.data.db.converters.TestTypeEnumConverter;
import ru.android.childdiary.data.db.converters.TimeUnitEnumConverter;

public class CustomMapping extends DefaultMapping {
    public CustomMapping(Platform platform) {
        super(platform);
        addConverter(new AchievementTypeEnumConverter());
        addConverter(new BreastEnumConverter());
        addConverter(new DiaperStateEnumConverter());
        addConverter(new EventTypeEnumConverter());
        addConverter(new FeedTypeEnumConverter());
        addConverter(new JodaDateTimeConverter());
        addConverter(new JodaLocalDateConverter());
        addConverter(new JodaLocalTimeConverter());
        addConverter(new LengthValueConverter());
        addConverter(new LinearGroupsConverter());
        addConverter(new PeriodicityTypeEnumConverter());
        addConverter(new SexEnumConverter());
        addConverter(new TestTypeEnumConverter());
        addConverter(new TimeUnitEnumConverter());
    }
}
