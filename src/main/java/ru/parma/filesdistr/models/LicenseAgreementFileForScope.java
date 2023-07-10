package ru.parma.filesdistr.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "license_agreement_file_for_scope")
@Data
public class LicenseAgreementFileForScope {
    @Id
    Long id;

    String name;
    String comment;
    Double size;
    byte [] content;
    @Column(name = "scope_id")
    Integer scopeId;
}
