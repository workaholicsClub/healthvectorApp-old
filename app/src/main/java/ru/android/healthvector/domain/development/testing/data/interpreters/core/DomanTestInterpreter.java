package ru.android.healthvector.domain.development.testing.data.interpreters.core;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.development.testing.data.processors.core.DomanResult;
import ru.android.healthvector.domain.development.testing.data.processors.core.DomanTestProcessor;
import ru.android.healthvector.domain.development.testing.data.tests.core.DomanTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class DomanTestInterpreter<T extends DomanTest, TP extends DomanTestProcessor<T>>
        extends BaseTestInterpreter<T, TP> {
    private final List<String> stageTitles, stageDescriptions;
    private final DomanResult domanResult;

    protected DomanTestInterpreter(Context context,
                                   @NonNull TP testProcessor) {
        super(context, testProcessor);
        stageTitles = getStageTitles();
        stageDescriptions = getStageDescriptions();
        domanResult = testProcessor.getDomanResult();
    }

    private List<String> getStageTitles() {
        return Observable.range(1, 7)
                .map(i -> context.getString(R.string.stage, i))
                .toList()
                .blockingGet();
    }

    private List<String> getStageDescriptions() {
        return getStrings(R.array.stage_descriptions);
    }

    private List<String> getStrings(@ArrayRes int id) {
        return Observable.fromArray(context.getResources().getStringArray(id))
                .toList()
                .blockingGet();
    }

    @Override
    public String interpret() {
        String format = context.getString(R.string.doman_result_text_format);
        int stage = domanResult.getStage();
        return String.format(format,
                getStageTitle(stage),
                getStageDescription(stage),
                getStageType(domanResult.getStageType()));
    }

    @Override
    public String interpretShort() {
        int stage = domanResult.getStage();
        return getStageTitle(stage);
    }

    private String getStageType(@Nullable DomanResult.StageType stageType) {
        if (stageType == null) {
            return context.getString(R.string.doman_stage_slow);
        }
        switch (stageType) {
            case ADVANCED:
                return context.getString(R.string.doman_stage_advanced);
            case NORMAL:
                return context.getString(R.string.doman_stage_normal);
            case SLOW:
                return context.getString(R.string.doman_stage_slow);
        }
        throw new IllegalStateException("Unsupported stage type");
    }

    @Nullable
    private String getStageTitle(int stage) {
        int index = stage;
        if (index >= 0 && index < stageTitles.size()) {
            return stageTitles.get(index);
        }
        return null;
    }

    @Nullable
    private String getStageDescription(int stage) {
        int index = stage;
        if (index >= 0 && index < stageDescriptions.size()) {
            return stageDescriptions.get(index);
        }
        return null;
    }
}
