package com.powersupply.PES.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "member_table")
public class MemberEntity {

    @Id
    @Column(name = "memberStuNum")
    private String memberStuNum; // 학번

    @Column(name = "memberName")
    private String memberName; // 이름

    @Column(name = "memberGitUrl")
    private String memberGitUrl; // 깃 주소

    @Column(name = "memberGen")
    private int memberGen; // 기수

    @Column(name = "memberScore")
    private int memberScore; // 점수

    @Column(name = "memberStatus")
    private String memberStatus; // 상태(신입생/재학생/관리자)

    @OneToOne(mappedBy = "memberEntity")
    private DetailMemberEntity detailMemberEntity;

    @OneToMany(mappedBy = "memberEntity")
    private List<AnswerEntity> answerEntities = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity")
    private List<CommentEntity> commentEntities = new ArrayList<>();
}
