package ru.android.childdiary.domain.development.testing.data.interpreters.core;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.List;

import io.reactivex.Observable;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.development.testing.data.processors.core.DomanTestProcessor;
import ru.android.childdiary.domain.development.testing.data.tests.core.DomanTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class DomanTestInterpreter<T extends DomanTest, TP extends DomanTestProcessor<T>>
        extends BaseTestInterpreter<T, TP> {
    private final List<String> stageTitles, stageDescriptions;

    protected DomanTestInterpreter(Context context,
                                   @NonNull TP testProcessor) {
        super(context, testProcessor);
        stageTitles = getStageTitles();
        stageDescriptions = getStageDescriptions();
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
        int stage = testProcessor.getResult();
        return String.format(format,
                getStageTitle(stage),
                getStageDescription(stage),
                getStageType(testProcessor.getInitialStage(), stage, testProcessor.getDomanDate()));
    }

    @Override
    public String interpretShort() {
        int stage = testProcessor.getResult();
        return getStageTitle(stage);
    }

    private String getStageType(int initialStage, int stage, @Nullable LocalDate domanDate) {
        if (domanDate == null) {
            return context.getString(R.string.doman_stage_slow);
        }
        if (initialStage == stage) {
            return context.getString(R.string.doman_stage_normal);
        } else if (initialStage < stage) {
            return context.getString(R.string.doman_stage_advanced);
        }
        return context.getString(R.string.doman_stage_slow);
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
