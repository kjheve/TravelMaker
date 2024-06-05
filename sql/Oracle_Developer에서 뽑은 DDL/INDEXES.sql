--------------------------------------------------------
--  DDL for Index AREA_CD_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "AREA_CD_PK" ON "AREA_CD" ("RAGION_CD") 
  ;
--------------------------------------------------------
--  DDL for Index BBS_PK_IDX
--------------------------------------------------------

  CREATE INDEX "BBS_PK_IDX" ON "BBS" ("BBS_ID") 
  ;
--------------------------------------------------------
--  DDL for Index CLASS_CD_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CLASS_CD_PK" ON "CLASS_CD" ("CODE_ID") 
  ;
--------------------------------------------------------
--  DDL for Index MEMBERTB_MEMBER_ID_UK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MEMBERTB_MEMBER_ID_UK" ON "MEMBER" ("MEMBER_ID") 
  ;
--------------------------------------------------------
--  DDL for Index MEMBERTB_NICKNAME_UK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MEMBERTB_NICKNAME_UK" ON "MEMBER" ("NICKNAME") 
  ;
--------------------------------------------------------
--  DDL for Index MEMBER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MEMBER_PK" ON "MEMBER" ("MANAGEMENT_ID") 
  ;
--------------------------------------------------------
--  DDL for Index PK_GB_ID
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_GB_ID" ON "GB" ("GB_ID") 
  ;
--------------------------------------------------------
--  DDL for Index RBBS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "RBBS_PK" ON "RBBS" ("RBBS_ID", "BBS_ID", "MANAGEMENT_ID") 
  ;
--------------------------------------------------------
--  DDL for Index TRVL_LST_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRVL_LST_PK" ON "TRVL_LST" ("PLAN_ID", "MANAGEMENT_ID") 
  ;
--------------------------------------------------------
--  DDL for Index TRVL_PL_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRVL_PL_PK" ON "TRVL_PL" ("PM_ID", "PLAN_ID", "MANAGEMENT_ID", "TRVL_CD") 
  ;
--------------------------------------------------------
--  DDL for Index TRVL_SP_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRVL_SP_PK" ON "TRVL_SP" ("TRVL_CD") 
  ;
--------------------------------------------------------
--  DDL for Index UPLOAD_FL_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UPLOAD_FL_PK" ON "UPLOAD_FL" ("UPLOADFILE_ID", "BBS_ID", "MANAGEMENT_ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_TRVL_Y
--------------------------------------------------------

  CREATE INDEX "IDX_TRVL_Y" ON "TRVL_SP" ("TRVL_Y") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_TRAGION_NM
--------------------------------------------------------

  CREATE INDEX "IDX_TRAGION_NM" ON "AREA_CD" ("RAGION_NM") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_TRVL_X
--------------------------------------------------------

  CREATE INDEX "IDX_TRVL_X" ON "TRVL_SP" ("TRVL_X") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_TRSP_CLA
--------------------------------------------------------

  CREATE INDEX "IDX_TRSP_CLA" ON "TRVL_SP" ("TRSP_CLA") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_RAGION_CD
--------------------------------------------------------

  CREATE INDEX "IDX_RAGION_CD" ON "TRVL_SP" ("RAGION_CD") 
  ;
