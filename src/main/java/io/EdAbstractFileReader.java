package io;

/**
 * Created by edwardlol on 17-4-17.
 */
public abstract class EdAbstractFileReader<T> implements EdFileReader<T> {

    protected static String seperator;

    protected static int columnNumber;

    public void setSeperator(String _seperator) {
        seperator = _seperator;
    }

    public String getSeperator() {
        return seperator;
    }

    public void setColumnNumber(int _columnNumber) {
        columnNumber = _columnNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
