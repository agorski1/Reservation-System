ALTER SESSION SET CONTAINER = XEPDB1;
ALTER SESSION SET CURRENT_SCHEMA = RS_USER;
-- 02-rs-schema.sql
create sequence RS_USER.USER_ROLE_SEQ
    nocache
/

create sequence RS_USER.USER_SEQ
    nocache
/

create sequence RS_USER.PAYMENT_METHOD_SEQ
    nocache
/

create sequence RS_USER.PAYMENT_STATUS_SEQ
    nocache
/

create sequence RS_USER.PAYMENT_SEQ
    nocache
/

create sequence RS_USER.RESERVATION_SEQ
    nocache
/

create sequence RS_USER.ROOM_SEQ
    nocache
/

create sequence RS_USER.ROOM_TYPE_SEQ
    nocache
/

create sequence RS_USER.AMENITY_SEQ
    nocache
/

create sequence RS_USER.ROOM_AMENITY_SEQ
    nocache
/

create sequence RS_USER.ISEQ$$_75795
/

create sequence RS_USER.RESERVATION_STATUS_SEQ
    nocache
/

create sequence RS_USER.COUNTRY_SEQ
    nocache
/

create sequence RS_USER.ROOM_TYPE_AMENITY_SEQ
    nocache
/

create table RS_USER.USER_ROLES
(
    UR_ID   NUMBER default "RS_USER"."USER_ROLE_SEQ"."NEXTVAL" not null
        primary key,
    UR_NAME VARCHAR2(20)                                       not null
        unique
)
/

create table RS_USER.USERS
(
    USR_ID                NUMBER default "RS_USER"."USER_SEQ"."NEXTVAL" not null
        primary key,
    USR_EMAIL             VARCHAR2(100)                                 not null
        unique,
    USR_PASSWORD          VARCHAR2(60)                                  not null,
    USR_FIRST_NAME        VARCHAR2(100),
    USR_LAST_NAME         VARCHAR2(100),
    USR_PHONE_NUMBER      VARCHAR2(20),
    USR_REGISTRATION_DATE DATE   default SYSDATE,
    USR_STREET            VARCHAR2(255),
    USR_CITY              VARCHAR2(100),
    USR_ZIP_CODE          VARCHAR2(20),
    UR_ID                 NUMBER
        references RS_USER.USER_ROLES
)
/

create table RS_USER.PAYMENT_METHODS
(
    PMTM_ID   NUMBER default "RS_USER"."PAYMENT_METHOD_SEQ"."NEXTVAL" not null
        primary key,
    PMTM_NAME VARCHAR2(20)                                            not null
        unique
)
/

create table RS_USER.PAYMENT_STATUSES
(
    PMTS_ID   NUMBER default "RS_USER"."PAYMENT_STATUS_SEQ"."NEXTVAL" not null
        primary key,
    PMTS_NAME VARCHAR2(20)                                            not null
        unique
)
/

create table RS_USER.ROOM_TYPE
(
    RT_ID              NUMBER default "RS_USER"."ROOM_TYPE_SEQ"."NEXTVAL" not null
        primary key,
    RT_NAME            VARCHAR2(40)                                       not null,
    RT_CAPACITY        NUMBER,
    RT_PRICE_PER_NIGHT NUMBER(10, 2),
    RT_DESCRIPTION     VARCHAR2(200)
)
/

create table RS_USER.ROOMS
(
    RM_ID     NUMBER       default "RS_USER"."ROOM_SEQ"."NEXTVAL" not null
        primary key,
    RM_NUMBER NUMBER(4)                                           not null
        unique,
    RM_STATUS VARCHAR2(20) default 'ACTIVE'
        check (RM_status IN ('ACTIVE', 'UNDER_MAINTENANCE', 'DELETED')),
    RT_ID     NUMBER
        references RS_USER.ROOM_TYPE
)
/

create table RS_USER.AMENITIES
(
    AMN_ID   NUMBER default "RS_USER"."AMENITY_SEQ"."NEXTVAL" not null
        primary key,
    AMN_NAME VARCHAR2(50)                                     not null,
    AMN_CODE VARCHAR2(20)                                     not null
        unique
)
/

create table RS_USER.RESERVATION_STATUSES
(
    RSVS_ID   NUMBER default "RS_USER"."PAYMENT_STATUS_SEQ"."NEXTVAL" not null
        primary key,
    RSVS_NAME VARCHAR2(30)                                            not null
)
/

create table RS_USER.RESERVATIONS
(
    RSV_ID             NUMBER       default "RS_USER"."RESERVATION_SEQ".nextval not null
		primary key,
    RSV_CHECK_IN_DATE  DATE                                   not null,
    RSV_CHECK_OUT_DATE DATE                                   not null,
    RSV_GUEST_COUNT    NUMBER(2)
        check (RSV_guest_count > 0),
    RM_ID              NUMBER                                 not null
        references RS_USER.ROOMS,
    USR_ID             NUMBER                                 not null
        references RS_USER.USERS,
    RSVS_ID            NUMBER       default 1                 not null
        constraint FK_RESERVATIONS_STATUS
            references RS_USER.RESERVATION_STATUSES,
    RSV_CREATED_AT     TIMESTAMP(6) default CURRENT_TIMESTAMP not null
)
/

create table RS_USER.PAYMENTS
(
    PMT_ID             NUMBER default "RS_USER"."PAYMENT_SEQ"."NEXTVAL" not null
        primary key,
    PMT_AMOUNT         NUMBER(10, 2)                                    not null
        check (PMT_amount > 0),
    PMT_DATE           DATE   default SYSDATE,
    PMT_ACCOUNT_NUMBER VARCHAR2(34)
        check (PMT_account_number IS NULL OR
               REGEXP_LIKE(PMT_account_number, '^[A-Z0-9]{15,34}$')),
    RSV_ID             NUMBER                                           not null
        references RS_USER.RESERVATIONS,
    PMTM_ID            NUMBER                                           not null
        references RS_USER.PAYMENT_METHODS,
    PMTS_ID            NUMBER                                           not null
        references RS_USER.PAYMENT_STATUSES
)
/

create table RS_USER.ROOM_TYPE_AMENITIES
(
    RTA_ID NUMBER default "RS_USER"."ROOM_TYPE_AMENITY_SEQ"."NEXTVAL" not null
        primary key,
    RT_ID  NUMBER                                                     not null
        references RS_USER.ROOM_TYPE,
    AMN_ID NUMBER                                                     not null
        references RS_USER.AMENITIES
)
/

