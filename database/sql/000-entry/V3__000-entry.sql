alter table scope
    add icon_id integer;

alter table scope
    add constraint icon_id_fk
        foreign key (icon_id) references file;

alter table version
    add id serial;