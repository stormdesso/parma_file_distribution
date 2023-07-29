package ru.parma.filesdistr.models.intermediate;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "file_for_version")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "file_id")
    Integer fileId;

    @Column(name = "version_id")
    Integer versionId;

}
