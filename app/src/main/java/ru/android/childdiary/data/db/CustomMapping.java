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
import ru.android.childdiary.data.converters.SexEnumConverter;

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
        addConverter(new SexEnumConverter());
    }
}
