1、新增record资源操作项

update um_operation set name = '录入数据' where id = 27;
 
INSERT INTO `um_operation`(`id`, `applicationId`, `dependId`, `dependParent`, `name`, `operationId`, `resourceTypeId`, `seqNo`)
VALUES (30, 'tss', 'opt1', null, '浏览数据', '4', 'D2', '4');

INSERT INTO `um_operation`(`id`, `applicationId`, `dependId`, `dependParent`, `name`, `operationId`, `resourceTypeId`, `seqNo`)
VALUES (31, 'tss', 'opt1', null, '维护数据', '5', 'D2', '5');
 
INSERT INTO `dm_permission_record` (`id`, `isGrant`, `isPass`, `operationId`, `permissionState`, `resourceId`, `roleId`, `resourceName`) VALUES('4','1','1','4','2','0','-1','root');
INSERT INTO `dm_permission_record` (`id`, `isGrant`, `isPass`, `operationId`, `permissionState`, `resourceId`, `roleId`, `resourceName`) VALUES('5','1','1','5','2','0','-1','root');
