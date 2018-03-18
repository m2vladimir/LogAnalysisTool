package com.kyriba.parser.core;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * The tool configuration. Singleton.
 * <p>
 * <p>Initializes values from configuration file.</p>
 *
 * @author VMyakushin
 */
public enum Configuration {
    INSTANCE {
        private static final String USERNAME_PATTERN = "pattern.username";
        private static final String DATE_PATTERN = "pattern.date";
        private static final String MESSAGE_PATTERN = "pattern.message";
        private static final String DATE_FORMAT = "format.date";
        private static final String OUTPUT_PATH = "path.log.output";
        private SimpleDateFormat m_dateFormat;
        private Properties m_properties;

        @Override
        public String getUsernamePattern() {
            return m_properties.getProperty(USERNAME_PATTERN);
        }

        @Override
        public String getDatePattern() {
            return m_properties.getProperty(DATE_PATTERN);
        }

        @Override
        public String getMessagePattern() {
            return m_properties.getProperty(MESSAGE_PATTERN);
        }

        @Override
        public String getDateFormatString() {
            return m_properties.getProperty(DATE_FORMAT);
        }

        @Override
        public DateFormat getDateFormat() {
            return m_dateFormat;
        }

        @Override
        public String getOutputPath() {
            return m_properties.getProperty(OUTPUT_PATH);
        }

        @Override
        public void load(final String configPath) throws IOException {
            m_properties = new Properties();
            m_dateFormat = new SimpleDateFormat();
            File config = new File(configPath);

            if (config.exists() && config.isFile()) {
                try (final Reader reader = new FileReader(config)) {
                    m_properties.load(reader);
                }
            } else { //create default config
                m_properties.setProperty(USERNAME_PATTERN, DefaultConfig.DEFAULT_USERNAME_PATTERN);
                m_properties.setProperty(DATE_PATTERN, DefaultConfig.DEFAULT_DATE_PATTERN);
                m_properties.setProperty(MESSAGE_PATTERN, DefaultConfig.DEFAULT_MESSAGE_PATTERN);
                m_properties.setProperty(DATE_FORMAT, DefaultConfig.DEFAULT_DATE_FORMAT);
                m_properties.setProperty(OUTPUT_PATH, DefaultConfig.DEFAULT_OUTPUT_PATH);

                config.getParentFile().mkdirs();
                config.createNewFile();
                try (final Writer writer = new FileWriter(config)) {
                    m_properties.store(writer, DefaultConfig.DEFAULT_COMMENT);
                }
            }

            m_dateFormat.applyPattern(getDateFormatString());
        }
    };

    public abstract String getUsernamePattern();

    public abstract String getDatePattern();

    public abstract String getMessagePattern();

    public abstract String getDateFormatString();

    public abstract DateFormat getDateFormat();

    public abstract String getOutputPath();

    /**
     * Load configuration from specified path.
     * Config file with default configuration will be created if config file by specified path is missed.
     *
     * @param configPath configuration file path.
     * @throws IOException in case of reading\writing errors.
     */
    public abstract void load(final String configPath) throws IOException;

    private final class DefaultConfig {
        private final static String DEFAULT_COMMENT = "";
        private final static String DEFAULT_USERNAME_PATTERN = "\\s\\[(?<username>\\w+)\\]";
        private final static String DEFAULT_DATE_PATTERN = "^(?<date>[0-9]{2}/[0-9]{2}/[0-9]{4})\\s";
        private final static String DEFAULT_MESSAGE_PATTERN = "\\[\\w+\\]:\\s(?<message>.*)$";
        private final static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
        private final static String DEFAULT_OUTPUT_PATH = "output/output.log";

        private DefaultConfig() {
        }
    }
}
