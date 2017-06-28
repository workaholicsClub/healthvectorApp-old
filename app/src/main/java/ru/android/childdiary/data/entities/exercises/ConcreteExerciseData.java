package ru.android.childdiary.data.entities.exercises;

import org.joda.time.DateTime;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.OneToOne;
import io.requery.Persistable;
import io.requery.ReferentialAction;
import io.requery.Table;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.core.RepeatParametersData;

@Table(name = "concrete_exercise")
@Entity(name = "ConcreteExerciseEntity")
public interface ConcreteExerciseData extends Persistable {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @ManyToOne
    ChildData getChild();

    @ForeignKey(delete = ReferentialAction.RESTRICT)
    @ManyToOne
    ExerciseData getExercise();

    @ForeignKey(delete = ReferentialAction.RESTRICT)
    @OneToOne
    RepeatParametersData getRepeatParameters();

    String getName();

    Integer getDurationInMinutes();

    DateTime getDateTime();

    DateTime getFinishDateTime();

    Boolean isExported();

    Integer getNotifyTimeInMinutes();

    String getNote();

    String getImageFileName();

    Boolean isDeleted();
}
