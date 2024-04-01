package com.memori.memori_service.dtos;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PullSyncResponseDto {
    private List<SyncEntityDto> conflictedItems;

    private List<SyncEntityDto> successfulItems;
}
