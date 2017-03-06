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

    Double birthHeight;

    Double birthWeight;

    private Child(Long id, String name, LocalDate birthDate, LocalTime birthTime, Sex sex, String imageFileName, Double birthHeight, Double birthWeight) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.birthTime = birthTime;
        this.sex = sex;
        this.imageFileName = imageFileName;
        this.birthHeight = birthHeight;
        this.birthWeight = birthWeight;
    }

    protected Child(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.birthDate = (LocalDate) in.readSerializable();
        this.birthTime = (LocalTime) in.readSerializable();
        int tmpSex = in.readInt();
        this.sex = tmpSex == -1 ? null : Sex.values()[tmpSex];
        this.imageFileName = in.readString();
        this.birthHeight = (Double) in.readValue(Double.class.getClassLoader());
        this.birthWeight = (Double) in.readValue(Double.class.getClassLoader());
    }

    public Child.ChildBuilder getBuilder() {
        return Child.builder()
                .id(id)
                .name(name)
                .birthDate(birthDate)
                .birthTime(birthTime)
                .sex(sex)
                .imageFileName(imageFileName)
                .birthHeight(birthHeight)
                .birthWeight(birthWeight);
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
        dest.writeValue(this.birthHeight);
        dest.writeValue(this.birthWeight);
    }
}
