package it.javaboss;


public interface Extractor<T extends Object> {

    void extract(T data);
}
