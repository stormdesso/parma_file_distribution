INSERT INTO users(
    name, password )
VALUES ('testAdmin',
        '$2a$08$PflYrzWwtpFAJWIBh0F3n.2vBt5EJiWzLrDClw8CwAO990JzCwFCC');

INSERT INTO users(
    name, password )
VALUES ('testAdminScopes',
        '$2a$08$PflYrzWwtpFAJWIBh0F3n.2vBt5EJiWzLrDClw8CwAO990JzCwFCC');


INSERT INTO role(
    user_id, role_name)
VALUES ((SELECT id FROM users AS u WHERE u.name = 'testAdmin'), 'ADMIN');

INSERT INTO role(
    user_id, role_name)
VALUES ((SELECT id FROM users AS u WHERE u.name = 'testAdminScopes'), 'ADMIN_SCOPES');

INSERT INTO scope(title)
VALUES ('testScope');

INSERT INTO scope(title, description)
VALUES ('testScope2', 'scope for admin_scopes');

INSERT INTO user_scope(
    user_id, scope_id)
VALUES ((SELECT id FROM users AS u WHERE u.name = 'testAdminScopes'), (SELECT id FROM scope AS s WHERE s.title = 'testScope2') );



INSERT INTO folder(scope_id, title)
VALUES (  (SELECT id FROM scope AS s WHERE s.title = 'testScope'), 'testFolder');

INSERT INTO version(
    version_number, folder_id)
VALUES ('1.0', (SELECT id FROM folder AS f WHERE f.title = 'testFolder') );

INSERT INTO tag(
    letter, bkg_color, letter_color)
VALUES ('A', 'blue', 'white');

INSERT INTO tag(
    letter, bkg_color, letter_color)
VALUES ('M', 'gray','white');

INSERT INTO tag(
    letter, bkg_color, letter_color)
VALUES ('D', 'green', 'white');

INSERT INTO tag(
    letter, bkg_color, letter_color)
VALUES ('P', 'purple', 'white');