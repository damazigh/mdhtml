package fr.amazing.converter.mdhtml.writer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public interface IWriter<T> {
    void write(T input, Object...opts) throws IOException;

    default void writeContentToFile(String content, String filename, String dir) {
        File file = new File(new StringBuilder(dir).append(File.separator).append(filename).toString());
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileUtils.write(file, content, "UTF-8");
        } catch (IOException e) {
            throw new IllegalStateException("error occured ... :( ", e);
        }

    }

    default void writeYmlFileFromObject(Object content, String filename, String dir){
        File file = new File(new StringBuilder(dir).append(File.separator).append(filename).toString());
        try {
            if (content != null) {
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                DumperOptions options = new DumperOptions();
                options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                options.setCanonical(false); // display bean member attribute
                options.setExplicitStart(true); // display --- start
                Yaml yml = new Yaml(options);
                yml.dump(content, new FileWriter(file));
            }
        } catch (IOException e) {
            throw new IllegalStateException("error occured ... :( ", e);
        }
    }
}