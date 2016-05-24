SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE TKFL;
DROP TABLE TMLY;
DROP TABLE TKXZX;
DROP TABLE ZSTK;
DROP TABLE YXTK;
DROP TABLE YXTKXZX;
DROP TABLE YXTKDA;
DROP TABLE GR_TK_GXJL;
DROP TABLE DEPT_TK_GXJL;
DROP TABLE TKDA;
DROP TABLE LAUD_RECORD;




/* Create Tables */

CREATE TABLE TKFL
(
	ID_ INT NOT NULL UNIQUE AUTO_INCREMENT COMMENT 'ID_',
	PARENT_ID INT COMMENT 'parent_id',
	TKMC VARCHAR(200) COMMENT '题库名称',
	MS VARCHAR(500) COMMENT '描述',
	PRIMARY KEY (ID_)
) COMMENT = '题库分类表';


CREATE TABLE TMLY
(
	ID_ INT NOT NULL UNIQUE AUTO_INCREMENT COMMENT 'ID_',
	TITLE VARCHAR(200) COMMENT '来源标题',
	CONTENT VARCHAR(2000) COMMENT '内容',
	PRIMARY KEY (ID_)
) COMMENT = '题目来源表';


CREATE TABLE TKXZX
(
	ID_ VARCHAR(50) COMMENT 'ID_',
	TK_ID VARCHAR(50) COMMENT '题目ID',
	-- A or B or C or D
	XZ_KEY CHAR(1) COMMENT '选项 ABCD : A or B or C or D',
	CONTENT VARCHAR(2000) COMMENT '选项内容'
) COMMENT = '题目选择项';


CREATE TABLE ZSTK
(
	ID_ VARCHAR(50) NOT NULL UNIQUE COMMENT 'ID_',
	FL_ID INT COMMENT '所属分类',
	USER_ID INT COMMENT '录入人ID',
	CREATE_DATE DATE COMMENT '创建日期',
	SP_DATE DATE COMMENT '审批日期',
	SPR_ID INT COMMENT '审批人ID',
	DEPTID INT COMMENT '部门ID',
	CONTENT VARCHAR(1000) COMMENT '题干内容',
	TMFZ DOUBLE COMMENT '题目分值',
	-- 1基础型，2进阶型
	TMND INT COMMENT '题目难度 : 1基础型，2进阶型',
	TMLY_ID INT COMMENT '题目来源ID',
	MODE CHAR(1) COMMENT '选择类型 1单选，2多选',
	PRIMARY KEY (ID_)
) COMMENT = '正式题库';


CREATE TABLE YXTK
(
	ID_ VARCHAR(50) NOT NULL UNIQUE COMMENT 'ID_',
	FL_ID INT COMMENT '所属分类',
	USER_ID INT COMMENT '录入人ID',
	CREATE_DATE DATE COMMENT '创建日期',
	SP_DATE DATE COMMENT '审批日期',
	SPR_ID INT COMMENT '审批人ID',
	DEPTID INT COMMENT '部门ID',
	CONTENT VARCHAR(1000) COMMENT '题干内容',
	TMFZ DOUBLE COMMENT '题目分值',
	-- 1基础型，2进阶型
	TMND INT COMMENT '题目难度 : 1基础型，2进阶型',
	TMLY_ID INT COMMENT '题目来源ID',
	MODE CHAR(1) COMMENT '选择类型 1单选，2多选',
	PRIMARY KEY (ID_)
) COMMENT = '预选题库';


CREATE TABLE YXTKXZX
(
	ID_ VARCHAR(50) COMMENT 'ID_',
	TK_ID VARCHAR(50) COMMENT '题目ID',
	-- A or B or C or D
	XZ_KEY CHAR(1) COMMENT '选项 ABCD : A or B or C or D',
	CONTENT VARCHAR(2000) COMMENT '选项内容'
) COMMENT = '预选题目选择项';


CREATE TABLE YXTKDA
(
	ID_ VARCHAR(50) COMMENT 'ID_',
	TK_ID VARCHAR(50) COMMENT '题目ID',
	TKXZXID VARCHAR(50) COMMENT '题目选择项ID'
) COMMENT = '预选题目答案';


CREATE TABLE GR_TK_GXJL
(
	ID_ VARCHAR(50) NOT NULL UNIQUE COMMENT 'ID_',
	DEPT_ID INT COMMENT '部门ID',
	TK_ID VARCHAR(50) COMMENT '题目ID',
	GXZ DOUBLE COMMENT '贡献值',
	GXLY INT COMMENT '贡献方式 1预选库出题，2被选入正式库',
	GX_DATE DATE COMMENT '获取时间',
	PRIMARY KEY (ID_)
) COMMENT = '个人题库贡献记录';


CREATE TABLE DEPT_TK_GXJL
(
	ID_ VARCHAR(50) NOT NULL UNIQUE COMMENT 'ID_',
	DEPT_ID INT COMMENT '部门ID',
	TK_ID VARCHAR(50) COMMENT '题目ID',
	GXZ DOUBLE COMMENT '贡献值',
	GXLY INT COMMENT '贡献方式 1预选库出题，2被选入正式库',
	GX_DATE DATE COMMENT '获取时间',
	PRIMARY KEY (ID_)
) COMMENT = '部门题库贡献记录';


CREATE TABLE TKDA
(
	ID_ VARCHAR(50) COMMENT 'ID_',
	TK_ID VARCHAR(50) COMMENT '题目ID',
	TKXZXID VARCHAR(50) COMMENT '题目选择项ID'
) COMMENT = '题目答案';


CREATE TABLE LAUD_RECORD
(
	ID_ INT NOT NULL UNIQUE AUTO_INCREMENT COMMENT 'ID_',
	USER_ID INT COMMENT 'USER_ID',
	DEPT_ID INT COMMENT 'DEPT_ID',
	ZSTK_ID VARCHAR(50) COMMENT 'ZSTK_ID',
	PRIMARY KEY (ID_)
) COMMENT = '题目点赞记录';



