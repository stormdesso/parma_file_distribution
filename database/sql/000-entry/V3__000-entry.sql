INSERT INTO public.users(
    name, password )
VALUES ('testAdmin',
        '$2a$08$PflYrzWwtpFAJWIBh0F3n.2vBt5EJiWzLrDClw8CwAO990JzCwFCC');-- password: pass

INSERT INTO public.users(
    name, password )
VALUES ('testAdminScopes',
        '$2a$08$PflYrzWwtpFAJWIBh0F3n.2vBt5EJiWzLrDClw8CwAO990JzCwFCC');-- password: pass


INSERT INTO public.role(
    user_id, role_name)
VALUES ( (SELECT id FROM users AS u WHERE u.name = 'testAdmin'), 'ADMIN');

INSERT INTO public.role(
    user_id, role_name)
VALUES ( (SELECT id FROM users AS u WHERE u.name = 'testAdminScopes'), 'ADMIN_SCOPES');

INSERT INTO public.scope(title, permit_all)
VALUES ('testScope', true);

INSERT INTO public.scope(title, description, permit_all)
VALUES ('testScope2', 'scope for admin_scopes', false);

INSERT INTO public.user_scope(
    user_id, scope_id)
VALUES ((SELECT id FROM users AS u WHERE u.name = 'testAdminScopes'), (SELECT id FROM scope AS s WHERE s.title = 'testScope2') );



INSERT INTO public.folder(scope_id, title)
VALUES (  (SELECT id FROM scope AS s WHERE s.title = 'testScope'), 'testFolder');

INSERT INTO public.version(
    version_number, folder_id)
VALUES ('1.0', (SELECT id FROM folder AS f WHERE f.title = 'testFolder') );

INSERT INTO public.tag(
    letter, bkg_color, letter_color)
VALUES ('A', 'blue', 'white');

INSERT INTO public.tag(
    letter, bkg_color, letter_color)
VALUES ('M', 'gray','white');

INSERT INTO public.tag(
    letter, bkg_color, letter_color)
VALUES ('D', 'green', 'white');

INSERT INTO public.tag(
    letter, bkg_color, letter_color)
VALUES ('P', 'purple', 'white');