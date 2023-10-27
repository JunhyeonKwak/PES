package com.powersupply.PES.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class ProblemDTO {

    @Getter
    @Builder
    public static class ProblemResponse {
        private Long problemId;
        private String problemTitle;
        private int problemScore;
        private String answerState;
    }

    @Getter
    @Builder
    public static class ShowProblem {
        private Long problemId;
        private String problemTitle;
        private String problemContent;
        private int problemScore;
    }

    @Getter
    @Builder
    public static class SolveForm {
        private String answerState;
        private String answerUrl;
        private String questionContentFst;
        private String questionContentSec;
        private String answerFst;
        private String answerSec;
        private LocalDateTime updateTime;
    }
}
