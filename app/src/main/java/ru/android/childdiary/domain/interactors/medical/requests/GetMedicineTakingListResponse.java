package ru.android.childdiary.domain.interactors.medical.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.data.MedicineTaking;

@Value
@Builder
public class GetMedicineTakingListResponse {
    @NonNull
    GetMedicineTakingListRequest request;
    @NonNull
    List<MedicineTaking> medicineTakingList;
}
