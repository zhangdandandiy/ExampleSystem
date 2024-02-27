alter table  txal_doclink  ADD COLUMN transstate varchar(10) NULL COMMENT '转化状态'
;
update txal_doclink set transstate='none'
;