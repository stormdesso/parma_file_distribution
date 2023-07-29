package ru.parma.filesdistr.models;

import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.parma.filesdistr.utils.IPathName;

import javax.persistence.*;
import java.util.List;


@Table(name = "folder")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Folder implements IPathName{
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "scope_id", referencedColumnName = "id")//scope_id(fk) folder -> id(pk) scope
    private  Scope scope;

    @Column(name = "title")
    private String name;

    @Column(name = "publish")
    private boolean publish;

    @Column(name = "manifest_ios")
    private boolean manifestForIOS;

    @Column(name = "identifier")
    private String identifier;

    @OneToOne
    @JoinColumn(name = "manifest_ios_id")//manifest_ios_id(fk) folder -> id(pk) file
    private File manifestForIOSFile;

    @OneToMany( targetEntity = Version.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", referencedColumnName = "id")
    private List<Version> versions;

    @Override
    public String getPath () {
        return getName() + "//";
    }

    @Override
    public String getRootPath () {
        return scope.getRootPath() + getPath();
    }
}
