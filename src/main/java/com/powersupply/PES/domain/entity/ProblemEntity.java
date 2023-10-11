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
@Table(name = "problem_table")
public class ProblemEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long problemId;
    private String problemTitle;
    private String problemContent;
    private int problemScore;

    @Builder.Default
    @OneToMany(mappedBy = "problemEntity")
    private List<QuestionEntity> questionEntities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "problemEntity")
    private List<AnswerEntity> answerEntities = new ArrayList<>();
}
