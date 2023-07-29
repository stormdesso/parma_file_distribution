package ru.parma.filesdistr.models.intermediate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "illustration_for_version")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IllustrationVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "file_id")
    Integer fileId;

    @Column(name = "version_id")
    Integer versionId;

}
