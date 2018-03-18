package com.kyriba.parser.core;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Results of analysis.
 * <p>
 * <p>Encapsulates analysis statistic.</p>
 *
 * @author VMyakushin
 */
public class ParsingResults {
    private Map<GroupingKey, BigInteger> m_statistic;
    private BigInteger linesCount;

    public ParsingResults() {
        m_statistic = new HashMap<>();
        linesCount = BigInteger.valueOf(0);
    }

    /**
     * Increment statistic for data grouped by input parameters with the same values.
     *
     * @param key grouping parameters with specified values.
     */
    public void add(final GroupingKey key) {
        if (m_statistic.putIfAbsent(key, BigInteger.ONE) != null) {
            m_statistic.put(key, m_statistic.get(key).add(BigInteger.ONE));
        }
        linesCount = linesCount.add(BigInteger.ONE);
    }

    /**
     * Get statistic for data grouped by grouping input parameters.
     *
     * @return analysis statistic.
     */
    public Map<GroupingKey, BigInteger> getStatistic() {
        return m_statistic;
    }

    /**
     * Number of lines that was filtered during analysis.
     *
     * @return number of filtered lines.
     */
    public BigInteger countFilteredLines() {
        return linesCount;
    }
}
