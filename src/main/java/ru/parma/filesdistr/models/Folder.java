package ru.parma.filesdistr.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "folder")
@Data
public class Folder {
    @Id
    @Column(name = "id")
    Integer id;

    @Column(name = "scope_id")
    Integer scopeId;

    @Column(name = "title")
    String name;

    @Column(name = "publish")
    boolean publish;

    @Column(name = "manifest_IOS")
    boolean manifestForIOS;

    @Column(name = "identifier")
    String identifier;

//TODO: починить

//    @OneToMany(fetch = FetchType.LAZY)
//        @JoinTable(name = "version",
//                joinColumns = @JoinColumn(name = "folder_id"))
//    List<Version> versions = new ArrayList<>();
}
