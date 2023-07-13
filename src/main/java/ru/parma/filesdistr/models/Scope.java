package ru.parma.filesdistr.models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scope")
@Data
public class Scope {

    @Id
    @Column(name = "id")
    Long id;

    @Column(name = "title")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "copyright")
    String copyright;

    @Column(name = "show_illustrations")
    boolean showIllustration;

    @Column(name = "icon_id")
    Long iconId;

    @OneToOne
    @JoinColumn(name = "icon_id", //icon_id - fk,
            insertable = false, updatable = false)//инчае не компилирует
    File icon; //картинка

    @ManyToMany
    @JoinTable(name = "illustration_for_scope",
            joinColumns = @JoinColumn(name = "scope_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))//используется для маппинга с другой таблицей, т.к отношениу Many To Many
    List<File> images = new ArrayList<>();


//    @Column(name = "content")
//    @CollectionTable(name = "license_agreement_file_for_scope",
//            joinColumns = @JoinColumn(name = "scope_id"))
//    byte[] licenseAgreement;

    @OneToMany(fetch = FetchType.LAZY)
            @JoinTable(name = "folder",
                    joinColumns = @JoinColumn(name = "scope_id"))
    List<Folder> folders;
}