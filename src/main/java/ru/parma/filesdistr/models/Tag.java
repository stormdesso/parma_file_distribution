package ru.parma.filesdistr.models;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tag")
@Entity
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "letter")
    private String letter;

    @Column(name = "bkg_color")
    private String backgroundColor;

    @Column(name = "letter_color")
    private String letterColor;
}
