
create index idx_article_createTime on CMS_ARTICLE (createTime); 
create index idx_article_status on CMS_ARTICLE (status); 
create index idx_article_AUTHOR on CMS_ARTICLE (AUTHOR); 
create index idx_article_ISSUEDATE on CMS_ARTICLE (ISSUEDATE); 

create index idx_channel_name   on CMS_CHANNEL (NAME);
create index idx_channel_DECODE on CMS_CHANNEL (DECODE); 
