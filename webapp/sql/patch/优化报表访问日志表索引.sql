Alter table `dm_access_log` add index `accessTime_IDX` (`accessTime`);
Alter table `dm_access_log` add index `userId_IDX` (`userId`);
Alter table `dm_access_log` ADD INDEX `methodCnName_IDX` (`methodCnName`)

CHECK TABLE dm_access_log;
REPAIR TABLE dm_access_log;
ANALYZE TABLE dm_access_log;

DELETE FROM dm_access_log  WHERE accessTime < DATE_SUB(CURDATE(), INTERVAL 7 DAY)
DELETE FROM dm_access_log  WHERE `methodCnName` IN ('SONumSendPM', 'SONumOrder', 'SONumSendPH', '看板（订单状态统计）');
SELECT COUNT(*) FROM dm_access_log t 