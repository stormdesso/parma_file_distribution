package ru.parma.filesdistr.models;


import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "file")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "name")
    String name;

    @Column(name = "size")
    Double size;

    @Column(name = "type")
    String type;

    @Column(name = "date_created")
    Date dateCreated;

    @Column(name = "location")
    String location;

    //@Nullable
    //List<Tag> tags;

//    @ManyToMany(mappedBy = "images")
//    List<Scope> scopes;

//    @ManyToMany(mappedBy = "illustrations")
//    List<Version> IllustrationsForVersion;
//
//    @ManyToMany(mappedBy = "files")
//    List<Version> filesForVersion;

}