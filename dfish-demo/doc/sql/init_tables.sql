-- 附件表
-- mysql
CREATE TABLE PUB_FILE_RECORD (
  FILE_ID char(32) NOT NULL COMMENT '附件编号',
  FILE_NAME varchar(600) COMMENT '附件名称',
  FILE_URL varchar(200) COMMENT '附件存储地址',
  FILE_EXTENSION varchar(64) COMMENT '附件扩展名',
  FILE_TYPE varchar(32) COMMENT '附件类型',
  FILE_SIZE decimal(18, 0) COMMENT '文件大小',
  FILE_CREATOR varchar(32) COMMENT '文件创建人',
  CREATE_TIME datetime(0) COMMENT '创建时间',
  UPDATE_TIME datetime(0) COMMENT '更新时间',
  FILE_LINK varchar(64) COMMENT '附件链接模块',
  FILE_KEY varchar(64) COMMENT '附件链接的关键字',
  FILE_STATUS char(1) COMMENT '附件状态#0:正常,1:已关联,9:已删除',
  FILE_DURATION decimal(8, 0) COMMENT '附件时长',
  FILE_SCHEME varchar(32) COMMENT '附件处理方案',
  PRIMARY KEY (FILE_ID),
  INDEX IDX_PUB_FILE_RECORD_01(FILE_LINK, FILE_KEY)
) COMMENT = '附件表';

-- Lob表
-- mysql
CREATE TABLE PUB_LOB  (
  LOB_ID char(32) NOT NULL,
  LOB_DATA longblob,
  LOB_CONTENT longtext,
  OPER_TIME datetime(0),
  ARCHIVE_FLAG char(1),
  ARCHIVE_TIME datetime(0),
  PRIMARY KEY (LOB_ID)
);