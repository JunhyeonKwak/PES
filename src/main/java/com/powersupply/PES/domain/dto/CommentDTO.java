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
        private int commentPassFail;
        private String commentContent;
    }
}
