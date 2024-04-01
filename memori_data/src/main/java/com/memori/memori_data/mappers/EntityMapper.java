package com.memori.memori_data.mappers;

public interface EntityMapper<T> {
    void mapEntity(T source, T target);

    void updateEntity(T source, T target);

    void deleteEntity(T source, T target);
}