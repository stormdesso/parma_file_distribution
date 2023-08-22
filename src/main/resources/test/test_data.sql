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

INSERT INTO public.scope(title)
VALUES ('testScope');

INSERT INTO public.scope(title, description)
VALUES ('testScope2', 'scope for admin_scopes');

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






INSERT INTO public.users(
    name, password, can_create_and_delete_scope )
VALUES ('testAdminScopes2',
        '$2a$08$PflYrzWwtpFAJWIBh0F3n.2vBt5EJiWzLrDClw8CwAO990JzCwFCC', true);-- password: pass


INSERT INTO public.role(
    user_id, role_name)
VALUES ( (SELECT id FROM users AS u WHERE u.name = 'testAdminScopes2'), 'ADMIN_SCOPES');



INSERT INTO public.users(
    name, password, is_admin_manager, is_admin_scope_manager, can_create_and_delete_scope )
VALUES ('testAdmin2',
        '$2a$08$PflYrzWwtpFAJWIBh0F3n.2vBt5EJiWzLrDClw8CwAO990JzCwFCC', false, false, true);-- password: pass

INSERT INTO public.role(
    user_id, role_name)
VALUES ( (SELECT id FROM users AS u WHERE u.name = 'testAdmin2'), 'ADMIN');




INSERT INTO public.users(
    name, password, is_admin_manager, is_admin_scope_manager, can_create_and_delete_scope )
VALUES ('testRoot',
        '$2a$08$PflYrzWwtpFAJWIBh0F3n.2vBt5EJiWzLrDClw8CwAO990JzCwFCC', true, true, true);-- password: pass

INSERT INTO public.role(
    user_id, role_name)
VALUES ( (SELECT id FROM users AS u WHERE u.name = 'testRoot'), 'ROOT');