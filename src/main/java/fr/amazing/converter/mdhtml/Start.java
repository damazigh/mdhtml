package fr.amazing.converter.mdhtml;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Start {
    private static final Logger LOG = LoggerFactory.getLogger(Start.class);
    public static void main(String [] args) {
        try {
            String dir;
            String mode;
            if (args != null && args.length >= 2) {
                mode = args[0];
                dir= args[1];
                args = Arrays.copyOfRange(args, 2, args.length);
            } else {
                LOG.warn("Invalid command must at least specify two first argument");
                return;
            }
            Facade facade = Facade.getInstance();
            if (StringUtils.equalsIgnoreCase(mode, "init")) {
                LOG.info("Initialisation mode");
                facade.doInit(dir, args);
            } else if (StringUtils.equalsIgnoreCase(mode, "gen")){
                LOG.info("Generation mode");
                facade.doGenerate(dir);
            }
        } catch (Exception e) {
            LOG.error("Something went wrong \\_O_/", e);
        }
    }
}

