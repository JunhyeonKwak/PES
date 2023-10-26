package com.powersupply.PES.service;

import com.powersupply.PES.domain.dto.ProblemDTO;
import com.powersupply.PES.domain.entity.AnswerEntity;
import com.powersupply.PES.domain.entity.ProblemEntity;
import com.powersupply.PES.exception.AppException;
import com.powersupply.PES.exception.ErrorCode;
import com.powersupply.PES.repository.AnswerRepository;
import com.powersupply.PES.repository.ProblemRepository;
import com.powersupply.PES.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final AnswerRepository answerRepository;

    // 문제 가져오기
    public List<ProblemDTO.ProblemResponse> getProblemList() {

        String stuNum = JwtUtil.getMemberStuNumFromToken();

        // 현재 인증된 사용자의 권한 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 권한 같은 경우 True 다른 경우 False
        boolean hasNewStudentRole = authorities.stream()
                .anyMatch(authority -> "ROLE_NEW_STUDENT".equals(authority.getAuthority()));

        List<ProblemEntity> problemEntityList = problemRepository.findAll();
        List<ProblemDTO.ProblemResponse> problemResponseList = new ArrayList<>();

        // 문제 리스트 비어있는지 체크
        if(problemEntityList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND,"정보가 존재하지 않습니다.");
        }

        // 저장
        for(ProblemEntity problemEntity : problemEntityList) {

            String answerState = null;

            // 권한이 "NEW_STUDENT"인 경우에만 answerState 조회
            if (hasNewStudentRole) {
                Optional<AnswerEntity> answerEntityOpt = answerRepository.findByMemberEntity_MemberStuNumAndProblemEntity_ProblemId(stuNum, problemEntity.getProblemId());
                if (answerEntityOpt.isPresent()) {
                    answerState = answerEntityOpt.get().getAnswerState();
                }
            }

            ProblemDTO.ProblemResponse problemResponse = ProblemDTO.ProblemResponse.builder()
                    .problemId(problemEntity.getProblemId())
                    .problemTitle(problemEntity.getProblemTitle())
                    .problemScore(problemEntity.getProblemScore())
                    .answerState(answerState)
                    .build();
            problemResponseList.add(problemResponse);
        }
        return problemResponseList;
    }

    public ProblemDTO.ShowProblem getProblem(Long problemId) {
        ProblemEntity selectedEntity = problemRepository.findById(problemId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "잘못된 페이지 호출"));

        return ProblemDTO.ShowProblem.builder()
                .problemId(selectedEntity.getProblemId())
                .problemTitle(selectedEntity.getProblemTitle())
                .problemContent(selectedEntity.getProblemContent())
                .problemScore(selectedEntity.getProblemScore())
                .build();
    }
}
