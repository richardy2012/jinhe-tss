drop table view_application_resources;
CREATE VIEW view_application_resources AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM um_application  WHERE (applicationType = -1 or applicationType = -2)
UNION
SELECT -5,  0, 1, '全部系统', 1, '00001'      FROM DUAL
UNION
SELECT -1, -5, 1, '平台系统', 2, '0000100001' FROM DUAL
UNION
SELECT -2, -5, 2, '其他系统', 2, '0000100002' FROM DUAL;

drop table view_MAINGROUP_resourcs;
CREATE VIEW view_MAINGROUP_resourcs AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM  um_group WHERE groupType = 1 ;

drop table view_ASSISTGROUP_resources;
CREATE VIEW view_ASSISTGROUP_resources AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM  um_group WHERE groupType = 2 ;

drop table view_OtherGroup_resources;
CREATE VIEW view_OtherGroup_resources AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM  um_group WHERE groupType = 3 ;

drop table view_ROLE_resources;
CREATE VIEW view_ROLE_resources AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM  um_role;

