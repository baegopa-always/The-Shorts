create table advertisement
(
    ad_id bigint unsigned not null
        primary key,
    title varchar(20)     not null
);

create table member
(
    member_id bigint unsigned auto_increment
        primary key,
    username  varchar(10)  not null,
    email     varchar(200) not null,
    password  varchar(300) null,
    role      varchar(6)   not null,
    constraint email_UNIQUE
        unique (email)
);

create table refresh_token
(
    token_id      int auto_increment
        primary key,
    member_id     int          not null,
    refresh_token varchar(300) not null
);

create table video
(
    video_id       bigint unsigned auto_increment
        primary key,
    member_id      bigint unsigned                           not null,
    title          varchar(30)                               not null,
    upload_date    timestamp       default CURRENT_TIMESTAMP not null,
    length         bigint unsigned                           not null,
    total_playtime bigint unsigned default '0'               not null,
    video_views    bigint unsigned default '0'               not null,
    constraint video_owner
        foreign key (member_id) references member (member_id)
);

create table play_history
(
    play_id         bigint auto_increment
        primary key,
    user_id         bigint unsigned                           not null,
    video_id        bigint unsigned                           not null,
    last_watch_time bigint unsigned default '0'               not null,
    play_date       timestamp       default CURRENT_TIMESTAMP not null,
    constraint user_id_key
        foreign key (user_id) references member (member_id),
    constraint video_id_key
        foreign key (video_id) references video (video_id)
);

create index user_id_key_idx
    on play_history (user_id);

create index video_id_key_idx
    on play_history (video_id);

create table sales
(
    sales_id       bigint auto_increment
        primary key,
    video_id       bigint unsigned                           not null,
    sales_amount   bigint unsigned                           not null,
    sales_date     timestamp       default CURRENT_TIMESTAMP not null,
    daily_views    bigint unsigned default '0'               not null,
    daily_ad_views bigint unsigned default '0'               not null,
    daily_playtime bigint unsigned default '0'               not null,
    user_id        bigint unsigned                           not null,
    constraint userId_sales
        foreign key (user_id) references member (member_id),
    constraint video_sales
        foreign key (video_id) references play_history (video_id)
);

create index userId_sales_idx
    on sales (user_id);

create index video_sales_idx
    on sales (video_id);

create index user_video_key_idx
    on video (member_id);

create table video_advertisement
(
    played_ad_id bigint unsigned auto_increment
        primary key,
    video_id     bigint unsigned                     not null,
    ad_id        bigint unsigned                     not null,
    ad_timestamp timestamp default CURRENT_TIMESTAMP not null,
    constraint video_ad_id
        foreign key (ad_id) references advertisement (ad_id),
    constraint video_ad_key
        foreign key (video_id) references video (video_id)
);

create index video_ad_id_idx
    on video_advertisement (ad_id);

create index video_ad_key_idx
    on video_advertisement (video_id);

