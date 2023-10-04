package com.powersupply.PES.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "detail_member_table")
public class DetailMemberEntity extends BaseEntity{

    @Id
    @Column(name = "memberStuNum")
    private String memberStuNum; // 학번

    @Column(name = "memberPw")
    private String memberPw; // 암호화된 비밀번호

    @Column(name = "memberMajor")
    private String memberMajor; // 학과

    @Column(name = "memberPhone", unique = true)
    private String memberPhone; // 전화번호

    @Column(name = "memberEmail", unique = true)
    private String memberEmail; // 이메일

    @OneToOne
    @MapsId  // 이 어노테이션은 MemberEntity의 PK를 DetailMemberEntity의 PK/FK로 사용하게 만듭니다.
    @JoinColumn(name = "memberStuNum")
    private MemberEntity memberEntity;
}
