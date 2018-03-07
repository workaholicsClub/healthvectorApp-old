package ru.android.healthvector.data.network.dto;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@Root(name = "Программа")
public class Program {
    private static final String LANGUAGE_EN = "en", LANGUAGE_RU = "ru";

    @Attribute(name = "lang")
    String languageCode;

    @Element(name = "ИД")
    Long serverId;

    @Element(name = "Код", required = false)
    String code;

    @Element(name = "Наименование")
    String name;

    @Element(name = "Краткое_описание")
    String description;

    public boolean isEn() {
        return LANGUAGE_EN.equals(languageCode);
    }

    public boolean isRu() {
        return LANGUAGE_RU.equals(languageCode);
    }
}
