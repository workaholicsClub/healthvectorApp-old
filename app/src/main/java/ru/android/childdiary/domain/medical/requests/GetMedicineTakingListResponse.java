package ru.android.childdiary.domain.medical.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.medical.data.MedicineTaking;

@Value
@Builder
public class GetMedicineTakingListResponse {
    @NonNull
    GetMedicineTakingListRequest request;
    @NonNull
    List<MedicineTaking> medicineTakingList;
}
