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
@Table(name = "question_table")
public class QuestionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    private String questionContent;
    private int questionDifficulty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private ProblemEntity problemEntity;

    @OneToMany(mappedBy = "questionFst")
    private List<AnswerEntity> answerEntitiesForFst = new ArrayList<>();

    @OneToMany(mappedBy = "questionSec")
    private List<AnswerEntity> answerEntitiesForSec = new ArrayList<>();
}
