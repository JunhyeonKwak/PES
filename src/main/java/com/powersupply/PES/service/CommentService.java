package com.powersupply.PES.service;

import com.powersupply.PES.domain.dto.CommentDTO;
import com.powersupply.PES.domain.entity.AnswerEntity;
import com.powersupply.PES.domain.entity.CommentEntity;
import com.powersupply.PES.exception.AppException;
import com.powersupply.PES.exception.ErrorCode;
import com.powersupply.PES.repository.AnswerRepository;
import com.powersupply.PES.repository.CommentRepository;
import com.powersupply.PES.repository.MemberRepository;
import com.powersupply.PES.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    // 댓글 보기
    @Transactional
    public List<CommentDTO.ViewComment> getViewComment(Long problemId, String memberStuNum) {
        AnswerEntity answerEntity = answerRepository.findByMemberEntity_MemberStuNumAndProblemEntity_ProblemId(memberStuNum,problemId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,"해당 답안이 없습니다."));

        List<CommentEntity> commentEntities = commentRepository.findByAnswerEntity(answerEntity);

        List<CommentDTO.ViewComment> viewComments = new ArrayList<>();

        for(CommentEntity commentEntity: commentEntities) {
            LocalDateTime updateTime = commentEntity.getUpdatedTime();
            if(updateTime == null) {
                updateTime = commentEntity.getCreatedTime();
            }
            CommentDTO.ViewComment viewComment = CommentDTO.ViewComment.builder()
                    .memberName(commentEntity.getMemberEntity().getMemberName())
                    .memberGen(commentEntity.getMemberEntity().getMemberGen())
                    .commentPassFail(commentEntity.getCommentPassFail())
                    .commentContent(commentEntity.getCommentContent())
                    .updateTime(updateTime)
                    .build();
            viewComments.add(viewComment);
        }

        return viewComments;
    }

    // 댓글 달기
    public void saveComment(Long problemId, String memberStuNum, CommentDTO.PostComment dto) {
        AnswerEntity answerEntity = answerRepository.findByMemberEntity_MemberStuNumAndProblemEntity_ProblemId(memberStuNum, problemId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,"해당 답안이 없습니다."));

        // 연결된 comment 가져오기
        List<CommentEntity> commentEntities = commentRepository.findByAnswerEntity(answerEntity);

        int count = 0;
        for(CommentEntity commentEntity: commentEntities) {
            count += 1;
            // comment에 자신이 등록한 커멘트가 있다면 오류 메시지 전송
            if(commentEntity.getMemberEntity().getMemberStuNum().equals(JwtUtil.getMemberStuNumFromToken())) {
                throw new AppException(ErrorCode.INVALID_INPUT,"이미 자신이 등록한 커멘트가 있습니다.");
            }
        }
        // comment가 3개라면 오류 메시지 전송
        if(count == 3) {
            throw new AppException(ErrorCode.INVALID_INPUT,"3개의 답변이 이미 존재합니다.");
        }

        // 오류가 없다면 저장
        CommentEntity commentEntity = CommentEntity.builder()
                .memberEntity(memberRepository.findByMemberStuNum(JwtUtil.getMemberStuNumFromToken()).get())
                .answerEntity(answerEntity)
                .commentPassFail(dto.getCommentPassFail())
                .commentContent(dto.getCommentContent())
                .build();
        commentRepository.save(commentEntity);
    }
}
