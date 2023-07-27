package ru.parma.filesdistr.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.parma.filesdistr.utils.IPathName;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "folder")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Folder implements IPathName{
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

    @Override
    public String getPath () {
        return getName() + "//";
    }

    public String getRootPath ( @NotNull Scope scope ) {
        return  scope.getPath() + getPath();
    }

//TODO: починить

//    @OneToMany(fetch = FetchType.LAZY)
//        @JoinTable(name = "version",
//                joinColumns = @JoinColumn(name = "folder_id"))
//    List<Version> versions = new ArrayList<>();
}
