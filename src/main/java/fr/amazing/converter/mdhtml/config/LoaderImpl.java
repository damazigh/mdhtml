package fr.amazing.converter.mdhtml.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class LoaderImpl implements ILoader{
    private Context context = Context.getInstance();
    public void load(String configFile) {
        try {
            File file = new File(configFile);
            // file should be valid yaml
            Yaml yaml = new Yaml(new Constructor(Option.class));
            InputStream is = new FileInputStream(file);
            Option options = yaml.load(is);
            this.context.setOption(options);
        } catch(FileNotFoundException e) {
            throw new IllegalStateException(String.format("The file '%s' doesn't exists !", configFile), e);
        }
    }
    public <T> T loadYmlFromClasspath(String resource, Class<T> clazz) {
            Yaml yml = new Yaml(new Constructor(clazz));
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(resource);
            return (T) yml.load(is);
    }
}