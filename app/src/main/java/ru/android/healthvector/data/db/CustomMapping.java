package ru.android.healthvector.data.db;

import io.requery.android.DefaultMapping;
import io.requery.sql.Platform;
import ru.android.healthvector.data.db.converters.AchievementTypeEnumConverter;
import ru.android.healthvector.data.db.converters.BreastEnumConverter;
import ru.android.healthvector.data.db.converters.DiaperStateEnumConverter;
import ru.android.healthvector.data.db.converters.EventTypeEnumConverter;
import ru.android.healthvector.data.db.converters.FeedTypeEnumConverter;
import ru.android.healthvector.data.db.converters.JodaDateTimeConverter;
import ru.android.healthvector.data.db.converters.JodaLocalDateConverter;
import ru.android.healthvector.data.db.converters.JodaLocalTimeConverter;
import ru.android.healthvector.data.db.converters.LengthValueConverter;
import ru.android.healthvector.data.db.converters.LinearGroupsConverter;
import ru.android.healthvector.data.db.converters.PeriodicityTypeEnumConverter;
import ru.android.healthvector.data.db.converters.SexEnumConverter;
import ru.android.healthvector.data.db.converters.TestTypeEnumConverter;
import ru.android.healthvector.data.db.converters.TimeUnitEnumConverter;

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
