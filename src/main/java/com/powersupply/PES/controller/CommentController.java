package com.powersupply.PES.controller;

import com.powersupply.PES.domain.dto.CommentDTO;
import com.powersupply.PES.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/comment/{problemId}/{memberStuNum}")
    public ResponseEntity<List<CommentDTO.ViewComment>> viewComment(@PathVariable Long problemId, @PathVariable String memberStuNum) {
        return ResponseEntity.ok().body(commentService.getViewComment(problemId, memberStuNum));
    }
}
