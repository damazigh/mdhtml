package fr.amazing.converter.mdhtml.parser;

public interface IParser<T, V> {
    V parse(T arg);

    default boolean belongToLevel(int index, int levelSize) {
        return index <= levelSize;
    }
}
