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

alter table folder
    rename column "manifest_IOS" to manifest_ios;

drop table license_agreement_file_for_scope;

alter table scope
    add distribution_agreement_id int;

alter table scope
    add constraint scope_distr_agreement_fk
        foreign key (distribution_agreement_id) references file;

drop table "manifest_IOS_ for_folder";

alter table folder
    add manifest_ios_id int;

alter table folder
    add constraint folder_manifest_ios_id_fk
        foreign key (manifest_ios_id) references file;

drop table if exists tag_file_for_version;

alter table file_for_version
    add tag_id int;

alter table file_for_version
    add constraint file_for_version_tag_id_fk
        foreign key (tag_id) references tag;

create sequence tag_id_seq
    as integer;

alter table tag
    alter column id set default nextval('public.tag_id_seq'::regclass);

alter sequence tag_id_seq owned by tag.id;

alter table scope
    drop constraint icon_id_fk;

alter table scope
    add constraint icon_id_fk
        foreign key (icon_id) references file
            on delete cascade;

alter table illustration_for_version
    drop constraint illustration_for_version_version_id_fkey;

alter table illustration_for_version
    add foreign key (version_id) references version
        on delete cascade;

alter table illustration_for_version
    drop constraint illustration_for_version_file_id_fkey;

alter table illustration_for_version
    add foreign key (file_id) references file
        on delete cascade;

alter table illustration_for_version
    drop constraint illustration_for_version_version_id_fkey;

alter table illustration_for_version
    add foreign key (version_id) references version;

alter table file_for_version
    drop constraint file_for_version_file_id_fkey;

alter table file_for_version
    add foreign key (file_id) references file
        on delete cascade;

alter table folder
    drop constraint folder_manifest_ios_id_fk;

alter table folder
    add constraint folder_manifest_ios_id_fk
        foreign key (manifest_ios_id) references file
            on delete set null;

alter table folder
    drop constraint folder_manifest_ios_id_fk;

alter table folder
    add constraint folder_manifest_ios_id_fk
        foreign key (manifest_ios_id) references file;

alter table folder
    drop constraint folder_manifest_ios_id_fk;

alter table folder
    add constraint folder_manifest_ios_id_fk
        foreign key (manifest_ios_id) references file
            on delete set null;

ALTER TYPE roles ADD VALUE 'ADMIN_SCOPES' AFTER 'ADMIN';
ALTER TYPE roles ADD VALUE 'ROOT' BEFORE 'ADMIN';

alter table users
    add unique (name);

