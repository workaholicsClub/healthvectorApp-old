package ru.android.healthvector.data.repositories.exercises;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.android.healthvector.data.network.dto.Programs;

public interface ExerciseNetworkService {
    @GET("/get_proggrams.php")
    Observable<Programs> getPrograms();
}
