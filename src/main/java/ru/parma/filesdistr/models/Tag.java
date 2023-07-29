package ru.parma.filesdistr.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tag")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "letter")
    private String letter;

    @Column(name = "bkg_color")
    private String backgroundColor;

    @Column(name = "letter_color")
    private String letterColor;
}
