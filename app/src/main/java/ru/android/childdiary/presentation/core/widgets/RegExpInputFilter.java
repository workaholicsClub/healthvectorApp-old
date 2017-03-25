package ru.android.childdiary.presentation.core.widgets;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegExpInputFilter implements InputFilter {
    private final Pattern pattern;

    public RegExpInputFilter() {
        pattern = getPattern();
    }

    protected abstract Pattern getPattern();

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder sb = new StringBuilder();
        sb.append(dest.subSequence(0, dstart));
        sb.append(source.subSequence(start, end));
        sb.append(dest.subSequence(dend, dest.length()));

        Matcher matcher = pattern.matcher(sb);
        boolean matches = matcher.matches();

        return matches ? null : "";
    }

    public static class HeightInputFilter extends RegExpInputFilter {
        @Override
        protected Pattern getPattern() {
            return Pattern.compile("\\d{0,2}(\\.\\d?)?(\\s\\w+)?");
        }
    }

    public static class WeightInputFilter extends RegExpInputFilter {
        @Override
        protected Pattern getPattern() {
            return Pattern.compile("\\d?(\\.\\d{0,3})?(\\s\\w+)?");
        }
    }

    public static class AmountInputFilter extends RegExpInputFilter {
        @Override
        protected Pattern getPattern() {
            return Pattern.compile("\\d{0,3}(\\.\\d?)?");
        }
    }

    public static class AmountMlInputFilter extends RegExpInputFilter {
        @Override
        protected Pattern getPattern() {
            return Pattern.compile("\\d{0,3}(\\.\\d?)?(\\s\\w+)?");
        }
    }
}
