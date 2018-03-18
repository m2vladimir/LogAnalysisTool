package com.kyriba.parser.core;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Options defined by user input.
 * Provides validity checks for input values.
 *
 * @author VMyakushin
 */
public class ParseOptions {
    private Map<Filter, String> m_filters;
    private Set<GroupBy> m_groups;
    private File m_inputFile;
    private File m_outputFile;
    private boolean filterAccepted;
    private boolean groupConditionAccepted;

    public ParseOptions() {
        m_filters = new HashMap<>();
        m_groups = new TreeSet<>();
        filterAccepted = false;
        groupConditionAccepted = false;
    }

    /**
     * Add new grouping condition.
     * At least one condition should be added for successful validity check.
     *
     * @param condition group by.
     */
    public void addGroupingCondition(GroupBy condition) {
        m_groups.add(condition);
        groupConditionAccepted = true;
    }

    /**
     * @return grouping conditions.
     */
    public Set<GroupBy> getGroupingConditions() {
        return m_groups;
    }

    /**
     * @return filters based on user input.
     */
    public Set<Filter> getFilters() {
        return m_filters.keySet();
    }

    /**
     * Get value of specified filter.
     *
     * @param filter as key in map.
     * @return value of the filter.
     */
    public String getFilterValue(Filter filter) {
        return m_filters.get(filter);
    }

    /**
     * Creates USERNAME filter with specified value.
     *
     * @param filterValue username.
     * @return validity check result.
     */
    public ResponseMessage setUsernameFilter(String filterValue) {
        m_filters.put(Filter.USERNAME, filterValue);
        filterAccepted = true;
        return ResponseMessage.SUCCESS;
    }

    /**
     * Creates DATE filter with specified value. Dates must match specified date format or validity check will fail.
     *
     * @param filterDateFrom from when. Must be early than dateTo parameter.
     * @param filterDateTo   to when. Must be later than dateFrom parameter.
     * @param format         date format.
     * @return validity check result.
     */
    public ResponseMessage setDateFilter(String filterDateFrom, String filterDateTo, DateFormat format) {
        ResponseMessage message;
        try {
            Date dateFrom = format.parse(filterDateFrom);
            Date dateTo = format.parse(filterDateTo);
            if (dateFrom.after(dateTo)) {
                message = ResponseMessage.WRONG_DATE_RANGE;
            } else {
                String filterValue = filterDateFrom + Constants.FILTER_DATE_SEPARATOR + filterDateTo;
                m_filters.put(Filter.DATE, filterValue);
                message = ResponseMessage.SUCCESS;
                filterAccepted = true;
            }
        } catch (ParseException e) {
            message = ResponseMessage.WRONG_DATE_FORMAT;
        }

        return message;
    }

    /**
     * Creates MESSAGE filter with specified value.
     * This filter checks only that parsed message {@link String#contains(CharSequence) contains} specified text.
     *
     * @param filterValue message
     * @return validity check result.
     */
    public ResponseMessage setMessageFilter(String filterValue) {
        m_filters.put(Filter.MESSAGE, filterValue);
        filterAccepted = true;
        return ResponseMessage.SUCCESS;
    }

    /**
     * Set input path.
     * Cannot be empty string or validity will fail.
     *
     * @param inputPath path for existing single text file or directory with files.
     * @return validity check result.
     */
    public ResponseMessage setInputPath(String inputPath) {
        ResponseMessage message;
        m_inputFile = new File(inputPath);
        if (!m_inputFile.exists()) {
            m_inputFile = null;
            message = ResponseMessage.WRONG_INPUT_PATH;
        } else {
            message = ResponseMessage.SUCCESS;
        }

        return message;
    }

    /**
     * Set path to output file.
     * The file and parent directories will be created if it isn't exist.
     *
     * @param outputPath output file path.
     * @return validity check result.
     */
    public ResponseMessage setOutputPath(String outputPath) {
        ResponseMessage message;
        m_outputFile = new File(outputPath);
        if (!m_outputFile.exists()) {
            m_outputFile.getParentFile().mkdirs();
            try {
                m_outputFile.createNewFile();
                message = ResponseMessage.SUCCESS;
            } catch (IOException e) {
                message = ResponseMessage.WRONG_OUTPUT_PATH;
            }
        } else {
            if (m_outputFile.isDirectory()) {
                message = ResponseMessage.OUTPUT_FILE_CANNOT_BE_DIRECTORY;
            } else {
                message = ResponseMessage.SUCCESS;
            }
        }

        return message;
    }

    /**
     * Must be set before.
     *
     * @return file or directory with log for analysis.
     */
    public File getInputFile() {
        return m_inputFile;
    }

    /**
     * Must be set before.
     *
     * @return output file for record.
     */
    public File getOutputFile() {
        return m_outputFile;
    }

    /**
     * Check if current filter settings can be useful for log analysis.
     *
     * @return validity check result.
     */
    public ResponseMessage checkFilterValidity() {
        return filterAccepted ? ResponseMessage.SUCCESS : ResponseMessage.FILTER_IS_NOT_VALID;
    }

    /**
     * Check if current "group by" settings can be useful for log analysis.
     *
     * @return validity check result.
     */
    public ResponseMessage checkGroupingValidity() {
        return groupConditionAccepted ? ResponseMessage.SUCCESS : ResponseMessage.GROUPING_IS_NOT_VALID;
    }
}
