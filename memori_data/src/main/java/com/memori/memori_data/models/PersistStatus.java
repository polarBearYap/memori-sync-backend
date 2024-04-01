package com.memori.memori_data.models;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class PersistStatus<T> {
    
    @NonNull
    private Boolean isSuccessful;

    @NonNull
    private Boolean hasSyncConflict;

    @NonNull
    private T entity;
}
