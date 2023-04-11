create table USERS
(
    ID              BIGINT auto_increment PRIMARY KEY,
    MAIL            CHARACTER VARYING(255),
    PASSWORD_HASH   CHARACTER VARYING(255) not null,
    UNIQUE(MAIL)
);