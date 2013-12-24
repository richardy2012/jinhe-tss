-- 用户组资源视图
drop table view_group_resources cascade constraints;
CREATE VIEW view_group_resources AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM  um_group;

-- 角色资源视图
drop table view_role_resources cascade constraints;
CREATE VIEW view_role_resources AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM  um_role;
