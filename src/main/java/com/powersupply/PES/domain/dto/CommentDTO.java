package com.powersupply.PES.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class CommentDTO {

    @Getter
    @Builder
    public static class ViewComment {
        private String memberName;
        private int memberGen;
        private int commentPassFail;
        private String commentContent;
        private LocalDateTime updateTime;
    }

    @Getter
    public static class PostComment {
        private String writer;
        private int commentPassFail;
        private String commentContent;
    }

    @Getter
    public static class PatchComment {
        private String writer;
        private String commentContent;
    }

    @Getter
    @Builder
    public static class MyComment {
        private Long problemId;
        private String memberStuNum;
        private String answerState;
        private int commentPassFail;
    }
}
