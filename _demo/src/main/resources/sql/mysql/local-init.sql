drop table view_xx_resource;

CREATE VIEW view_xx_resource AS
SELECT 0 as id, 'root' as name, -1 as parentId, '00001' as decode FROM dual
UNION
SELECT id, name, parentId, decode FROM xx_tbl;