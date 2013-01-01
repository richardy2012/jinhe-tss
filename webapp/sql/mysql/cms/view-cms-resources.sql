drop table view_channel_resources;

CREATE VIEW view_channel_resources AS
select -1 as id, 'È«²¿' as name, 0 as parentId, 1 as seqNo,'00001' as decode, 1 as levelNo from dual
union
select id, name, parentId, seqNo, decode, levelNo from cms_channel;
    

