package fr.amazing.converter.mdhtml.assertions;

import java.io.File;

public class FileAssertion {
    public static void assertExists(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException(String.format("Expected '%s' file to exists", file.getAbsolutePath()));
        }
    }

    public static boolean assertExists(String file) {
        try {
            assertExists(new File(file));
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public static void assertDirExist(File file) {
        if (!file.isDirectory()) {
            throw new IllegalArgumentException(String.format("Given directory : %s doesn't exist", file.getAbsolutePath()));
        }
    }
}
