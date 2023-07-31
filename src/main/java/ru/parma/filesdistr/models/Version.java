package ru.parma.filesdistr.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", referencedColumnName = "id")//folder_id(fk) version -> id(pk) folder
    private  Folder folder;

    @Column(name = "version_number")
    private String versionNumber;

    @Column(name = "date_of_publication")
    private Date dateOfPublication;

    @Column(name = "description")
    private String description;

    @Column(name = "show_illustration")
    private boolean showIllustration;

    @Column(name = "publish")
    private boolean publish;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "illustration_for_version",
            joinColumns = {
                    @JoinColumn(name = "version_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "file_id", referencedColumnName = "id")
            }
    )
    private  List<File> images;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "file_for_version",
            joinColumns = {
                    @JoinColumn(name = "version_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "file_id", referencedColumnName = "id")
            }
    )
    private List<File> files;

    @Override
    public String getPath () {
        return getVersionNumber() + "//";
    }

    @Override
    public String getRootPath () {
        return folder.getRootPath() + getPath();
    }
}
