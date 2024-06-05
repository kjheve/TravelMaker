--------------------------------------------------------
--  Ref Constraints for Table BBS
--------------------------------------------------------

  ALTER TABLE "BBS" ADD CONSTRAINT "BBSTB_CODE_ID_FK" FOREIGN KEY ("CODE_ID")
	  REFERENCES "CLASS_CD" ("CODE_ID") ENABLE;
  ALTER TABLE "BBS" ADD CONSTRAINT "BBSTB_MANAGEMENT_ID_FK" FOREIGN KEY ("MANAGEMENT_ID")
	  REFERENCES "MEMBER" ("MANAGEMENT_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CLASS_CD
--------------------------------------------------------

  ALTER TABLE "CLASS_CD" ADD CONSTRAINT "CLASS_CDTB_CODE_ID_FK" FOREIGN KEY ("PCODE_ID")
	  REFERENCES "CLASS_CD" ("CODE_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table GB
--------------------------------------------------------

  ALTER TABLE "GB" ADD CONSTRAINT "FK_GB_MANAGEMENT_ID" FOREIGN KEY ("MANAGEMENT_ID")
	  REFERENCES "MEMBER" ("MANAGEMENT_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MEMBER
--------------------------------------------------------

  ALTER TABLE "MEMBER" ADD CONSTRAINT "MEMBERTB_CODE_ID_FK" FOREIGN KEY ("CODE_ID")
	  REFERENCES "CLASS_CD" ("CODE_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table RBBS
--------------------------------------------------------

  ALTER TABLE "RBBS" ADD CONSTRAINT "RBBSTB_MANAGEMENT_ID_FK" FOREIGN KEY ("MANAGEMENT_ID")
	  REFERENCES "MEMBER" ("MANAGEMENT_ID") ENABLE;
  ALTER TABLE "RBBS" ADD CONSTRAINT "RBBSTB_BBS_ID_FK" FOREIGN KEY ("BBS_ID")
	  REFERENCES "BBS" ("BBS_ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TRVL_LST
--------------------------------------------------------

  ALTER TABLE "TRVL_LST" ADD CONSTRAINT "TRVL_LSTTB_MANAGEMENT_ID_FK" FOREIGN KEY ("MANAGEMENT_ID")
	  REFERENCES "MEMBER" ("MANAGEMENT_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TRVL_PL
--------------------------------------------------------

  ALTER TABLE "TRVL_PL" ADD CONSTRAINT "TRVL_PLTB_PLAN_ID_MANAGEMENT_ID_FK" FOREIGN KEY ("PLAN_ID", "MANAGEMENT_ID")
	  REFERENCES "TRVL_LST" ("PLAN_ID", "MANAGEMENT_ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE "TRVL_PL" ADD CONSTRAINT "TRVL_PLTB_TRVL_CD_FK" FOREIGN KEY ("TRVL_CD")
	  REFERENCES "TRVL_SP" ("TRVL_CD") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TRVL_SP
--------------------------------------------------------

  ALTER TABLE "TRVL_SP" ADD CONSTRAINT "FK_TRVL_SP_RAGION_CD" FOREIGN KEY ("RAGION_CD")
	  REFERENCES "AREA_CD" ("RAGION_CD") ENABLE;
  ALTER TABLE "TRVL_SP" ADD CONSTRAINT "FK_TRVL_SP_TRSP_CLA" FOREIGN KEY ("TRSP_CLA")
	  REFERENCES "CLASS_CD" ("CODE_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table UPLOAD_FL
--------------------------------------------------------

  ALTER TABLE "UPLOAD_FL" ADD CONSTRAINT "UPLOAD_FLTB_BBS_ID_FK" FOREIGN KEY ("BBS_ID")
	  REFERENCES "BBS" ("BBS_ID") ENABLE;
