drop table view_channel_resource;

CREATE VIEW view_channel_resource AS
select -1 as id, '全部' as name, 0 as parentId, '00001' as decode from dual
union
select id, name, parentId, decode from cms_channel;
    

