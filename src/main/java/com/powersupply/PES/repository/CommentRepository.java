package com.powersupply.PES.repository;

import com.powersupply.PES.domain.entity.AnswerEntity;
import com.powersupply.PES.domain.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    List<CommentEntity> findByAnswerEntity(AnswerEntity answerEntity);
}
