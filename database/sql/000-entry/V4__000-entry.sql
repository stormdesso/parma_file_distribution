create sequence users_id_seq
    as integer;

alter table users
    alter column id set default nextval('public.users_id_seq'::regclass);

alter sequence users_id_seq owned by users.id;