package it.javaboss.extractor;

public interface Extractor<T extends Object> {

    void extract(T data);
}
