create table scope
(
    id                 integer generated always as identity
        primary key,
    title              varchar(20) not null
        unique,
    description        varchar(1000),
    copyright          varchar(40),
    show_illustrations boolean     not null
);



create table folder
(
    id             integer generated always as identity
        primary key,
    scope_id       integer     not null
        references scope,
    title          varchar(50) not null
        constraint folder_title_title1_key
            unique,
    publish        boolean     not null,
    "manifest_IOS" boolean     not null,
    identifier     varchar(30) not null
);



create table "manifest_for_IOS"
(
    id        integer      not null
        primary key,
    folder_id integer      not null
        constraint folder_id
            unique
        references folder,
    name      varchar(250) not null,
    content   bytea        not null
);



create table version
(
    id                  integer     not null
        primary key,
    version_number      varchar(10) not null,
    date_of_publication date        not null,
    description         varchar(1000),
    show_illustration   boolean     not null,
    "URLLicense"        varchar(25) not null,
    publish             boolean     not null,
    folder_id           integer     not null
        references folder,
    constraint version_folder_id_version_number_folder_id1_version_number1_key
        unique (folder_id, version_number)
);



create table tag
(
    id           integer      not null
        primary key,
    letter       varchar(250) not null,
    bkg_color    varchar      not null,
    letter_color varchar      not null
);



create table users
(
    id                          integer               not null
        constraint user_pkey
            primary key,
    name                        varchar(40)           not null,
    password                    varchar(255)          not null,
    blocked                     boolean default false not null,
    is_admin_manager            boolean default false not null,
    is_admin_scope_manager      boolean default false not null,
    can_create_and_delete_scope boolean default false,
    max_number_scope            integer default 0,
    max_storage_space           integer default 0,
    max_number_folder           integer default 0
);



create table user_scope
(
    user_id  integer not null
        references users,
    scope_id integer generated always as identity
        references scope
);



create table file
(
    id            integer generated always as identity
        primary key
        references version,
    name          varchar(255)     not null,
    size          double precision not null,
    type          varchar          not null,
    "dateCreated" date             not null,
    location      varchar          not null
);



create table license_agreement_file_for_scope
(
    id       integer generated always as identity
        primary key,
    name     varchar(250)     not null,
    comment  varchar(1000),
    size     double precision not null,
    content  bytea            not null,
    scope_id integer          not null
        constraint scope_id
            unique
        constraint scope_fkey
            references scope,
    file_id  integer          not null
        constraint license_agreement_file_for_scope_file_id_file_id1_key
            unique
        references file
);



create table illustration_for_scope
(
    id       integer generated always as identity
        primary key,
    scope_id integer not null
        references scope,
    file_id  integer not null
        constraint illustration_for_scope_file_id_file_id1_key
            unique
        references file
);



create table file_for_version
(
    id         integer not null
        primary key,
    version_id integer not null
        references version,
    file_id    integer not null
        constraint file_for_version_file_id_file_id1_key
            unique
        references file
);



create table tag_file_for_version
(
    tag_id              integer not null
        references tag,
    file_for_version_id integer not null
        references file_for_version,
    constraint tag_and_version
        unique (tag_id, file_for_version_id)
);


create table illustration_for_version
(
    id         integer generated always as identity
        primary key,
    version_id integer not null
        references version,
    file_id    integer not null
        constraint illustration_for_version_file_id_file_id1_key
            unique
        references file
);



DROP TYPE IF EXISTS "roles";
CREATE TYPE "roles" AS ENUM ('ADMIN','USER');
create table role
(
    id        integer generated always as identity
        primary key,

    user_id   integer not null
        references users,
    role_name roles   not null
);




