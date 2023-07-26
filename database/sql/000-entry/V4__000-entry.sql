create sequence users_id_seq
    as integer;

alter table users
    alter column id set default nextval('public.users_id_seq'::regclass);

alter sequence users_id_seq owned by users.id;

alter table user_scope
    alter column scope_id drop identity;

create sequence file_for_version_id_seq
    as integer;

alter table file_for_version
    alter column id set default nextval('public.file_for_version_id_seq'::regclass);

alter sequence file_for_version_id_seq owned by file_for_version.id;

drop table "manifest_for_IOS";

create table "manifest_IOS_ for_folder"
(
    id        serial
        primary key,
    folder_id int not null
        constraint "manifest_ios_ for_folder_folder_id_fk"
            references folder,
    file_id   int not null
        constraint "manifest_ios_ for_folder_file_id_fk"
            references file
);

alter table version
    drop column if exists "URLLicense";

alter table license_agreement_file_for_scope
    drop constraint scope_id;

alter table license_agreement_file_for_scope
    drop constraint license_agreement_file_for_scope_file_id_file_id1_key;








