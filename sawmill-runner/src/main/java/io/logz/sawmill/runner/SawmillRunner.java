package io.logz.sawmill.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Eran Shahar on 06/04/2017.
 */
public class SawmillRunner {

    private static final Logger logger = LoggerFactory.getLogger(SawmillRunner.class);

    public static void main(String [] args) {
        validateArgs(args);
    }

    private static void validateArgs(String [] args) {
        if (args.length < 2 || args.length > 3) {
            logUsage();
            System.exit(1);
        }

    }

    private static void logUsage() {
        // TODO - IMPLEMENT
    }

}
