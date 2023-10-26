package com.powersupply.PES.domain.dto;

import lombok.Builder;
import lombok.Getter;

public class ProblemDTO {

    @Getter
    @Builder
    public static class ProblemResponse {
        private Long problemId;
        private String problemTitle;
        private int problemScore;
        private String problemState;
    }

    @Getter
    @Builder
    public static class ShowProblem {
        private Long problemId;
        private String problemTitle;
        private String problemContent;
        private int problemScore;
    }
}
