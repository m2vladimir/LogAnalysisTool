package com.kyriba.parser.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Possible filter value.
 *
 * @author VMyakushin
 */
public enum Filter {
    USERNAME {
        @Override
        public void compile() {
            m_patternGroupName = toString().toLowerCase();
            m_pattern = Pattern.compile(Configuration.INSTANCE.getUsernamePattern());
        }

        @Override
        public boolean match(String currentValue, String filterValue) {
            return currentValue.equals(filterValue);
        }
    },
    DATE {
        private SimpleDateFormat m_dateFormat;

        @Override
        public void compile() {
            m_patternGroupName = toString().toLowerCase();
            m_pattern = Pattern.compile(Configuration.INSTANCE.getDatePattern());
            m_dateFormat = new SimpleDateFormat(Configuration.INSTANCE.getDateFormatString());
        }

        @Override
        public boolean match(String currentValue, String filterValue) {
            String[] filterRange = filterValue.split(Constants.FILTER_DATE_SEPARATOR);
            if (filterRange.length < 2) {
                return false;
            }
            Date filterDateFrom;
            Date filterDateTo;
            Date currentDate;
            try {
                filterDateFrom = m_dateFormat.parse(filterRange[0]);
                filterDateTo = m_dateFormat.parse(filterRange[1]);
                currentDate = m_dateFormat.parse(currentValue);
            } catch (ParseException e) {
                return false;
            }

            return currentDate.after(filterDateFrom) && currentDate.before(filterDateTo);
        }
    },
    MESSAGE {
        @Override
        public void compile() {
            m_patternGroupName = toString().toLowerCase();
            m_pattern = Pattern.compile(Configuration.INSTANCE.getMessagePattern());
        }

        @Override
        public boolean match(String currentValue, String filterValue) {
            return currentValue.contains(filterValue);
        }
    };

    protected String m_patternGroupName;
    protected Pattern m_pattern;

    /**
     * Compile patterns based on configuration for all filters.
     */
    public static void compileAll() {
        for (Filter filter : values()) {
            filter.compile();
        }
    }

    /**
     * Compile pattern based on configuration for the filter.
     */
    protected abstract void compile();

    /**
     * Match values according to the filter rules.
     *
     * @param currentValue value parsed from line according to filter pattern.
     * @param filterValue  user defined filter value.
     * @return true if values are match.
     */
    public abstract boolean match(final String currentValue, final String filterValue);

    /**
     * @return group name for {@link Matcher#group(String)}.
     */
    public String getPatternGroupName() {
        return m_patternGroupName;
    }

    /**
     * Must be compiled before get.
     *
     * @return pattern based on configuration for the filter or {@code null} if pattern wasn't compiled.
     */
    public Pattern getPattern() {
        return m_pattern;
    }
}
