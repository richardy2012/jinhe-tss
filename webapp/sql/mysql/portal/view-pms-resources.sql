drop table view_Portal_resources;
CREATE VIEW view_Portal_resources AS
SELECT 0 as id, 'root' as name, -1 as parentId, 1 as seqNo, 1 AS levelNo, '00001' as decode FROM dual
UNION
SELECT id, name, parentId, seqNo, levelNo, decode FROM pms_portal_structure;

drop table view_menu_resources;
CREATE VIEW view_menu_resources AS
SELECT 0 as id, 'root' as name, -1 as parentId, 1 as seqNo, 1 as levelNo, '00001' as decode FROM dual
union
select id, name, parentid, seqno, levelno, decode from pms_menu;