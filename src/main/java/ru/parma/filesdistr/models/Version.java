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
import java.sql.Date;

@Entity
@Table(name = "version")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Version implements IPathName{

    @Id
    @Column(name = "id")
    Integer id;

    @Column(name = "version_number")
    String versionNumber;

    @Column(name = "date_of_publication")
    Date dateOfPublication;

    @Column(name = "description")
    String description;

    @Column(name = "show_illustration")
    boolean showIllustration;

    @Column(name = "publish")
    boolean publish;

    @Column(name = "folder_id")
    Integer parentId;


    @Override
    public String getPath () {
        return getVersionNumber() + "//";
    }

    public String getRootPath ( @NotNull Folder folder, Scope scope ) {
        return folder.getRootPath(scope) + getPath();
    }

    //TODO: починить

//    @Column(name = "title")
//    @CollectionTable(name = "folder",
//            joinColumns = @JoinColumn(name = "id"))
//    String parentName;

//    @ManyToMany
//    @JoinTable(name = "illustration_for_version",
//            joinColumns = @JoinColumn(name = "version_id"),
//            inverseJoinColumns = @JoinColumn(name = "file_id"))
//    List<File> illustrations = new ArrayList<>();
//
//    @ManyToMany
//    @JoinTable(name = "file_for_version",
//            joinColumns = @JoinColumn(name = "version_id"),
//            inverseJoinColumns = @JoinColumn(name = "file_id"))
//    List<File> files = new ArrayList<>();
}
