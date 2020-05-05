package fr.amazing.converter.mdhtml.config;

public interface ILoader {
    /**
     * Load the needed configuration (can be overrode by user)
     * @param configFile location of configuration file
     */
    void load(String configFile);

    /**
     * Load a yaml resource from classpath
     * @param resource
     */
    <T> T loadYmlFromClasspath(String resource, Class<T> clazz);
}
