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

    @OneToMany(mappedBy = "problemEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<QuestionEntity> questionEntities = new ArrayList<>();
}
