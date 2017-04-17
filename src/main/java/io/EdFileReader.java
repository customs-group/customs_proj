package io;

/**
 * Created by edwardlol on 17-4-17.
 */
public interface EdFileReader<T> {
    T read(String file);

    void setSeperator(String _seperator);

    String getSeperator();
}
