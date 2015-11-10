Alter table `dm_access_log` add index `accessTime_IDX` (`accessTime`);
Alter table `dm_access_log` add index `userId_IDX` (`userId`);
Alter table `dm_access_log` ADD INDEX `methodCnName_IDX` (`methodCnName`)

CHECK TABLE dm_access_log;
REPAIR TABLE dm_access_log;
ANALYZE TABLE dm_access_log;

DELETE FROM dm_access_log  WHERE accessTime < DATE_SUB(CURDATE(), INTERVAL 7 DAY)
DELETE FROM dm_access_log  WHERE `methodCnName` IN ('SONumSendPM', 'SONumOrder', 'SONumSendPH', '看板（订单状态统计）');
SELECT COUNT(*) FROM dm_access_log t 

-- 清除日志
SELECT t.`methodCnName`, COUNT(*)
FROM dm_access_log t
WHERE `methodCnName` IN ('CompanyNameList', 'CenterNameList') OR `methodCnName` LIKE 'Get%'
GROUP BY t.`methodCnName`

DELETE FROM dm_access_log  
WHERE `methodCnName` IN ('CompanyNameList', 'CenterNameList') OR `methodCnName` LIKE 'Get%';

-- 设置标记，忽略记录访问日志
UPDATE  dm_report SET remark = CONCAT(IFNULL(remark, ''), ',ACLOG_IGNORE_REPORT')
WHERE `name` IN ('CompanyNameList', 'CenterNameList') OR `name` LIKE 'Get%';

SELECT remark FROM  dm_report 
WHERE `name` IN ('CompanyNameList', 'CenterNameList') OR `name` LIKE 'Get%';

delete FROM dm_access_log  WHERE accessTime < DATE_SUB(CURDATE(), INTERVAL 30 DAY);
delete FROM component_log  WHERE operatetime < DATE_SUB(CURDATE(), INTERVAL 30 DAY)

注：在mysql里执行delete、update语句，有时会提示：rror Code: 1175. You are using safe update mode
   可以先执行：SET SQL_SAFE_UPDATES = 0;