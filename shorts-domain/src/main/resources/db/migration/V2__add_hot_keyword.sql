create table hot_keyword
(
    id            bigint auto_increment primary key,
    keyword_ranking      varchar(200) not null,
    created_at    datetime(6)  not null,
    modified_at   datetime(6)  null
);
