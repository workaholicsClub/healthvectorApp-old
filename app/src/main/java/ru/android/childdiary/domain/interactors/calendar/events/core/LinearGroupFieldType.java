package ru.android.childdiary.domain.interactors.calendar.events.core;

public enum LinearGroupFieldType {
    // medcine taking event fields
    MEDICINE, AMOUNT, MEDICINE_MEASURE,
    // doctor visit event fields
    DOCTOR, DOCTOR_VISIT_EVENT_NAME, DOCTOR_VISIT_EVENT_DURATION_IN_MINUTES,
    // exercise event fields
    EXERCISE_EVENT_NAME, EXERCISE_EVENT_DURATION_IN_MINUTES,
    // master event fields
    NOTIFY_TIME_IN_MINUTES, TIME
}
