package ru.android.childdiary.data.dto;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@Root(name = "Программы")
public class Programs {
    @ElementList(inline = true)
    List<Program> programs;
}
