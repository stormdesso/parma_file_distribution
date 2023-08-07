alter table public.scope
    add permit_all boolean;

update public.scope
set permit_all = true
where title = 'testScope';

update public.scope
set permit_all = false
where title = 'testScope2';

ALTER TABLE public.scope
    ALTER COLUMN permit_all SET NOT NULL;