-- 站点栏目资源视图
drop table view_channel_resources cascade constraints;

CREATE VIEW view_channel_resources AS
	select -1 as id, 'root' as name, 0 as parentId, '00001' as decode from dual
	union
	select id, name, parentId, decode from cms_channel;
    

