package ru.android.childdiary.domain.interactors.child;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.Sex;

@Value
@Builder
public class Child implements Parcelable {
    public static final Child NULL = Child.builder().build();

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

    Long id;

    String name;

    LocalDate birthDate;

    // необязательный параметр
    LocalTime birthTime;

    Sex sex;

    // необязательный параметр
    String imageFileName;

    Double height;

    Double weight;

    private Child(Long id, String name, LocalDate birthDate, LocalTime birthTime, Sex sex, String imageFileName, Double height, Double weight) {
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
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.birthDate = (LocalDate) in.readSerializable();
        this.birthTime = (LocalTime) in.readSerializable();
        int tmpSex = in.readInt();
        this.sex = tmpSex == -1 ? null : Sex.values()[tmpSex];
        this.imageFileName = in.readString();
        this.height = (Double) in.readValue(Double.class.getClassLoader());
        this.weight = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static Child.ChildBuilder getBuilder(@Nullable Child child) {
        return child == null ? Child.builder() : Child.builder()
                .id(child.id)
                .name(child.name)
                .birthDate(child.birthDate)
                .birthTime(child.birthTime)
                .sex(child.sex)
                .imageFileName(child.imageFileName)
                .height(child.height)
                .weight(child.weight);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeSerializable(this.birthDate);
        dest.writeSerializable(this.birthTime);
        dest.writeInt(this.sex == null ? -1 : this.sex.ordinal());
        dest.writeString(this.imageFileName);
        dest.writeValue(this.height);
        dest.writeValue(this.weight);
    }
}
