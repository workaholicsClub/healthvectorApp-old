package ru.android.childdiary.data.entities.calendar.events;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.OneToOne;
import io.requery.Persistable;
import io.requery.ReferentialAction;
import io.requery.Table;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.exercises.ConcreteExerciseData;

@Table(name = "exercise_event")
@Entity(name = "ExerciseEventEntity")
public interface ExerciseEventData extends Persistable {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @OneToOne
    MasterEventData getMasterEvent();

    @ForeignKey(delete = ReferentialAction.RESTRICT)
    @ManyToOne
    ConcreteExerciseData getConcreteExercise();

    String getName();

    Integer getDurationInMinutes();

    String getImageFileName();
}
