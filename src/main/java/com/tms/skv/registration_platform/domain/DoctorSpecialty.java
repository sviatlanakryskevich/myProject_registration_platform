package com.tms.skv.registration_platform.domain;

public enum DoctorSpecialty {
    THERAPIST("Терапевт"),
    SURGEON("Хирург"),
    CARDIOLOGIST("Кардиолог"),
    OPHTHALMOLOGIST("Офтальмолог"),
    ENT_SPECIALIST("Оторинолариголог"),
    ENDOCRINOLOGIST("Эндокринолог"),
    ORTHOPEDIST("Ортопед"),
    NEUROLOGIST("Невролог"),
    GYNECOLOGIST("Гинеколог"),
    UROLOGIST("Уролог"),
    PEDIATRICIAN("Педиатр"),
    ONCOLOGIST("Онколог");


    private final String russianName;

    DoctorSpecialty(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }
}
