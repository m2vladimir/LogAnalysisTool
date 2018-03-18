package com.kyriba.parser.controller;

import com.kyriba.parser.core.*;

import java.io.*;
import java.math.BigInteger;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for any console that supports input\print streams.
 *
 * @author VMyakushin
 */
public class ConsoleController {
    private static final Logger logger = Logger.getLogger(ConsoleController.class.getName());
    private static final String TOTAL_FILTERED_MESSAGE = "Total records was filtered: %s%n";
    private static final String EXIT_MESSAGE = "Press ENTER to exit.";
    private static final String RESULT_TABLE_COLUMN = "%-15s%s";
    private static final String RESULT_TABLE_COUNT_COLUMN = "%-10s|";
    private final PrintStream m_printStream;
    private final InputStream m_inputStream;

    /**
     * Constructor.
     *
     * @param printStream output stream.
     * @param inputStream input stream.
     */
    public ConsoleController(final PrintStream printStream, final InputStream inputStream) {
        m_printStream = printStream;
        m_inputStream = inputStream;
    }

    /**
     * Allows user to input parameters for analysis and start log parser.
     * Print statistic when parsing is completed.
     */
    public void start() {
        ParseOptions options = new ParseOptions();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(m_inputStream))) {
            ResponseMessage outputFileMessage = options.setOutputPath(Configuration.INSTANCE.getOutputPath());
            if (outputFileMessage != ResponseMessage.SUCCESS) {
                m_printStream.println(outputFileMessage);
                throw new IOException(outputFileMessage.toString());
            }

            m_printStream.println(getStartingMessage());

            String text = "Specify a path to log files for analysis (file or directory):";
            inputPath(options, br, text);

            while (true) {
                text = "Specify USERNAME filter. Leave line empty to SKIP filter:";
                usernameFilter(options, br, text);
                text = "Specify DATE filter. Leave line empty to SKIP filter,";
                dateFilter(options, br, text);
                text = "Specify MESSAGE filter. Leave line empty to SKIP filter:";
                messageFilter(options, br, text);

                ResponseMessage message = options.checkFilterValidity();
                if (message == ResponseMessage.SUCCESS) {
                    break;
                } else {
                    m_printStream.println(message);
                }
            }

            StringBuilder groupingMessage = new StringBuilder();
            groupingMessage.append("Specify conditions for grouping. Several conditions can be separated by commas. Example, YEAR, DAY. Case insensitive.");
            groupingMessage.append(System.lineSeparator());
            groupingMessage.append("Possible values: ");
            for (GroupBy group : GroupBy.values()) {
                groupingMessage.append(group);
                groupingMessage.append(" ");
            }
            groupingMessage.append(System.lineSeparator());
            groupBy(options, br, groupingMessage.toString());

            logger.log(Level.INFO, "Analysis started with user defined parameters.");
            Parser parser = new ParserBasicImpl();
            ParsingResults results;
            results = parser.parse(options);
            logger.log(Level.INFO, "Analysis was completed successful. Number of filtered lines: " + results.countFilteredLines());

            printResults(results, options);
            m_printStream.println(EXIT_MESSAGE); //TODO remove after creating .bat file
            br.readLine();
        } catch (IllegalArgumentException e) {
            ResponseMessage message = ResponseMessage.WRONG_REGULAR_EXPRESSION;
            logger.log(Level.SEVERE, message.toString(), e);
            m_printStream.println(message);
        } catch (IOException e) {
            ResponseMessage message = ResponseMessage.UNKNOWN_ERROR;
            logger.log(Level.SEVERE, message.toString(), e);
            m_printStream.println(message);
        }
    }

    private void printResults(ParsingResults results, ParseOptions options) {
        m_printStream.println();
        m_printStream.printf(TOTAL_FILTERED_MESSAGE, results.countFilteredLines());

        for (GroupBy group : options.getGroupingConditions()) {
            m_printStream.printf(RESULT_TABLE_COLUMN, group.toString(), "|");
        }
        m_printStream.printf(RESULT_TABLE_COUNT_COLUMN, "COUNT");
        m_printStream.println();
        for (Map.Entry<GroupingKey, BigInteger> pair : results.getStatistic().entrySet()) {
            for (GroupBy group : options.getGroupingConditions()) {
                String value = pair.getKey().getConditionValue(group);
                if (value != null) {
                    m_printStream.printf(RESULT_TABLE_COLUMN, value, "|");
                }
            }
            m_printStream.printf(RESULT_TABLE_COUNT_COLUMN, pair.getValue());
            m_printStream.println();
        }
    }

    private void groupBy(ParseOptions options, BufferedReader reader, String text) throws IOException {
        while (true) {
            ResponseMessage message = null;
            m_printStream.println(text);
            String line = reader.readLine();
            if (!line.isEmpty()) {
                String[] groups = line.split(Constants.GROUP_BY_SEPARATOR);
                for (String group : groups) {
                    try {
                        GroupBy groupBy = GroupBy.valueOf(group.trim().toUpperCase());
                        options.addGroupingCondition(groupBy);
                    } catch (IllegalArgumentException e) {
                        message = ResponseMessage.WRONG_GROUP_VALUE;
                        m_printStream.println(message);
                        break; //break inner loop;
                    }
                }
            }

            if (message == null) {
                message = options.checkGroupingValidity();
                m_printStream.println(message);
            }
            if (message == ResponseMessage.SUCCESS) {
                break;
            }
        }
    }

    private void inputPath(ParseOptions options, BufferedReader reader, String text) throws IOException {
        while (true) {
            m_printStream.println(text);
            String line = reader.readLine();
            ResponseMessage message = options.setInputPath(line);
            m_printStream.println(message);
            if (message == ResponseMessage.SUCCESS) {
                break;
            }
        }
    }

    private void messageFilter(ParseOptions options, BufferedReader reader, String text) throws IOException {
        while (true) {
            m_printStream.println(text);
            String line = reader.readLine();
            if (!line.isEmpty()) {
                ResponseMessage message = options.setMessageFilter(line);
                m_printStream.println(message);
                if (message == ResponseMessage.SUCCESS) {
                    break;
                }
            } else {
                break;
            }
        }
    }

    private void dateFilter(ParseOptions options, BufferedReader reader, String text) throws IOException {
        while (true) {
            m_printStream.println(text);
            m_printStream.println("Date FROM:");
            String dateFrom = reader.readLine();
            if (!dateFrom.isEmpty()) {
                m_printStream.println("Date TO:");
                String dateTo = reader.readLine();
                ResponseMessage message = options.setDateFilter(dateFrom, dateTo, Configuration.INSTANCE.getDateFormat());
                m_printStream.println(message);
                if (message == ResponseMessage.SUCCESS) {
                    break;
                }
            } else {
                break;
            }
        }
    }

    private void usernameFilter(ParseOptions options, BufferedReader reader, String text) throws IOException {
        while (true) {
            m_printStream.println(text);
            String line = reader.readLine();
            if (!line.isEmpty()) {
                ResponseMessage message = options.setUsernameFilter(line);
                m_printStream.println(message);
                if (message == ResponseMessage.SUCCESS) {
                    break;
                }
            } else {
                break;
            }
        }
    }

    private String getStartingMessage() {
        return "Tool for logs analysis." +
                System.lineSeparator() +
                "Please, double-check configuration before starting." +
                System.lineSeparator() +
                "Other options must be specified by user input." +
                System.lineSeparator();
    }
}
