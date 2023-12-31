package ru.parma.filesdistr.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import ru.parma.filesdistr.utils.IPathName;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "scope")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Scope implements IPathName {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "copyright")
    private String copyright;

    @Column(name = "show_illustrations")
    private boolean showIllustration;

    @Formula("(SELECT COUNT(*) = 0 FROM user_scope AS us" +
            "    WHERE (us.scope_id = id AND us.user_id IN" +
            "        (SELECT r.user_id FROM role AS r" +
            "             WHERE r.role_name = 'USER' AND us.user_id = r.user_id)))")
    private boolean permitAll;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = Folder.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "scope_id", referencedColumnName = "id")
    private List<Folder> folders;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "icon_id")//icon_id(fk) scope -> id(pk) file
    private File icon;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "illustration_for_scope",
            joinColumns = {
                    @JoinColumn(name = "scope_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "file_id", referencedColumnName = "id")
            }
    )
    List<File> images;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "distribution_agreement_id")
    private File distributionAgreement;

    @Override
    public String getPath () {
        return getName() + "//";
    }

    @Override
    public String getRootPath () {
        return getPath();
    }
}