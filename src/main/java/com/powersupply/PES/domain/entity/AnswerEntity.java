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
@Table(name = "answer_table")
public class AnswerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;
    private String answerState;
    private String answerUrl;
    private String answerFst;
    private String answerSec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberStuNum")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "problemId")
    private ProblemEntity problemEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "questionFst")
    private QuestionEntity questionFst;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "questionSec")
    private QuestionEntity questionSec;

    @OneToMany(mappedBy = "answerEntity")
    private List<CommentEntity> commentEntities = new ArrayList<>();
}
