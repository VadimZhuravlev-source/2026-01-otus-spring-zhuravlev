insert into authors(id, full_name) values (1, 'Fyodor Dostoevsky');
insert into authors(id, full_name) values (2, 'Jules Verne');
insert into authors(id, full_name) values (3, 'George Orwell');

insert into genres(id, name) values (1, 'Novel');
insert into genres(id, name) values (2, 'Adventure');
insert into genres(id, name) values (3, 'Dystopia');

insert into books(id, title, author_id, genre_id) values (1, 'Crime and Punishment', 1, 1);
insert into books(id, title, author_id, genre_id) values (2, 'Twenty Thousand Leagues Under the Seas', 2, 2);
insert into books(id, title, author_id, genre_id) values (3, 'Nineteen Eighty-Four', 3, 3);

alter table authors alter column id restart with 4;
alter table genres alter column id restart with 4;
alter table books alter column id restart with 4;