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
    fcm_token_payload varchar(200) not null default '',
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

create table member_log
(
    id                          bigint auto_increment primary key,
    member_id                   bigint,

    -- 최근 접속 일자
    last_attendance_date_time datetime(6) not null default CURRENT_TIMESTAMP (6),

    -- 연속 접속일 수
    continuous_attendance_count int not null default 1,

    -- 주간 뉴스 읽은 횟수
    weeklyReadCount             int not null default 0,

    -- 공유 횟수
    shared_count                int not null default 0,

    -- 뉴스를 저장한 갯수
    saved_news_count            int not null default 0,

    -- 뉴스 카드를 저장한 갯수
    saved_news_card_count       int not null default 0,

    -- 저장한 뉴스를 읽은 횟수
    read_news_count             int not null default 0,

    created_at                datetime(6) not null default CURRENT_TIMESTAMP (6),
    modified_at               datetime(6) not null default CURRENT_TIMESTAMP (6)
);

create table member_badge
(
    id                                          bigint auto_increment primary key,
    member_id                                   bigint,

    -- 작심삼일 - 3일 연속 접속
    three_days_continuous_attendance            boolean not null default false,
    three_days_continuous_attendance_created_at datetime(6) not null default CURRENT_TIMESTAMP (6),

    -- 단골손님 - 10일 연속 접속
    ten_days_continuous_attendance              boolean not null default false,
    ten_days_continuous_attendance_created_at   datetime(6) not null default CURRENT_TIMESTAMP (6),

    -- 세상 탐험가 뱃지 - 1주일 간 뉴스를 20개 저장
    explorer                                    boolean not null default false,
    explorer_created_at                         datetime(6) not null default CURRENT_TIMESTAMP (6),

    -- 뿌듯한 첫 공유 - 첫 공유 시도
    king_of_sharing                             boolean not null default false,
    king_of_sharing_created_at                  datetime(6) not null default CURRENT_TIMESTAMP (6),

    -- 설레는 첫 저장 뱃지 - 첫 뉴스 저장
    first_news_saving                           boolean not null default false,
    first_news_saving_created_at                datetime(6) not null default CURRENT_TIMESTAMP (6),

    -- 시작이 반 - 처음으로 뉴스를 다 읽음
    first_clear_news                            boolean not null default false,
    first_clear_news_created_at                 datetime(6) not null default CURRENT_TIMESTAMP (6),

    -- 취향 존중 뱃지
    change_mode                                 boolean not null default false,
    change_mode_created_at                      datetime(6) not null default CURRENT_TIMESTAMP (6),

    created_at                                  datetime(6) not null default CURRENT_TIMESTAMP (6),
    modified_at                                 datetime(6) not null default CURRENT_TIMESTAMP (6)
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
    member_id   bigint  not null,
    news_id     bigint  not null,
    read_at     datetime(6) not null default CURRENT_TIMESTAMP (6),
    deleted     boolean not null default false,
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
    member_id    bigint  not null,
    news_card_id bigint  not null,
    deleted      boolean not null default false,
    created_at   datetime(6) not null,
    modified_at  datetime(6) not null
);

insert into category(name, created_at, modified_at)
values ('POLITICS', now(), now());

insert into category(name, created_at, modified_at)
values ('ECONOMIC', now(), now());

insert into category(name, created_at, modified_at)
values ('SOCIETY', now(), now());

insert into category(name, created_at, modified_at)
values ('CULTURE', now(), now());

insert into category(name, created_at, modified_at)
values ('WORLD', now(), now());

insert into category(name, created_at, modified_at)
values ('SCIENCE', now(), now());

CREATE INDEX news_index ON news (created_at);
ANALYZE
TABLE news;
