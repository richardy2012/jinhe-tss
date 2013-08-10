truncate table um_group;

insert into um_group (ID, NAME, DISABLED, GROUPTYPE, PARENTID, SEQNO, LEVELNO, DECODE, lockVersion)
values (-1, 'root',     0, 0,  0, 1, 1, '00001', 0);

insert into um_group (ID, NAME, DISABLED, GROUPTYPE, PARENTID, SEQNO, LEVELNO, DECODE, lockVersion)
values (-2, '主用户组',     0, 1,  -1, 1, 2, '0000100001', 0);

insert into um_group (ID, NAME, DISABLED, GROUPTYPE, PARENTID, SEQNO, LEVELNO, DECODE, lockVersion)
values (-3, '辅助用户组',   0, 2,  -1, 2, 2, '0000100002', 0);

insert into um_group (ID, NAME, DISABLED, GROUPTYPE, PARENTID, SEQNO, LEVELNO, DECODE, lockVersion)
values (-7, '自注册用户组', 0, 1, -2, 1, 3, '000010000100001', 0);

insert into um_group (ID, NAME, DISABLED, GROUPTYPE, PARENTID, SEQNO, LEVELNO, DECODE, lockVersion)
values (-8, '已认证用户组', 0, 1, -7, 1, 4, '00001000010000100001', 0);

insert into um_group (ID, NAME, DISABLED, GROUPTYPE, PARENTID, SEQNO, LEVELNO, DECODE, lockVersion)
values (-9, '未认证用户组', 0, 1, -7, 2, 4, '00001000010000100002', 0);

commit;


truncate table UM_ROLE;

insert into UM_ROLE (ID, ISGROUP, PARENTID, NAME, SEQNO, DISABLED, LEVELNO, DECODE, lockVersion)
values (-6, 1, 0, '全部', 1, 0, 1, '00001', 0);

insert into UM_ROLE (ID, STARTDATE, ENDDATE, ISGROUP, PARENTID, NAME, SEQNO, DISABLED, LEVELNO, DECODE, lockVersion)
values (-1,     sysdate(), str_to_date('01/01/2099', '%m/%d/%Y'), 0, -6, '系统管理员', 1, 0, 2, '0000100001', 0);

insert into UM_ROLE (ID, STARTDATE, ENDDATE, ISGROUP, PARENTID, NAME, SEQNO, DISABLED, LEVELNO, DECODE, lockVersion)
values (-10000, sysdate(), str_to_date('01/01/2099', '%m/%d/%Y'), 0, -6, '匿名角色',   2, 0, 2, '0000100002', 0);

commit;

truncate table UM_USER;

insert into UM_USER (ID, DISABLED, ACCOUNTUSEFULLIFE, AUTHENTICATEMETHOD, LOGINNAME, PASSWORD, USERNAME, lockVersion)
values (-1, 0, str_to_date('01/01/2099', '%m/%d/%Y'), 'com.jinhe.tss.um.sso.UMPasswordIdentifier', 'Admin', 'E5E0A2593A3AE4C038081D5F113CEC78', '系统管理员', 0);

insert into UM_USER (ID, DISABLED, ACCOUNTUSEFULLIFE, AUTHENTICATEMETHOD, LOGINNAME, PASSWORD, USERNAME, lockVersion)
values (-10000, 0, str_to_date('01/01/2099', '%m/%d/%Y'), null, 'ANONYMOUS', null, '匿名用户', 0);
commit;


truncate table um_groupUser;

insert into um_groupUser (ID, GROUPID, USERID) values (-1, -2, -1);
insert into um_groupUser (ID, GROUPID, USERID) values (-2, -2, -10000);
commit;

truncate table UM_ROLEUSER;

insert into UM_ROLEUSER (ID, ROLEID, USERID) values (0, -1, -1);
commit;
