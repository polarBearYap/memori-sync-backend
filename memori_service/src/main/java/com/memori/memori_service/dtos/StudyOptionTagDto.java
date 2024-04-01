package com.memori.memori_service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudyOptionTagDto extends SyncEntityDto {

    private String cardTagId;

    private String studyOptionId;

    // private CardTagDto tag;

    // private StudyOptionDto studyOption;

}