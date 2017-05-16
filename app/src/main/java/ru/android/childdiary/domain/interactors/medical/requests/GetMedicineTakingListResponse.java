package ru.android.childdiary.domain.interactors.medical.requests;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;

@Value
@Builder
public class GetMedicineTakingListResponse {
    GetMedicineTakingListRequest request;
    List<MedicineTaking> medicineTakingList;
}
