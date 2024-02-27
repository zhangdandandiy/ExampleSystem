alter table  txal_anli  ADD COLUMN sb_tech text NULL COMMENT '总结-技术'
;
alter table  txal_anli  ADD COLUMN sb_mng text NULL COMMENT '总结-管理'
;
alter table  txal_anli  ADD COLUMN bg_lc_a text NULL COMMENT '流程after'
;
alter table  txal_anli  ADD COLUMN bg_jc_b text NULL COMMENT '检测before'
;
alter table  txal_anli  ADD COLUMN bg_jc_a text NULL COMMENT '检测after';
alter table  txal_anli  ADD COLUMN bg_lc_b text NULL COMMENT '流程before';
alter table  txal_anli  ADD COLUMN bgh_ly_a text NULL COMMENT '布局优化after';
alter table  txal_anli  ADD COLUMN bgh_ly_b text NULL COMMENT '布局优化before'
;
alter table  txal_anli  ADD COLUMN bg_des_a text NULL COMMENT '设计after'
;
alter table  txal_anli  ADD COLUMN bg_des_b text NULL COMMENT '设计before';
alter table  txal_anli  ADD COLUMN bgh_ll_a text NULL COMMENT '良率after'
;
alter table  txal_anli  ADD COLUMN bgh_rl_b text NULL COMMENT '人力减少before';
alter table  txal_anli  ADD COLUMN bgh_ll_b text NULL COMMENT '良率before'
;
alter table  txal_anli  ADD COLUMN bgh_rl_a text NULL COMMENT '人力减少after'
;
alter table  txal_anli  ADD COLUMN bgh_ct_b text NULL COMMENT 'CT提升before'
;
alter table  txal_anli  ADD COLUMN bg_glfs_a text NULL COMMENT '管理方式after'
;
alter table  txal_anli  ADD COLUMN bg_glfs_b text NULL COMMENT '管理方式before'
;
alter table  txal_anli  ADD COLUMN bgh_ct_a text NULL COMMENT 'CT提升after'
;
