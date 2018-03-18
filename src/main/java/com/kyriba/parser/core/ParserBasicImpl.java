package com.kyriba.parser.core;

import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Matcher;

/**
 * Basic implementation of {@link Parser} allows to analysis text files.
 *
 * @author VMyakushin
 */
public class ParserBasicImpl implements Parser {

    /**
     * {@inheritDoc}
     * Results of applying filters are written to file specified by {@link ParseOptions options}.
     *
     * @throws IllegalArgumentException in case of regular expression doesn't contain named group that coincides with filter.
     */
    @Override
    public ParsingResults parse(final ParseOptions options) throws IOException, IllegalArgumentException {
        FileUtils.clearFile(options.getOutputFile());
        ParsingResults results = new ParsingResults();

        for (File file : FileUtils.getFiles(options.getInputFile())) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                 OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(options.getOutputFile(), true), Charset.forName("UTF-8"))) {
                String line;
                Matcher matcher;

                while ((line = br.readLine()) != null) {
                    boolean matchFilter = true;
                    for (Filter filter : options.getFilters()) {
                        matcher = filter.getPattern().matcher(line);
                        String filterValue = options.getFilterValue(filter);
                        if (!filterValue.isEmpty()) {
                            if (!(matcher.find() && filter.match(matcher.group(filter.getPatternGroupName()), filterValue))) {
                                matchFilter = false;
                            }
                        }
                    }

                    if (matchFilter) {
                        GroupingKey key = new GroupingKey();
                        for (GroupBy condition : options.getGroupingConditions()) {
                            condition.processKey(line, key);
                        }
                        if (!key.isEmpty()) {
                            results.add(key);
                        }

                        osw.write(line + System.lineSeparator());
                    }
                }
            }
        }

        return results;
    }
}
