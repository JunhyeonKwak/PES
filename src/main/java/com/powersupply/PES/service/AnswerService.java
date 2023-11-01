package com.powersupply.PES.service;

import com.powersupply.PES.domain.dto.AnswerDTO;
import com.powersupply.PES.domain.entity.AnswerEntity;
import com.powersupply.PES.domain.entity.MemberEntity;
import com.powersupply.PES.domain.entity.QuestionEntity;
import com.powersupply.PES.exception.AppException;
import com.powersupply.PES.exception.ErrorCode;
import com.powersupply.PES.repository.AnswerRepository;
import com.powersupply.PES.repository.MemberRepository;
import com.powersupply.PES.repository.ProblemRepository;
import com.powersupply.PES.repository.QuestionRepository;
import com.powersupply.PES.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private final QuestionRepository questionRepository;

    // 채점 하기
    @Transactional
    public void submit(Long problemId, String memberStuNum, AnswerDTO.gitUrl dto) {
        if(!JwtUtil.getMemberStuNumFromToken().equals(memberStuNum)) {
            throw new AppException(ErrorCode.INVALID_INPUT,"채점할 권한이 없는 유저 입니다.");
        }

        // 깃 주소 비교 로직
        MemberEntity memberEntity = memberRepository.findByMemberStuNum(memberStuNum)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "멤버 정보를 찾을 수 없습니다."));

        // 깃 주소가 해당 url로 시작 하지 않으면 AppException 실행
        String memberGitUrl = memberEntity.getMemberGitUrl();
        if(!dto.getAnswerUrl().startsWith(memberGitUrl)) {
            throw new AppException(ErrorCode.INVALID_INPUT,"제출된 깃 주소가 유저의 깃 주소와 일치하지 않습니다.");
        }

        // 채점 로직 추후 추가
        boolean check = true;

        Optional<AnswerEntity> optionalAnswerEntity = answerRepository.findByMemberEntity_MemberStuNumAndProblemEntity_ProblemId(memberStuNum,problemId);
        AnswerEntity answerEntity;

        // 무작위 2개의 질문 선택
        Pageable pageable = PageRequest.of(0, 2, Sort.unsorted());
        List<QuestionEntity> questions = questionRepository.findByProblemEntity_ProblemId(problemId, pageable);

        // answer table에 저장되어 있는지 판단
        if (optionalAnswerEntity.isPresent()) {
            answerEntity = optionalAnswerEntity.get();

            if(check) {
                // 성공 시 로직
                answerEntity.setAnswerState("InProgress");
                answerEntity.setAnswerUrl(dto.getAnswerUrl());
                answerEntity.setQuestionFst(questions.get(0));
                answerEntity.setQuestionSec(questions.get(1));
            } else {
                answerEntity.setAnswerUrl(dto.getAnswerUrl());
            }
        } else {
            if(check) {
                // 성공 시 초기화 로직
                answerEntity = AnswerEntity.builder()
                        .memberEntity(memberEntity)
                        .problemEntity(problemRepository.findById(problemId).get())
                        .answerState("Answer")
                        .answerUrl(dto.getAnswerUrl())
                        .questionFst(questions.get(0))
                        .questionSec(questions.get(1))
                        .build();
            } else {
                // 실패 시 초기화 로직
                answerEntity = AnswerEntity.builder()
                        .memberEntity(memberEntity)
                        .problemEntity(problemRepository.findById(problemId).get())
                        .answerState("InProgress")
                        .answerUrl(dto.getAnswerUrl())
                        .build();
            }
        }
        answerRepository.save(answerEntity);
    }

    public void saveAnswer(Long problemId, String memberStuNum, AnswerDTO.answerRequest dto) {
        if(!JwtUtil.getMemberStuNumFromToken().equals(memberStuNum)) {
            throw new AppException(ErrorCode.INVALID_INPUT,"답변할 권한이 없는 유저 입니다.");
        }

        AnswerEntity answerEntity = answerRepository.findByMemberEntity_MemberStuNumAndProblemEntity_ProblemId(memberStuNum,problemId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "아직 채점되지 않았습니다."));

        // answerFst와 answerSec가 null이 아닌 경우에 AppException 발생
        if(answerEntity.getAnswerFst() != null || answerEntity.getAnswerSec() != null) {
            throw new AppException(ErrorCode.INVALID_INPUT, "이미 답변이 있는 경우 수정해 주세요.");
        }

        answerEntity.setAnswerState("UnderReview");
        answerEntity.setAnswerFst(dto.getAnswerFst());
        answerEntity.setAnswerSec(dto.getAnswerSec());
        answerRepository.save(answerEntity);
    }

    @Transactional
    public void patchAnswer(Long problemId, String memberStuNum, AnswerDTO.answerRequest dto) {
        if(!JwtUtil.getMemberStuNumFromToken().equals(memberStuNum)) {
            throw new AppException(ErrorCode.INVALID_INPUT,"수정 권한이 없는 유저 입니다.");
        }

        AnswerEntity answerEntity = answerRepository.findByMemberEntity_MemberStuNumAndProblemEntity_ProblemId(memberStuNum,problemId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "아직 채점되지 않았습니다."));

        // commentEntities가 존재하는 경우에 AppException 발생
        if(!answerEntity.getCommentEntities().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT, "이미 코멘트가 있는 답변은 수정할 수 없습니다.");
        }

        answerEntity.setAnswerFst(dto.getAnswerFst());
        answerEntity.setAnswerSec(dto.getAnswerSec());
        answerRepository.save(answerEntity);
    }

    // (재)풀이 목록 보기
    @Transactional
    public List<AnswerDTO.SolveList> getSolveList(Long problemId) {
        List<AnswerEntity> answerEntityList = answerRepository.findAllByProblemEntity_ProblemId(problemId);
        List<AnswerDTO.SolveList> solveLists = new ArrayList<>();
        for(AnswerEntity answerEntity: answerEntityList) {
            int commentCount = 0;
            // AnswerEntity와 CommentEntity가 연관 관계에 있다면
            if(answerEntity.getCommentEntities() != null) {
                commentCount = answerEntity.getCommentEntities().size();
            }

            AnswerDTO.SolveList solveList = AnswerDTO.SolveList.builder()
                    .memberStuNum(answerEntity.getMemberEntity().getMemberStuNum())
                    .memberName(answerEntity.getMemberEntity().getMemberName())
                    .commentCount(commentCount)
                    .answerState(answerEntity.getAnswerState())
                    .build();
            solveLists.add(solveList);
        }
        return solveLists;
    }
}
