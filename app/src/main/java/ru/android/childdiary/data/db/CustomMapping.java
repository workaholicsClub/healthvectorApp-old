package ru.android.childdiary.data.db;

import io.requery.android.DefaultMapping;
import io.requery.sql.Platform;
import ru.android.childdiary.data.converters.BreastEnumConverter;
import ru.android.childdiary.data.converters.DiaperStateEnumConverter;
import ru.android.childdiary.data.converters.EventTypeEnumConverter;
import ru.android.childdiary.data.converters.FeedTypeEnumConverter;
import ru.android.childdiary.data.converters.JodaDateTimeConverter;
import ru.android.childdiary.data.converters.JodaLocalDateConverter;
import ru.android.childdiary.data.converters.JodaLocalTimeConverter;
import ru.android.childdiary.data.converters.LengthValueConverter;
import ru.android.childdiary.data.converters.LinearGroupsConverter;
import ru.android.childdiary.data.converters.PeriodicityTypeEnumConverter;
import ru.android.childdiary.data.converters.SexEnumConverter;
import ru.android.childdiary.data.converters.TimeUnitEnumConverter;

public class CustomMapping extends DefaultMapping {
    public CustomMapping(Platform platform) {
        super(platform);
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
        addConverter(new TimeUnitEnumConverter());
    }
}
