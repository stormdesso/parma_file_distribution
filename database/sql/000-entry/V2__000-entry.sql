ALTER TABLE public.file RENAME COLUMN "dateCreated" TO date_created;
ALTER TABLE public.file DROP CONSTRAINT file_id_fkey;

