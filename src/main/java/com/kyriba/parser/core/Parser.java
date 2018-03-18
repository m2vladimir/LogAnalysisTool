package com.kyriba.parser.core;

import java.io.IOException;

/**
 * Interface provides methods to files analysis.
 *
 * @author VMyakushin
 */
public interface Parser {
    /**
     * Method allows to parse text files and collect results based on user input.
     *
     * @param options user defined input parameters.
     * @return results of data analysis.
     * @throws IOException in case of reading\writing errors.
     */
    ParsingResults parse(final ParseOptions options) throws IOException;
}
