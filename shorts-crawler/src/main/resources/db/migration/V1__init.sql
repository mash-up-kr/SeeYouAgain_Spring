CREATE TABLE IF NOT EXISTS shorts.category
(
    id          bigint auto_increment primary key,
    name        varchar(15) not null unique key,
    created_at  datetime(6) not null,
    modified_at datetime(6) null
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS shorts.member
(
    id          bigint primary key,
    unique_id   varchar(100) not null,
    nickname    varchar(20)  not null,
    created_at  datetime(6)  not null,
    modified_at datetime(6)  null
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS shorts.member_category
(
    id          bigint auto_increment primary key,
    category_id bigint      null,
    member_id   bigint      null,
    created_at  datetime(6) not null,
    modified_at datetime(6) null,

    foreign key (category_id) references shorts.category (id),
    foreign key (member_id) references shorts.member (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS shorts.news
(
    id                  bigint auto_increment primary key,
    title               varchar(200)   not null,
    content             varchar(10000) not null,
    news_link           varchar(500)   not null,
    press               varchar(20)    not null,
    thumbnail_image_url varchar(255)   not null,
    type                varchar(10)    not null,
    written_date_time   varchar(30)    not null,
    crawled_count       int            not null,
    category_id         bigint         not null,
    created_at          datetime(6)    not null,
    modified_at         datetime(6)    null,

    foreign key (category_id) references shorts.category (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS shorts.news_card
(
    id            bigint auto_increment primary key,
    multiple_news varchar(5000) null,
    keywords      varchar(100)  null,
    category_id   bigint        not null,
    created_at    datetime(6)   not null,
    modified_at   datetime(6)   null,
    foreign key (category_id) references shorts.category (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS shorts.member_news
(
    id          bigint auto_increment primary key,
    member_id   bigint      null,
    news_id     bigint      null,
    created_at  datetime(6) not null,
    modified_at datetime(6) null,
    foreign key (news_id) references shorts.news (id),
    foreign key (member_id) references shorts.member (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

insert into shorts.category(name, created_at, modified_at)
values ("POLITICS", now(), now());

insert into shorts.category(name, created_at, modified_at)
values ("ECONOMIC", now(), now());

insert into shorts.category(name, created_at, modified_at)
values ("SOCIETY", now(), now());

insert into shorts.category(name, created_at, modified_at)
values ("CULTURE", now(), now());

insert into shorts.category(name, created_at, modified_at)
values ("WORLD", now(), now());

insert into shorts.category(name, created_at, modified_at)
values ("SCIENCE", now(), now());
