package ru.parma.filesdistr.models;


import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "file")
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "size")
    private Double size;

    @Column(name = "type")
    private String type;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "location")
    private String location;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id")
    private Tag tag;

}