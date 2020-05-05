package fr.amazing.converter.mdhtml.generator;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

@Getter
@Setter
public class Extras {
    private String parent;
    private String actual;
    private int level;

    public static Extras of(String parent, String actual, int level) {
        Extras e = new Extras();
        e.setParent(StringUtils.remove(parent, File.separator));
        e.setActual(StringUtils.remove(actual, File.separator));
        e.setLevel(level);
        return e;
    }
}
