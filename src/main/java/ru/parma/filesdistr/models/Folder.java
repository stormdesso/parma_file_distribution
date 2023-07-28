package ru.parma.filesdistr.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
    private Integer id;

    @Column(name = "scope_id")
    private Integer scopeId;

    @Column(name = "title")
    String name;

    @Column(name = "publish")
    boolean publish;

    @Column(name = "manifest_ios")
    boolean manifestForIOS;

    @Column(name = "identifier")
    String identifier;

    @OneToOne
    @JoinColumn(name = "manifest_ios_id")//manifest_ios_id(fk) folder -> id(pk) file
    private File manifestForIOSFile;

    @OneToMany( targetEntity = Version.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", referencedColumnName = "id")
    private List<Version> versions;

    @Override
    public String getPath () {
        return getName() + "//";
    }

    public String getRootPath ( @NotNull Scope scope ) {
        return  scope.getPath() + getPath();
    }


}
