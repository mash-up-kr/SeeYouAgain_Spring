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
    created_at      datetime(6) not null,
    modified_at     datetime(6) null
);

create table member
(
    id                bigint auto_increment primary key,
    unique_id         varchar(100) not null,
    nickname          varchar(20)  not null,
    fcm_token_payload varchar(200) not null,
    is_allowed_alarm  boolean      not null default true,
    show_mode         varchar(30),
    created_at        datetime(6) not null,
    modified_at       datetime(6) null
);

create table member_category
(
    id          bigint auto_increment primary key,
    category_id bigint not null,
    member_id   bigint not null,
    created_at  datetime(6) not null,
    modified_at datetime(6) not null
);

create table member_company
(
    id          bigint auto_increment primary key,
    member_id   bigint       not null,
    company     varchar(100) not null,
    created_at  datetime default CURRENT_TIMESTAMP null,
    modified_at datetime default CURRENT_TIMESTAMP null
);

create table member_shorts_count
(
    id          bigint auto_increment primary key,
    member_id   bigint not null,
    count       int    not null,
    target_date date   not null,
    created_at  datetime(6) not null,
    modified_at datetime(6) not null
);

create table member_log
(
    id                         bigint auto_increment primary key,
    member_id                  bigint,

    -- 연속 접속일 수
    continuous_count           int not null default 0,

    -- 1주일 동안 숏스를 읽은 갯수 이 컬럼은 매주 월요일 00시마다 0으로 초기화 되어야한다.
    continuous_week_read_count int not null default 0,

    -- 공유 횟수
    shared_count               int not null default 0,

    -- 저장한 오늘의 숏스의 모든 갯수
    saved_today_shorts_count   bigint not null default 0,

    -- 저장한 오래 간직할 숏스의 모든 갯수
    saved_old_shorts_count     bigint not null default 0,

    created_at                 datetime(6),
    modified_at                datetime(6)
);

create table member_badge
(
    id                                          bigint auto_increment primary key,
    member_id                                   bigint,

    -- 작심삼일 뱃지
    three_days_continuous_attendance            boolean not null default false,
    three_days_continuous_attendance_created_at datetime,

    -- 단골손님 뱃지
    ten_days_continuous_attendance              boolean not null default false,
    ten_days_continuous_attendance_created_at   datetime,

    -- 세상 탐험가 뱃지
    king_of_read                                boolean not null default false,
    king_of_read_created_at                     datetime,

    -- 뿌듯한 첫 공유 뱃지
    king_of_sharing                             boolean not null default false,
    king_of_sharing_created_at                  datetime,

    -- 설레는 첫 저장 뱃지
    first_today_shorts_saving                   boolean not null default false,
    first_today_shorts_saving_created_at        datetime,

    -- 시작이 반 뱃지
    first_all_read_shorts                       boolean not null default false,
    first_all_read_shorts_created_at            datetime,

    -- 오래 간직될 지식 뱃지
    first_old_shorts_saving                     boolean not null default false,
    first_old_shorts_saving_created_at          datetime,

    -- 취향 존중 뱃지
    change_mode                                 boolean not null default false,
    change_mode_created_at                      datetime
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
    member_id   bigint not null,
    news_id     bigint not null,
    created_at  datetime(6) not null,
    modified_at datetime(6) not null
);

create table news_card
(
    id            bigint auto_increment primary key,
    multiple_news text         not null,
    keywords      varchar(100) not null,
    category_id   bigint       not null,
    created_at    datetime(6) not null,
    modified_at   datetime(6) not null
);

create table member_news_card
(
    id           bigint auto_increment primary key,
    member_id    bigint not null,
    news_card_id bigint not null,
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

CREATE INDEX news_index ON news (created_at);
ANALYZE
TABLE news;
