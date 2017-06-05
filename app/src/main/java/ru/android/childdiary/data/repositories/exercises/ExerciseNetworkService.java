package ru.android.childdiary.data.repositories.exercises;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.android.childdiary.data.dto.Programs;

public interface ExerciseNetworkService {
    @GET("/get_proggrams.php")
    Observable<Programs> getPrograms();
}
