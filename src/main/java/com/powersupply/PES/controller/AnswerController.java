package com.powersupply.PES.controller;

import com.powersupply.PES.domain.dto.AnswerDTO;
import com.powersupply.PES.service.AnswerService;
import com.powersupply.PES.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return answerService.submit(problemId, memberStuNum, dto);
    }

    // 채점 결과 받기
    @PostMapping("/api/v2/return")
    public ResponseEntity<?> returnSubmit(@RequestBody AnswerDTO.returnSubmit dto) {
        answerService.returnSubmit(dto);
        return ResponseUtil.successResponse("성공");
    }

    // (재)풀이 목록 보기
    @GetMapping("/api/solvelist/{problemId}")
    public ResponseEntity<List<AnswerDTO.SolveList>> getSolveList(@PathVariable Long problemId) {
        return ResponseEntity.ok().body(answerService.getSolveList(problemId));
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
