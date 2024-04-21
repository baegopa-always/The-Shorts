create table ad_type
(
    type_id    int auto_increment
        primary key,
    type_title varchar(10) not null
);

create table role
(
    role_id   int auto_increment
        primary key,
    role_type varchar(6) not null
);

create table member
(
    member_id       int auto_increment
        primary key,
    username        varchar(10) not null,
    email           varchar(30) not null,
    role_id         int         not null,
    social_provider varchar(10) not null,
    social_id       int         not null,
    password        varchar(45) null,
    constraint email_UNIQUE
        unique (email),
    constraint role_type
        foreign key (role_id) references role (role_id)
);

create index role_type_idx
    on member (role_id);

create table sales
(
    sales_id     int auto_increment
        primary key,
    user_id      int                                 not null,
    sales_amount int                                 not null,
    sales_date   timestamp default CURRENT_TIMESTAMP not null,
    constraint sales_user_id
        foreign key (user_id) references member (member_id)
);

create index sales_user_id_idx
    on sales (user_id);

create table video
(
    video_id    int auto_increment
        primary key,
    member_id   int                                 not null,
    title       varchar(30)                         not null,
    upload_date timestamp default CURRENT_TIMESTAMP not null,
    length      int                                 not null,
    constraint user_video
        foreign key (member_id) references member (member_id)
);

create table ad
(
    ad_id    int auto_increment
        primary key,
    video_id int not null,
    type_id  int not null,
    constraint ad_type
        foreign key (type_id) references ad_type (type_id),
    constraint video_ad
        foreign key (video_id) references video (video_id)
);

create index ad_type_idx
    on ad (type_id);

create index video_ad_idx
    on ad (video_id);

create table playback_history
(
    playback_id int auto_increment
        primary key,
    video_id    int                                 not null,
    user_id     int                                 not null,
    playtime    int                                 not null,
    play_date   timestamp default CURRENT_TIMESTAMP not null,
    constraint played_video
        foreign key (video_id) references video (video_id),
    constraint video_player
        foreign key (user_id) references member (member_id)
);

create index played_video_idx
    on playback_history (video_id);

create index video_player_idx
    on playback_history (user_id);

create table statistics
(
    stats_id      int auto_increment
        primary key,
    video_id      int                                 not null,
    video_views   int                                 not null,
    playback_time int                                 not null,
    stats_date    timestamp default CURRENT_TIMESTAMP not null,
    ad_views      int                                 not null,
    constraint check_video_id
        foreign key (video_id) references video (video_id)
);

create index check_video_id_idx
    on statistics (video_id);

create index user_video_idx
    on video (member_id);

