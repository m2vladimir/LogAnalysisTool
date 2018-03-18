package com.kyriba.parser.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Key that helps to aggregate analysis statistic.
 *
 * @author VMyakushin
 */
public class GroupingKey {
    private Map<GroupBy, String> m_conditions;

    public GroupingKey() {
        m_conditions = new HashMap<>();
    }

    /**
     * Add grouping condition and value of the condition.
     *
     * @param condition group by.
     * @param value     condition value.
     */
    public void add(final GroupBy condition, final String value) {
        m_conditions.put(condition, value);
    }

    /**
     * @return {@code true} if key doesn't contains any data.
     */
    public boolean isEmpty() {
        return m_conditions.isEmpty();
    }

    /**
     * Get value of specified grouping condition.
     *
     * @param key condition.
     * @return value or {@code null} if the grouping condition wasn't added before.
     */
    public String getConditionValue(final GroupBy key) {
        return m_conditions.get(key);
    }


    /**
     * Compares this key to the specified object.
     * The result is true if and only if the argument is not null
     * and is a {@link GroupingKey Key} object that represents the same grouping conditions and the same values of these conditions.
     *
     * @param o object to compare this key against.
     * @return {@code true} if keys are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupingKey)) return false;

        GroupingKey that = (GroupingKey) o;

        if (m_conditions.size() != that.m_conditions.size()) return false;

        for (GroupBy key : m_conditions.keySet()) {
            if (!(that.m_conditions.containsKey(key) &&
                    m_conditions.get(key).equals(that.m_conditions.get(key)))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        //TODO implement something less disturbed.
        return m_conditions != null ? m_conditions.hashCode() : 0;
    }
}
