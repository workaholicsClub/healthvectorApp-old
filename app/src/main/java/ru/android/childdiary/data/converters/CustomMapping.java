package ru.android.childdiary.data.converters;

import io.requery.android.DefaultMapping;
import io.requery.sql.Platform;

public class CustomMapping extends DefaultMapping {
    public CustomMapping(Platform platform) {
        super(platform);
        addConverter(new JodaLocalDateConverter());
        addConverter(new JodaLocalTimeConverter());
        addConverter(new SexEnumConverter());
    }
}
