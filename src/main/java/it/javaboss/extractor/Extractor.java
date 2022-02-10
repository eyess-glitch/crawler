package it.javaboss.extractor;


// Probabilmente inutile
public interface Extractor<T extends Object> {

    void extract(T data);
}
