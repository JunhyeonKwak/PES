package com.powersupply.PES.controller;

import com.powersupply.PES.domain.dto.AnswerDTO;
import com.powersupply.PES.service.AnswerService;
import com.powersupply.PES.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping("/solve/{problemId}")
    public String getSolve() {
        return "solve";
    }

    // 채점 하기
    @PostMapping("/api/submit/{problemId}/{memberStuNum}")
    public ResponseEntity<?> submit(@PathVariable Long problemId, @PathVariable String memberStuNum, @RequestBody AnswerDTO.gitUrl dto) {
        answerService.submit(problemId, memberStuNum, dto);
        return ResponseUtil.successResponse("성공");
    }

    // 답변 하기
    @PostMapping("/api/answer/{problemId}/{memberStuNum}")
    public ResponseEntity<?> postAnswer(@PathVariable Long problemId, @PathVariable String memberStuNum, @RequestBody AnswerDTO.answerRequest dto) {
        answerService.saveAnswer(problemId, memberStuNum, dto);
        return ResponseUtil.successResponse("성공");
    }

    // 답변 수정 하기
    @PatchMapping("/api/answer/{problemId}/{memberStuNum}")
    public ResponseEntity<?> patchAnswer(@PathVariable Long problemId, @PathVariable String memberStuNum, @RequestBody AnswerDTO.answerRequest dto) {
        answerService.patchAnswer(problemId, memberStuNum, dto);
        return ResponseUtil.successResponse("성공");
    }
}
