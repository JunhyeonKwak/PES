package com.powersupply.PES.repository;

import com.powersupply.PES.domain.entity.DetailMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetailMemberRepository extends JpaRepository<DetailMemberEntity, String> {
    Optional<DetailMemberEntity> findByMemberStuNum(String stuNum);
}
