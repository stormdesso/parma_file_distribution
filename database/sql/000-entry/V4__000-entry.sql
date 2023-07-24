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

