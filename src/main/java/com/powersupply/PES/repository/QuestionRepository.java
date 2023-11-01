package com.powersupply.PES.repository;

import com.powersupply.PES.domain.entity.QuestionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionEntity,Long> {
    List<QuestionEntity> findByProblemEntity_ProblemId(Long problemId, Pageable pageable);

    List<QuestionEntity> findByProblemEntity_ProblemId(Long problemId);
}
