-- 注意：该页面对应的前台目录为views/txal文件夹下


-- 如果你想更改到其他目录，请修改sql中component字段对应的值
create table txal_txjob (
                            id varchar(36) not null,
                            create_by varchar(50) comment '创建人',
                            create_time datetime comment '创建日期',
                            update_by varchar(50) comment '更新人',
                            update_time datetime comment '更新日期',
                            fname varchar(100) comment '三阶文件名称',
                            fcode varchar(30) comment '三阶文件编号',
                            fver varchar(10) comment '三阶级文件最新版本',
                            pname varchar(50) comment '归属程序名称',
                            orgs varchar(200) comment '作业办法关联部门',
                            primary key (id)
)
;
create table txal_txjobd (
                             id varchar(36) not null,
                             code varchar(20) comment '作业编号',
                             title varchar(300) comment '主题',
                             context longtext comment '作业内容',
                             ma_org_code varchar(200) comment '主导部门',
                             lk_org_code varchar(200) comment '关联部门',
                             fid varchar(36) comment '体系作业',
                             primary key (id)
)
;

update sys_permission set is_leaf=0
where id='1666279386080022529'
;
INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) 
VALUES ('2023070403047140580', '1666279386080022529', '作业维护', '/txal/txalTxjobList', 'txwj/txjob/TxalTxjobList', NULL, NULL, 1, NULL, '1', 1, 0, NULL, 1, 0, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2023-07-04 15:04:58', NULL, NULL, 0);

-- 权限控制sql
-- 新增
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2023070403047150581', '2023070403047140580', '添加体系作业', NULL, NULL, 0, NULL, NULL, 2, 'txal:txal_txjob:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-07-04 15:04:58', NULL, NULL, 0, 0, '1', 0);
-- 编辑
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2023070403047150582', '2023070403047140580', '编辑体系作业', NULL, NULL, 0, NULL, NULL, 2, 'txal:txal_txjob:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-07-04 15:04:58', NULL, NULL, 0, 0, '1', 0);
-- 删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2023070403047150583', '2023070403047140580', '删除体系作业', NULL, NULL, 0, NULL, NULL, 2, 'txal:txal_txjob:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-07-04 15:04:58', NULL, NULL, 0, 0, '1', 0);
-- 批量删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2023070403047150584', '2023070403047140580', '批量删除体系作业', NULL, NULL, 0, NULL, NULL, 2, 'txal:txal_txjob:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-07-04 15:04:58', NULL, NULL, 0, 0, '1', 0);
-- 导出excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2023070403047150585', '2023070403047140580', '导出excel_体系作业', NULL, NULL, 0, NULL, NULL, 2, 'txal:txal_txjob:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-07-04 15:04:58', NULL, NULL, 0, 0, '1', 0);
-- 导入excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('2023070403047150586', '2023070403047140580', '导入excel_体系作业', NULL, NULL, 0, NULL, NULL, 2, 'txal:txal_txjob:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-07-04 15:04:58', NULL, NULL, 0, 0, '1', 0);

INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external)
VALUES ('2023070903047140180', '1666279386080022529', '作业查询', '/txal/txalTxjobListSearch', 'txwj/txjobsearch/TxalTxjobList', NULL, NULL,1, NULL, '1', 2, 0, NULL, 1, 0, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2023-07-04 15:04:58', NULL, NULL, 0);