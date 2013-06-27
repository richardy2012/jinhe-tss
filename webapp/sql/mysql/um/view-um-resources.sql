
drop table view_GROUP_resourcs cascade constraints;
CREATE VIEW view_GROUP_resourcs AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM  um_group ;

drop table view_ROLE_resources cascade constraints;
CREATE VIEW view_ROLE_resources AS
SELECT ID, parentId, seqNo, name, levelNo, decode FROM  um_role;

