truncate table um_group;

insert into um_group (ID, APPLICATIONID, NAME, DISABLED, GROUPTYPE, PARENTID, SEQNO, LEVELNO, DECODE, lockVersion)
values (-2, 'tss', '主用户组',     0, 1,  0, 1, 1, '00001', 0);

insert into um_group (ID, APPLICATIONID, NAME, DISABLED, GROUPTYPE, PARENTID, SEQNO, LEVELNO, DECODE, lockVersion)
values (-3, 'tss', '辅助用户组',   0, 2,  0, 2, 1, '00002', 0);

insert into um_group (ID, APPLICATIONID, NAME, DISABLED, GROUPTYPE, PARENTID, SEQNO, LEVELNO, DECODE, lockVersion)
values (-4, 'tss', '其他应用组',   0, 3,  0, 3, 1, '00003', 0);

insert into um_group (ID, APPLICATIONID, NAME, DISABLED, GROUPTYPE, PARENTID, SEQNO, LEVELNO, DECODE, lockVersion)
values (-7, 'tss', '自注册用户组', 0, 1, -2, 1, 2, '0000100001', 0);

insert into um_group (ID, APPLICATIONID, NAME, DISABLED, GROUPTYPE, PARENTID, SEQNO, LEVELNO, DECODE, lockVersion)
values (-8, 'tss', '已认证用户组', 0, 1, -7, 1, 3, '000010000100001', 0);

insert into um_group (ID, APPLICATIONID, NAME, DISABLED, GROUPTYPE, PARENTID, SEQNO, LEVELNO, DECODE, lockVersion)
values (-9, 'tss', '未认证用户组', 0, 1, -7, 2, 3, '000010000100002', 0);

commit;


truncate table UM_ROLE;

insert into UM_ROLE (ID, ISGROUP, PARENTID, NAME, SEQNO, DISABLED, LEVELNO, DECODE, lockVersion)
values (-6, 1, 0, '全部', 1, 0, 1, '00001', 0);

insert into UM_ROLE (ID, STARTDATE, ENDDATE, ISGROUP, PARENTID, NAME, SEQNO, DISABLED, LEVELNO, DECODE, lockVersion)
values (-1,     SYSDATE, SYSDATE + 365*50, 0, -6, '系统管理员', 1, 0, 2, '0000100001', 0);

insert into UM_ROLE (ID, STARTDATE, ENDDATE, ISGROUP, PARENTID, NAME, SEQNO, DISABLED, LEVELNO, DECODE, lockVersion)
values (-10000, SYSDATE, SYSDATE + 365*50, 0, -6, '匿名角色',   2, 0, 2, '0000100002', 0);

commit;

truncate table UM_USER;

--系统管理员ID=-1，初始化密码为123456
insert into UM_USER (ID, DISABLED, ACCOUNTUSEFULLIFE, APPLICATIONID, AUTHENTICATEMETHOD, LOGINNAME, PASSWORD, USERNAME, lockVersion)
values (-1, 0, SYSDATE + 365*50, 'tss', 'com.jinhe.tss.um.sso.UMSLocalUserPWDIdentifier', 'Admin', 'E5E0A2593A3AE4C038081D5F113CEC78', '系统管理员', 0);

--匿名用户ID=-10000
insert into UM_USER (ID, DISABLED, ACCOUNTUSEFULLIFE, APPLICATIONID, AUTHENTICATEMETHOD, LOGINNAME, PASSWORD, USERNAME, lockVersion)
values (-10000, 0, SYSDATE + 365*50, 'tss', null, 'ANONYMOUS', null, '匿名用户', 0);
commit;


truncate table um_groupUser;

-- 将系统管理员和匿名用户卦到主用户组下
insert into um_groupUser (ID, GROUPID, USERID, SEQNO, LEVELNO, DECODE) values (-1, -2, -1, 1, 2, '0000100001');
insert into um_groupUser (ID, GROUPID, USERID, SEQNO, LEVELNO, DECODE) values (-2, -2, -10000, 2, 2, '0000100002');
commit;

truncate table UM_ROLEUSER;

insert into UM_ROLEUSER (ID, ROLEID, USERID) values (0, -1, -1);
commit;

--密码策略默认规则
insert into UM_PASSWORDRULE(ID, NAME, LEASTLENGTH, CANEQ2LOGINNAME, LEASTSTRENGTH, LOWSTRENGTH, HIGHERSTRENGTH, IMPERMISSIBLE, ISDEFAULT)
values(0, '默认规则', 3, 0, 4, 8, 16, '123, abc', 1);
commit;
