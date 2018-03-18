package com.kyriba.parser.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File utils.
 *
 * @author VMyakushin
 */
public final class FileUtils {
    private static final Logger logger = Logger.getLogger(FileUtils.class.getName());

    private FileUtils() {
    }

    /**
     * Get list of files with specified path.
     * List will contains only one record if specified file is regular file (not a directory).
     *
     * @param file representation of file and directory pathnames.
     * @return list of files. Can be empty.
     */
    public static List<File> getFiles(final File file) {
        List<File> fileList = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            fileList.addAll(Arrays.asList(files));
        } else {
            fileList.add(file);
        }
        logger.log(Level.INFO, String.format("%o files was found in %s", fileList.size(), file.getAbsolutePath()));
        return fileList;
    }

    /**
     * Check if file exists by specified path.
     * Using nio.
     *
     * @param path path to file.
     * @return true if exists.
     */
    public static boolean isExist(final String path) {
        return Files.exists(Paths.get(path));
    }

    /**
     * Clear existing file.
     *
     * @param file file for clearing.
     * @throws IOException if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason.
     */
    public static void clearFile(final File file) throws IOException {
        try (final Writer writer = new FileWriter(file)) {
            //nothing
        }
    }
}
