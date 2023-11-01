package com.powersupply.PES.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class QuestionDTO {

    @Getter
    @Builder
    public static class QuestionList {
        private Long questionId;
        private String questionContent;
        private int questionDifficulty;
        private LocalDateTime updateTime;
    }
}
