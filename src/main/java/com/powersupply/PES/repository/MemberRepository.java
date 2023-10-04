package com.powersupply.PES.repository;

import com.powersupply.PES.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    Optional<MemberEntity> findByMemberStuNum(String stuNum);
}
