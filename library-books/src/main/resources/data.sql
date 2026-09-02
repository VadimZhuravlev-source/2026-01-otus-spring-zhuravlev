insert into authors(id, full_name) values (1, 'Fyodor Dostoevsky');
insert into authors(id, full_name) values (2, 'Jules Verne');
insert into authors(id, full_name) values (3, 'George Orwell');

insert into genres(id, name) values (1, 'Novel');
insert into genres(id, name) values (2, 'Adventure');
insert into genres(id, name) values (3, 'Dystopia');

insert into books(id, title, author_id, genre_id) values (1, 'Crime and Punishment', 1, 1);
insert into books(id, title, author_id, genre_id) values (2, 'Twenty Thousand Leagues Under the Seas', 2, 2);
insert into books(id, title, author_id, genre_id) values (3, 'Nineteen Eighty-Four', 3, 3);

insert into book_comments(id, text, book_id) values (1, 'Strong psychological novel', 1);
insert into book_comments(id, text, book_id) values (2, 'Classic adventure story', 2);
insert into book_comments(id, text, book_id) values (3, 'Important dystopian novel', 3);

insert into users(id, username, password, role) values (1, 'user', '{noop}password', 'ROLE_USER');
insert into users(id, username, password, role) values (2, 'admin', '{noop}password', 'ROLE_ADMIN');

alter table authors alter column id restart with 4;
alter table genres alter column id restart with 4;
alter table books alter column id restart with 4;
alter table book_comments alter column id restart with 4;
alter table users alter column id restart with 3;