 
-- Create query index
create index idx_article_createTime on cms_article (createTime);
create index idx_article_status on cms_article (status);
create index idx_article_AUTHOR on cms_article (AUTHOR);
create index idx_article_ISSUEDATE on cms_article (ISSUEDATE);

create index idx_channel_name   on cms_channel (NAME);
create index idx_channel_DECODE on cms_channel (DECODE);
