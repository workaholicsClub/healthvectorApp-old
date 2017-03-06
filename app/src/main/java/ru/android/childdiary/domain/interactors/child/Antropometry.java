package ru.android.childdiary.domain.interactors.child;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Antropometry implements Parcelable {
    public static final Antropometry NULL = Antropometry.builder().build();

    public static final Parcelable.Creator<Antropometry> CREATOR = new Parcelable.Creator<Antropometry>() {
        @Override
        public Antropometry createFromParcel(Parcel source) {
            return new Antropometry(source);
        }

        @Override
        public Antropometry[] newArray(int size) {
            return new Antropometry[size];
        }
    };

    Long id;

    Double height;

    Double weight;

    LocalDate date;

    private Antropometry(Long id, Double height, Double weight, LocalDate date) {
        this.id = id;
        this.height = height;
        this.weight = weight;
        this.date = date;
    }

    protected Antropometry(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.height = (Double) in.readValue(Double.class.getClassLoader());
        this.weight = (Double) in.readValue(Double.class.getClassLoader());
        this.date = (LocalDate) in.readSerializable();
    }

    public Antropometry.AntropometryBuilder getBuilder() {
        return Antropometry.builder()
                .id(id)
                .height(height)
                .weight(weight)
                .date(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.height);
        dest.writeValue(this.weight);
        dest.writeSerializable(this.date);
    }
}
