package com.memori.memori_service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudyOptionStateDto extends SyncEntityDto {

    private Integer state;

    private String studyOptionId;

    // private StudyOptionDto studyOption;

}
