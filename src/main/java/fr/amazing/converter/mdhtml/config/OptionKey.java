package fr.amazing.converter.mdhtml.config;

import java.util.Arrays;

public enum OptionKey {
    REPOSITORY("REPO"), COLOR("COLOR"), SUBHEADER("SUBHEADER"),PROJECT_NAME("PROJECT_NAME");
    private String name;
    private OptionKey(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }

    public static OptionKey reverse(String val) {
        return Arrays.stream(OptionKey.values()).filter(x -> val.equals(x.toString())).findFirst().orElse(null);
    }

}
