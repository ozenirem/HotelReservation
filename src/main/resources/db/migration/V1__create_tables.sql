create schema if not exists otel_reservation;

create type room_type
    as enum (
    'SINGLE',
    'DOUBLE',
    'SUITE',
    'DELUXE',
    'FAMILY',
    'EXECUTIVE',
    'PRESIDENTIAL'
    );


create table guest
(
    id           serial primary key                  not null,
    first_name   varchar(50)                         not null,
    last_name    varchar(50)                         not null,
    email        varchar(100)                 unique not null,
    phone_number varchar(20),
    created_at   timestamp default current_timestamp not null,
    updated_at   timestamp default current_timestamp not null
);


create table room
(
    id               serial primary key                  not null,
    room_number      varchar(10)                  unique not null,
    room_type        room_type                           not null,
    description      text,
    capacity         int                                 not null,
    price_per_night  decimal(10, 2)                      not null,
    created_at       timestamp default current_timestamp not null,
    updated_at       timestamp default current_timestamp not null
);


create table reservation
(
    id               serial primary key                    not null,
    guest_id         int                                   not null,
    room_id          int                                   not null,
    check_in_date    date                                  not null,
    check_out_date   date                                  not null,
    number_of_people int                                   not null,
    total_price      decimal(10, 2)                        not null,
    created_at       timestamp default current_timestamp   not null,
    updated_at       timestamp default current_timestamp   not null
);


-- Foreign key constraints
alter table reservation
    add constraint fk_guest
    foreign key (guest_id) references guest(id);

alter table reservation
    add constraint fk_room
    foreign key (room_id) references room(id);
