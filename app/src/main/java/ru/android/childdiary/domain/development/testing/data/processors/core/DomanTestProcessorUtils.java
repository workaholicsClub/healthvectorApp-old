package ru.android.childdiary.domain.development.testing.data.processors.core;

import lombok.AllArgsConstructor;
import lombok.Value;

class DomanTestProcessorUtils {
    static final Range[] ADVANCED_RANGES = new Range[]{
            new Range(0.25, 0.75),
            new Range(0.625, 1.875),
            new Range(1.75, 5.25),
            new Range(3, 9),
            new Range(4.50, 13.5),
            new Range(9, 27),
            new Range(18, 54)
    };
    static final Range[] NORMAL_RANGES = new Range[]{
            new Range(0.75, 1.50),
            new Range(1.875, 3.75),
            new Range(5.25, 10.5),
            new Range(9, 18),
            new Range(13.5, 27),
            new Range(27, 54),
            new Range(54, 108)
    };
    static final Range[] SLOW_RANGES = new Range[]{
            new Range(1.5, 2.5),
            new Range(3.75, 6.25),
            new Range(10.5, 17.5),
            new Range(18, 30),
            new Range(27, 45),
            new Range(54, 90),
            new Range(108, 180)
    };
    private static final double[] AVERAGES = new double[]{
            1, 2.5, 7, 12, 18, 36, 72
    };

    static int getIndex(double months) {
        int length = AVERAGES.length;
        int last = length - 1;

        if (months <= AVERAGES[0]) {
            return 0;
        }
        if (months >= AVERAGES[last]) {
            return last;
        }

        for (int i = 0; i < last; ++i) {
            double current = AVERAGES[i];
            double next = AVERAGES[i + 1];
            if (current <= months && months <= next) {
                double diff1 = months - current;
                double diff2 = next - months;
                return diff1 < diff2 ? i : i + 1;
            }
        }

        throw new IllegalStateException("Invalid index");
    }

    @Value
    @AllArgsConstructor(suppressConstructorProperties = true)
    static class Range {
        double from, to;

        public boolean contains(double value) {
            return from <= value && value < to;
        }
    }
}
