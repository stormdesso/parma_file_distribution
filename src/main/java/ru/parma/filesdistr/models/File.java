package ru.parma.filesdistr.models;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "file")
@Data
public class File {

    @Id
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



    //byte[] data;

    //@Nullable
    //List<Tag> tags;

    @ManyToMany(mappedBy = "images")
    List<Scope> scopes;

//    @ManyToMany(mappedBy = "illustrations")
//    List<Version> IllustrationsForVersion;
//
//    @ManyToMany(mappedBy = "files")
//    List<Version> filesForVersion;
}