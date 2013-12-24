
drop table view_group_resources;
CREATE VIEW view_group_resources AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM  um_group ;

drop table  view_role_resources;
CREATE VIEW view_role_resources AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM  um_role;

