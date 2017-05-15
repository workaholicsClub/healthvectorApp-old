package ru.android.childdiary.domain.interactors.calendar.requests;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeleteEventsResponse {
    DeleteEventsRequest request;
    Integer count;
}
