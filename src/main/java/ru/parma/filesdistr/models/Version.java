package ru.parma.filesdistr.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.parma.filesdistr.utils.IPathName;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

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
    Integer folderId;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "illustration_for_version",
            joinColumns = {
                    @JoinColumn(name = "version_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "file_id", referencedColumnName = "id")
            }
    )
    List<File> images;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "file_for_version",
            joinColumns = {
                    @JoinColumn(name = "version_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "file_id", referencedColumnName = "id")
            }
    )
    List<File> files;
    @Override
    public String getPath () {
        return getVersionNumber() + "//";
    }

    public String getRootPath ( @NotNull Folder folder, Scope scope ) {
        return folder.getRootPath(scope) + getPath();
    }

}
