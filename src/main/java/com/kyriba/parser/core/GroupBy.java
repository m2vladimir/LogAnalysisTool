package com.kyriba.parser.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * Possible Group By conditions.
 *
 * @author VMyakushin
 */
public enum GroupBy {
    USERNAME {
        @Override
        public void processKey(String line, GroupingKey key) {
            Matcher matcher = Filter.USERNAME.getPattern().matcher(line);
            if (matcher.find()) {
                key.add(this, matcher.group(Filter.USERNAME.getPatternGroupName()));
            }
        }
    },
    YEAR {
        private final SimpleDateFormat resultFormat = new SimpleDateFormat("yyyy");

        @Override
        public void processKey(String line, GroupingKey key) {
            GroupBy.addTimeUnitToKey(line, key, resultFormat, this);
        }
    },
    MONTH {
        private final SimpleDateFormat resultFormat = new SimpleDateFormat("MMM");

        @Override
        public void processKey(String line, GroupingKey key) {
            GroupBy.addTimeUnitToKey(line, key, resultFormat, this);
        }
    },
    DAY {
        private final SimpleDateFormat resultFormat = new SimpleDateFormat("MM/dd/yyyy");

        @Override
        public void processKey(String line, GroupingKey key) {
            GroupBy.addTimeUnitToKey(line, key, resultFormat, this);
        }
    },
    HOUR {
        private final SimpleDateFormat resultFormat = new SimpleDateFormat("hh a");

        @Override
        public void processKey(String line, GroupingKey key) {
            GroupBy.addTimeUnitToKey(line, key, resultFormat, this);
        }
    };

    private static void addTimeUnitToKey(final String line, final GroupingKey key, final SimpleDateFormat resultFormat, final GroupBy groupBy) {
        DateFormat parserDateFormat = Configuration.INSTANCE.getDateFormat();
        Matcher matcher = Filter.DATE.getPattern().matcher(line);
        if (matcher.find()) {
            String dateString = matcher.group(Filter.DATE.getPatternGroupName());
            try {
                Date date = parserDateFormat.parse(dateString);
                dateString = resultFormat.format(date);
                key.add(groupBy, dateString);
            } catch (ParseException e) {
                //nothing. wrong date, skip it.
            }
        }
    }

    /**
     * Method calculates some value from given line and add this value to key.
     *
     * @param line text line.
     * @param key  key that helps to aggregate statistic for given line.
     */
    public abstract void processKey(String line, GroupingKey key);
}
