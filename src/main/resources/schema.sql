/*
drop table if exists users;
drop table if exists items;
*/

/*
create table if not exists users
(
    id int primary key auto_increment (1) ,
    name varchar(50),
    email varchar(50) unique
);

create table if not exists items
(
    id int primary key auto_increment (1),
    owner_id int references users(id),
    name varchar(50) not null,
    description varchar(50),
    available boolean,
    tenant_id int references users(id)
);

create table if not exists bookings
(
    id int primary key auto_increment (1) ,
    item_id int references items(id),
    first_date date,
    last_date date,
    feedback varchar(200),
    status varchar(30)
)
*/



