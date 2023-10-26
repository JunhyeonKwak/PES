package com.powersupply.PES.repository;

import com.powersupply.PES.domain.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {
    Optional<AnswerEntity> findByMemberEntity_MemberStuNumAndProblemEntity_ProblemId(String stuNum, Long problemId);
}
