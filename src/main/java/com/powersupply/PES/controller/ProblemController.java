package com.powersupply.PES.controller;

import com.powersupply.PES.domain.dto.ProblemDTO;
import com.powersupply.PES.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/problem/{memberStuNum}/{problemId}")
    public String getProb() {
        return "prob";
    }

    @GetMapping("/api/problemlist")
    public ResponseEntity<List<ProblemDTO.ProblemResponse>> getProblemList() {
        return ResponseEntity.ok().body(problemService.getProblemList());
    }

    @GetMapping("/api/problem/{problemId}")
    public ResponseEntity<ProblemDTO.ShowProblem> getProblem(@PathVariable Long problemId) {
        return ResponseEntity.ok().body(problemService.getProblem(problemId));
    }

    @GetMapping("/api/problem/{problemId}/{memberStuNum}")
    public ResponseEntity<ProblemDTO.SolveForm> getSolveForm(@PathVariable Long problemId, @PathVariable String memberStuNum) {
        return ResponseEntity.ok().body(problemService.getSolveForm(problemId,memberStuNum));
    }
}
