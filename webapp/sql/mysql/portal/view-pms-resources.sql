drop table view_portal_resources;
CREATE VIEW view_portal_resources AS
SELECT 0 as id, 'root' as name, -1 as parentId, 1 as seqNo, 1 AS levelNo, '00001' as decode FROM dual
UNION
SELECT id, name, parentId, seqNo, levelNo, decode FROM portal_structure;

drop table view_navigator_resources;
CREATE VIEW view_navigator_resources AS
SELECT 0 as id, 'root' as name, -1 as parentId, 1 as seqNo, 1 as levelNo, '00001' as decode FROM dual
union
select id, name, parentid, seqno, levelno, decode from portal_navigator;