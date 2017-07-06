package ru.android.childdiary.data.network.dto;

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
    @Attribute(name = "lang")
    String languageCode;

    @Element(name = "ИД")
    Long serverId;

    @Element(name = "Код")
    String code;

    @Element(name = "Наименование")
    String name;

    @Element(name = "Краткое_описание")
    String description;
}
