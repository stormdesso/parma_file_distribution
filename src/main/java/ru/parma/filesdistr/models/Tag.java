package ru.parma.filesdistr.models;

import lombok.*;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "letter")
    private String letter;

    @Column(name = "bkg_color")
    private String backgroundColor;

    @Column(name = "letter_color")
    private String letterColor;
}
