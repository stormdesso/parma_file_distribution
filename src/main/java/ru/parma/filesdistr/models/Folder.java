package ru.parma.filesdistr.models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "folder")
@Data
public class Folder {
    @Id
    @Column(name = "id")
    Integer id;

    @Column(name = "scope_id")
    Integer parentId;

    @Column(name = "title")
    String name;

    @Column(name = "publish")
    boolean publish;

    @Column(name = "manifest_IOS")
    boolean manifestForIOS;

    @Column(name = "identifier")
    String identifier;

    @Column(name = "content")
    @CollectionTable(name = "manifest_for_IOS",
            joinColumns = @JoinColumn(name = "folder_id"))
    byte[] manifestIOS;

    @OneToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "version",
                joinColumns = @JoinColumn(name = "folder_id"))
    List<Version> versions = new ArrayList<>();
}
