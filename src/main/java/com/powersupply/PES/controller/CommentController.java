package com.powersupply.PES.controller;

import com.powersupply.PES.domain.dto.CommentDTO;
import com.powersupply.PES.service.CommentService;
import com.powersupply.PES.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/comment/{problemId}/{memberStuNum}")
    public ResponseEntity<List<CommentDTO.ViewComment>> viewComment(@PathVariable Long problemId, @PathVariable String memberStuNum) {
        return ResponseEntity.ok().body(commentService.getViewComment(problemId, memberStuNum));
    }

    // 댓글 달기
    @PostMapping("/api/comment/{problemId}/{memberStuNum}")
    public ResponseEntity<?> postComment(@PathVariable Long problemId, @PathVariable String memberStuNum, @RequestBody CommentDTO.PostComment dto) {
        commentService.saveComment(problemId, memberStuNum, dto);
        return ResponseUtil.successResponse("성공");
    }
}
