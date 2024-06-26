DROP TABLE TRVL_PL;

CREATE TABLE TRVL_PL
(
    PM_ID    NUMBER(3) NOT NULL,
    TRVL_CD    NUMBER(10) NOT NULL,
    PLAN_ID    NUMBER(20) NOT NULL,
    MANAGEMENT_ID    NUMBER(10) NOT NULL,
    TRVL_DAY    VARCHAR2(1) NOT NULL,
    TRVL_DTIME    VARCHAR2(5) NULL,
    TRAFFIC_TIME    VARCHAR2(10) NULL,
    DAY_WEEK    VARCHAR2(3) NULL,
    TRVL_STIME    VARCHAR2(5) NULL,
    COMMENTS    CLOB
);

COMMENT ON COLUMN TRVL_PL.PM_ID IS '여행지관리아이디';

COMMENT ON COLUMN TRVL_PL.TRVL_CD IS '여행지코드';

COMMENT ON COLUMN TRVL_PL.PLAN_ID IS '여행일정아이디';

COMMENT ON COLUMN TRVL_PL.MANAGEMENT_ID IS '내부관리용아이디';

COMMENT ON COLUMN TRVL_PL.TRVL_DAY IS '여행일';

COMMENT ON COLUMN TRVL_PL.TRVL_DTIME IS '여행일활동시간';

COMMENT ON COLUMN TRVL_PL.TRAFFIC_TIME IS '교통시간';

COMMENT ON COLUMN TRVL_PL.DAY_WEEK IS '요일';

COMMENT ON COLUMN TRVL_PL.TRVL_STIME IS '여행지활동시간';

COMMENT ON COLUMN TRVL_PL.COMMENTS IS '코멘트';

COMMENT ON TABLE TRVL_PL IS '여행일정';

CREATE UNIQUE INDEX TRVL_PL_PK ON TRVL_PL
( PM_ID,PLAN_ID,MANAGEMENT_ID,TRVL_CD );

ALTER TABLE TRVL_PL
 ADD CONSTRAINT TRVL_PL_PK PRIMARY KEY ( PM_ID,PLAN_ID,MANAGEMENT_ID,TRVL_CD )
 USING INDEX TRVL_PL_PK;

ALTER TABLE TRVL_PL
ADD CONSTRAINT TRVL_PLTB_PLAN_ID_MANAGEMENT_ID_FK
FOREIGN KEY (PLAN_ID,MANAGEMENT_ID)
REFERENCES TRVL_LST(PLAN_ID,MANAGEMENT_ID);

ALTER TABLE TRVL_PL
ADD CONSTRAINT TRVL_PLTB_TRVL_CD_FK
FOREIGN KEY (TRVL_CD)
REFERENCES TRVL_SP(TRVL_CD);

ALTER TABLE TRVL_PL
MODIFY TRVL_STIME VARCHAR2(8);

ALTER TABLE TRVL_PL
MODIFY TRVL_DTIME VARCHAR2(8);
