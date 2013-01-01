
-- 应用资源视图
drop table view_application_resources cascade constraints;
CREATE VIEW view_application_resources AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM um_application  WHERE (applicationType = -1 or applicationType = -2)
UNION
SELECT -5,  0, 1, '全部系统', 1, '00001'      FROM DUAL
UNION
SELECT -1, -5, 1, '平台系统', 2, '0000100001' FROM DUAL
UNION
SELECT -2, -5, 2, '其他系统', 2, '0000100002' FROM DUAL;

-- 主用户组资源视图
drop table view_MAINGROUP_resourcs cascade constraints;
CREATE VIEW view_MAINGROUP_resourcs AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM  um_group WHERE groupType = 1 ;

-- 辅助用户组资源视图
drop table view_ASSISTGROUP_resources cascade constraints;
CREATE VIEW view_ASSISTGROUP_resources AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM  um_group WHERE groupType = 2 ;

-- 其他应用用户组
drop table view_OtherGroup_resources cascade constraints;
CREATE VIEW view_OtherGroup_resources AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM  um_group WHERE groupType = 3 ;

-- 角色资源视图
drop table view_ROLE_resources cascade constraints;
CREATE VIEW view_ROLE_resources AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM  um_role;

