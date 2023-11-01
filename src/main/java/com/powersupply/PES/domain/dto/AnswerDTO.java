package com.powersupply.PES.domain.dto;

import lombok.Builder;
import lombok.Getter;

public class AnswerDTO {

    @Getter
    @Builder
    public static class answerRequest {
        private String answerFst;
        private String answerSec;
    }

    @Getter
    public static class gitUrl {
        private String answerUrl;
    }

    @Getter
    @Builder
    public static class SolveList {
        private String memberStuNum;
        private String memberName;
        private int commentCount;
        private String answerState;
    }
}
