package ru.android.childdiary.domain.interactors.child;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Antropometry implements Parcelable {
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

    long id;

    Child child;

    double height;

    double weight;

    LocalDate date;

    public Antropometry(long id, Child child, double height, double weight, LocalDate date) {
        this.id = id;
        this.child = child;
        this.height = height;
        this.weight = weight;
        this.date = date;
    }

    protected Antropometry(Parcel in) {
        this.id = in.readLong();
        this.child = in.readParcelable(Child.class.getClassLoader());
        this.height = in.readDouble();
        this.weight = in.readDouble();
        this.date = (LocalDate) in.readSerializable();
    }

    public static Antropometry.AntropometryBuilder getBuilder(Antropometry antropometry) {
        return antropometry == null ? Antropometry.builder() : Antropometry.builder()
                .id(antropometry.getId())
                .child(antropometry.getChild())
                .height(antropometry.getHeight())
                .weight(antropometry.getWeight())
                .date(antropometry.getDate());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeParcelable(this.child, flags);
        dest.writeDouble(this.height);
        dest.writeDouble(this.weight);
        dest.writeSerializable(this.date);
    }
}
