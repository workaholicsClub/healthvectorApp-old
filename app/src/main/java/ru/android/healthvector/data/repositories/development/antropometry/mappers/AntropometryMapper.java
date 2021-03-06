package ru.android.healthvector.data.repositories.development.antropometry.mappers;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.requery.BlockingEntityStore;
import ru.android.healthvector.data.db.entities.child.ChildData;
import ru.android.healthvector.data.db.entities.child.ChildEntity;
import ru.android.healthvector.data.db.entities.development.AntropometryData;
import ru.android.healthvector.data.db.entities.development.AntropometryEntity;
import ru.android.healthvector.data.repositories.child.mappers.ChildMapper;
import ru.android.healthvector.data.repositories.core.mappers.EntityMapper;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.antropometry.data.Antropometry;

public class AntropometryMapper implements EntityMapper<AntropometryData, AntropometryEntity, Antropometry> {
    private final ChildMapper childMapper;

    @Inject
    public AntropometryMapper(ChildMapper childMapper) {
        this.childMapper = childMapper;
    }

    @Override
    public Antropometry mapToPlainObject(@NonNull AntropometryData antropometryData) {
        ChildData childData = antropometryData.getChild();
        Child child = childData == null ? Child.NULL : childMapper.mapToPlainObject(childData);
        return Antropometry.builder()
                .id(antropometryData.getId())
                .child(child)
                .height(antropometryData.getHeight())
                .weight(antropometryData.getWeight())
                .date(antropometryData.getAntropometryDate())
                .build();
    }

    @Override
    public AntropometryEntity mapToEntity(BlockingEntityStore blockingEntityStore,
                                          @NonNull Antropometry antropometry) {
        AntropometryEntity antropometryEntity;
        if (antropometry.getId() == null) {
            antropometryEntity = new AntropometryEntity();
        } else {
            antropometryEntity = (AntropometryEntity) blockingEntityStore.findByKey(AntropometryEntity.class, antropometry.getId());
        }
        fillNonReferencedFields(antropometryEntity, antropometry);
        Child child = antropometry.getChild();
        if (child != null) {
            ChildEntity childEntity = (ChildEntity) blockingEntityStore.findByKey(ChildEntity.class, child.getId());
            antropometryEntity.setChild(childEntity);
        }
        return antropometryEntity;
    }

    @Override
    public void fillNonReferencedFields(@NonNull AntropometryEntity to, @NonNull Antropometry from) {
        to.setHeight(from.getHeight());
        to.setWeight(from.getWeight());
        to.setAntropometryDate(from.getDate());
    }
}
