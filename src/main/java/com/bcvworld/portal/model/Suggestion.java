package com.bcvworld.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "suggestions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "help_required", length = 1000)
    private String helpRequired;

    @Column(name = "suggestion", length = 2000)
    private String suggestion;

    @Column(name = "agreed")
    private boolean agreed;

    @Column(name = "created_date")
    private String date;
}
