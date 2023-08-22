alter table public.version
alter column date_of_publication type timestamp;

alter table users
    add constraint users_prof_pic_fk
        foreign key (profile_picture_id) references file
            on delete set null;