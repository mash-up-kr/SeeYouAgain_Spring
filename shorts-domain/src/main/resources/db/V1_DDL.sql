create table category
(
    id          bigint auto_increment primary key,
    name        varchar(15) not null unique key,
    created_at  datetime(6) not null,
    modified_at datetime(6) null
);

create table hot_keyword
(
    id              bigint auto_increment primary key,
    keyword_ranking varchar(200) not null,
    created_at      datetime(6)  not null,
    modified_at     datetime(6)  null
);

create table member
(
    id          bigint auto_increment primary key,
    unique_id   varchar(100) not null,
    nickname    varchar(20)  not null,
    created_at  datetime(6)  not null,
    modified_at datetime(6)  null
);

create table member_category
(
    id          bigint auto_increment primary key,
    category_id bigint      not null,
    member_id   bigint      not null,
    created_at  datetime(6) not null,
    modified_at datetime(6) not null
);

create table member_shorts_count
(
    id          bigint auto_increment primary key,
    member_id   bigint      not null,
    count       int         not null,
    target_date date        not null,
    created_at  datetime(6) not null,
    modified_at datetime(6) not null
);

create table news
(
    id                  bigint auto_increment primary key,
    title               text        not null,
    content             text        not null,
    news_link           text        not null,
    press               varchar(20) not null,
    thumbnail_image_url text        not null,
    type                varchar(10) not null,
    written_date_time   varchar(30) not null,
    crawled_count       int         not null,
    category_id         bigint      not null,
    created_at          datetime(6) not null,
    modified_at         datetime(6) not null
);

create table member_news
(
    id          bigint auto_increment primary key,
    member_id   bigint      not null,
    news_id     bigint      not null,
    created_at  datetime(6) not null,
    modified_at datetime(6) not null
);

create table news_card
(
    id            bigint auto_increment primary key,
    multiple_news text         not null,
    keywords      varchar(100) not null,
    category_id   bigint       not null,
    created_at    datetime(6)  not null,
    modified_at   datetime(6)  not null
);

create table member_news_card
(
    id           bigint auto_increment primary key,
    member_id    bigint      not null,
    news_card_id bigint      not null,
    created_at   datetime(6) not null,
    modified_at  datetime(6) not null
);

insert into category(name, created_at, modified_at)
values ("POLITICS", now(), now());

insert into category(name, created_at, modified_at)
values ("ECONOMIC", now(), now());

insert into category(name, created_at, modified_at)
values ("SOCIETY", now(), now());

insert into category(name, created_at, modified_at)
values ("CULTURE", now(), now());

insert into category(name, created_at, modified_at)
values ("WORLD", now(), now());

insert into category(name, created_at, modified_at)
values ("SCIENCE", now(), now());
