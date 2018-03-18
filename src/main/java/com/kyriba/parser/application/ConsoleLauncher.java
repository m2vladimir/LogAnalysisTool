package com.kyriba.parser.application;

import com.kyriba.parser.controller.ConsoleController;
import com.kyriba.parser.core.Configuration;
import com.kyriba.parser.core.Filter;
import com.kyriba.parser.core.ResponseMessage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

/**
 * Application launcher class that uses console to communicate with the user.
 *
 * @author VMyakushin
 */
public class ConsoleLauncher {
    private static final Logger logger = Logger.getLogger(ConsoleLauncher.class.getName());

    public static void main(String[] args) throws IOException {
        try {
            String configPath;
            if (args.length > 0) {
                configPath = args[0];
            } else {
                throw new IOException("Path to config file is not defined");
            }
            Configuration.INSTANCE.load(configPath);
            logger.log(Level.INFO, "Configuration was successfully loaded from " + configPath);

            Filter.compileAll();
            logger.log(Level.INFO, "Patterns for all filters was compiled.");

            ConsoleController console = new ConsoleController(System.out, System.in);
            console.start();
        } catch (PatternSyntaxException e) {
            ResponseMessage message = ResponseMessage.WRONG_REGULAR_EXPRESSION;
            System.out.println(message);
            logger.log(Level.SEVERE, message.toString(), e);
        } catch (IOException e) {
            ResponseMessage message = ResponseMessage.CONFIG_LOADING_ERROR;
            System.out.println(message);
            logger.log(Level.SEVERE, message.toString(), e);
        }
    }
}
