package ru.android.childdiary.presentation.core.widgets;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegExpInputFilter implements InputFilter {
    protected final Pattern pattern;

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
        private static boolean contains(CharSequence s, char ch) {
            for (int i = 0; i < s.length(); ++i) {
                if (s.charAt(i) == ch) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected Pattern getPattern() {
            return Pattern.compile("\\d?(\\.\\d{0,3})?(\\s\\w+)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            StringBuilder sb = new StringBuilder();
            CharSequence head = dest.subSequence(0, dstart);
            sb.append(head);
            CharSequence replacement = source.subSequence(start, end);
            sb.append(replacement);
            CharSequence tail = dest.subSequence(dend, dest.length());
            sb.append(tail);

            // уже введена одна цифра И вводим/вставляем из буфера еще несколько цифр
            if (head.length() == 1 && replacement.length() > 0) {
                // добавляем точку
                return tail.length() > 0 && tail.charAt(0) == '.' ? "" : "." + replacement;
            }

            // удаляем середину, содержащую точку
            CharSequence middle = dest.subSequence(dstart, dend);
            if (replacement.length() == 0 && contains(middle, '.')) {
                // нельзя удалить точку, если останется более одной цифры
                return head.length() + tail.length() > 1 ? "." : "";
            }

            Matcher matcher = pattern.matcher(sb);
            boolean matches = matcher.matches();

            return matches ? null : "";
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
