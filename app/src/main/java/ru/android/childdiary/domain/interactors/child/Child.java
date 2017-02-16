package ru.android.childdiary.domain.interactors.child;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.Sex;

@Value
@Builder
public class Child implements Parcelable {
    public static final Parcelable.Creator<Child> CREATOR = new Parcelable.Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel source) {
            return new Child(source);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };

    long id;

    String name;

    LocalDate birthDate;

    LocalTime birthTime;

    Sex sex;

    String imageFileName;

    double height;

    double weight;

    public Child(long id, String name, LocalDate birthDate, LocalTime birthTime, Sex sex, String imageFileName, double height, double weight) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.birthTime = birthTime;
        this.sex = sex;
        this.imageFileName = imageFileName;
        this.height = height;
        this.weight = weight;
    }

    protected Child(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.birthDate = (LocalDate) in.readSerializable();
        this.birthTime = (LocalTime) in.readSerializable();
        int tmpSex = in.readInt();
        this.sex = tmpSex == -1 ? null : Sex.values()[tmpSex];
        this.imageFileName = in.readString();
        this.height = in.readDouble();
        this.weight = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeSerializable(this.birthDate);
        dest.writeSerializable(this.birthTime);
        dest.writeInt(this.sex == null ? -1 : this.sex.ordinal());
        dest.writeString(this.imageFileName);
        dest.writeDouble(this.height);
        dest.writeDouble(this.weight);
    }
}
