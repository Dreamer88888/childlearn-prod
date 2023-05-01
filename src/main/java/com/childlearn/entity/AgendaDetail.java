package com.childlearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_agenda_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long agendaHeaderId;
    private Long subjectId;
    private String title;

}
