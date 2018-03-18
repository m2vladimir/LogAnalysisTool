package com.kyriba.parser.core;

/**
 * Possible messages to the user.
 *
 * @author VMyakushin
 */
public enum ResponseMessage {
    SUCCESS {
        @Override
        public String toString() {
            return "The parameter was successfully set." + System.lineSeparator();
        }
    },
    WRONG_DATE_FORMAT {
        @Override
        public String toString() {
            return "Wrong date format. Dates must follow the pattern: " + Configuration.INSTANCE.getDateFormatString();
        }
    },
    WRONG_DATE_RANGE {
        @Override
        public String toString() {
            return "Date FROM must be early than date TO.";
        }
    },
    WRONG_INPUT_PATH {
        @Override
        public String toString() {
            return "Wrong input path. Please specify some file or directory.";
        }
    },
    WRONG_OUTPUT_PATH {
        @Override
        public String toString() {
            return "Wrong output path: file cannot be created.";
        }
    },
    FILTER_IS_NOT_VALID {
        @Override
        public String toString() {
            return "Wrong filter: at least one parameter should be specified.";
        }
    },
    GROUPING_IS_NOT_VALID {
        @Override
        public String toString() {
            return "Wrong group by: at least one parameter should be specified.";
        }
    },
    WRONG_GROUP_VALUE {
        @Override
        public String toString() {
            StringBuilder message = new StringBuilder();
            message.append("Wrong group value. Values can be: ");
            message.append(System.lineSeparator());
            for (GroupBy groupBy : GroupBy.values()) {
                message.append(groupBy);
                message.append(System.lineSeparator());
            }
            return message.toString();
        }

    },
    OUTPUT_FILE_CANNOT_BE_DIRECTORY {
        @Override
        public String toString() {
            return "Output file cannot be a directory. Please, check current configuration.";
        }
    },
    WRONG_REGULAR_EXPRESSION {
        @Override
        public String toString() {
            return "Wrong regular expression in config. See error log for details.";
        }
    },
    UNKNOWN_ERROR {
        @Override
        public String toString() {
            return "An error occurred. See error log for details.";
        }
    },
    CONFIG_LOADING_ERROR {
        @Override
        public String toString() {
            return "An error occurred during configuration loading. See error log for details.";
        }
    }
}
