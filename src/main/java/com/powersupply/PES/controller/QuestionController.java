package com.powersupply.PES.controller;

import com.powersupply.PES.domain.dto.QuestionDTO;
import com.powersupply.PES.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    // (재)질문 목록 보기
    @GetMapping("/api/questions/{problemId}")
    public ResponseEntity<List<QuestionDTO.QuestionList>> getQuestionList(@PathVariable Long problemId) {
        return ResponseEntity.ok().body(questionService.getQeustionList(problemId));
    }
}
