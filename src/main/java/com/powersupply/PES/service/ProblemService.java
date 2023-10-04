package com.powersupply.PES.service;

import com.powersupply.PES.domain.dto.ProblemDTO;
import com.powersupply.PES.domain.entity.ProblemEntity;
import com.powersupply.PES.exception.AppException;
import com.powersupply.PES.exception.ErrorCode;
import com.powersupply.PES.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    // 문제 가져오기
    public List<ProblemDTO.ProblemResponse> getProblem() {

        List<ProblemEntity> problemEntityList = problemRepository.findAll();
        List<ProblemDTO.ProblemResponse> problemResponseList = new ArrayList<>();

        // 문제 리스트 비어있는지 체크
        if(problemEntityList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND,"정보가 존재하지 않습니다.");
        }

        // 저장
        for(ProblemEntity problemEntity : problemEntityList) {
            ProblemDTO.ProblemResponse problemResponse = ProblemDTO.ProblemResponse.builder()
                    .problemId(problemEntity.getProblemId())
                    .problemTitle(problemEntity.getProblemTitle())
                    .problemScore(problemEntity.getProblemScore())
                    .build();
            problemResponseList.add(problemResponse);
        }
        return problemResponseList;
    }
}
