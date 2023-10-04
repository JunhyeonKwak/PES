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

    private String memberStuNum; // 학번
    private String memberPw; // 멤버 비밀번호
    private String memberName; // 이름
    private int memberGen; // 기수
    private String memberMajor; // 학과
    private String memberPhone; // 전화번호
    private String memberStatus; // 상태
    private String memberEmail; // 이메일
    private int memberScore; // 점수
    private String memberGitUrl; // 깃주소
}
