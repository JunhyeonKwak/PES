package com.powersupply.PES.service;

import com.powersupply.PES.domain.dto.CommentDTO;
import com.powersupply.PES.domain.entity.AnswerEntity;
import com.powersupply.PES.domain.entity.CommentEntity;
import com.powersupply.PES.exception.AppException;
import com.powersupply.PES.exception.ErrorCode;
import com.powersupply.PES.repository.AnswerRepository;
import com.powersupply.PES.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AnswerRepository answerRepository;

    // 댓글 보기
    @Transactional
    public List<CommentDTO.ViewComment> getViewComment(Long problemId, String memberStuNum) {
        AnswerEntity answerEntity = answerRepository.findByMemberEntity_MemberStuNumAndProblemEntity_ProblemId(memberStuNum,problemId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,"해당 답안이 없습니다."));

        List<CommentEntity> commentEntities = commentRepository.findByAnswerEntity(answerEntity);

        List<CommentDTO.ViewComment> viewComments = new ArrayList<>();

        for(CommentEntity commentEntity: commentEntities) {
            CommentDTO.ViewComment viewComment = CommentDTO.ViewComment.builder()
                    .memberName(commentEntity.getMemberEntity().getMemberName())
                    .memberGen(commentEntity.getMemberEntity().getMemberGen())
                    .commentPassFail(commentEntity.getCommentPassFail())
                    .commentContent(commentEntity.getCommentContent())
                    .createTime(commentEntity.getCreatedTime())
                    .updateTime(commentEntity.getUpdatedTime())
                    .build();
            viewComments.add(viewComment);
        }

        return viewComments;
    }
}
