-- MySQL dump 10.13  Distrib 5.5.32, for Win64 (x86)
--
-- Host: localhost    Database: tss
-- ------------------------------------------------------
-- Server version	5.5.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cms_article`
--

DROP TABLE IF EXISTS `cms_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `creatorId` bigint(20) DEFAULT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `lockVersion` int(11) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatorId` bigint(20) DEFAULT NULL,
  `updatorName` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `content` longtext NOT NULL,
  `hitCount` int(11) DEFAULT NULL,
  `isTop` int(11) DEFAULT NULL,
  `issueDate` datetime DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `overdueDate` datetime DEFAULT NULL,
  `pubUrl` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `subtitle` varchar(255) DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `channel_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK935C9C00147B967D` (`channel_id`),
  KEY `idx_article_createTime` (`createTime`),
  KEY `idx_article_status` (`status`),
  KEY `idx_article_AUTHOR` (`author`),
  KEY `idx_article_ISSUEDATE` (`issueDate`),
  CONSTRAINT `FK935C9C00147B967D` FOREIGN KEY (`channel_id`) REFERENCES `cms_channel` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_article`
--

LOCK TABLES `cms_article` WRITE;
/*!40000 ALTER TABLE `cms_article` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_attachment`
--

DROP TABLE IF EXISTS `cms_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `articleId` bigint(20) DEFAULT NULL,
  `fileExt` varchar(255) DEFAULT NULL,
  `fileName` varchar(255) NOT NULL,
  `localPath` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `seqNo` int(11) DEFAULT NULL,
  `type` int(11) NOT NULL,
  `uploadDate` datetime DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_attachment`
--

LOCK TABLES `cms_attachment` WRITE;
/*!40000 ALTER TABLE `cms_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_channel`
--

DROP TABLE IF EXISTS `cms_channel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_channel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `creatorId` bigint(20) DEFAULT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `lockVersion` int(11) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatorId` bigint(20) DEFAULT NULL,
  `updatorName` varchar(255) DEFAULT NULL,
  `decode` varchar(255) DEFAULT NULL,
  `disabled` int(11) DEFAULT NULL,
  `docPath` varchar(255) DEFAULT NULL,
  `imagePath` varchar(255) DEFAULT NULL,
  `levelNo` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `overdueDate` varchar(255) DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `seqNo` int(11) NOT NULL,
  `site_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `parentId` (`parentId`,`name`),
  KEY `FKEB0F4C0D4BCDDB9` (`site_id`),
  KEY `idx_channel_name` (`name`),
  KEY `idx_channel_DECODE` (`decode`),
  CONSTRAINT `FKEB0F4C0D4BCDDB9` FOREIGN KEY (`site_id`) REFERENCES `cms_channel` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_channel`
--

LOCK TABLES `cms_channel` WRITE;
/*!40000 ALTER TABLE `cms_channel` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_channel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_permission_channel`
--

DROP TABLE IF EXISTS `cms_permission_channel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_permission_channel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `isGrant` int(11) DEFAULT NULL,
  `isPass` int(11) DEFAULT NULL,
  `operationId` varchar(255) NOT NULL,
  `permissionState` int(11) DEFAULT NULL,
  `resourceId` bigint(20) NOT NULL,
  `resourceName` varchar(255) DEFAULT NULL,
  `roleId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_permission_channel`
--

LOCK TABLES `cms_permission_channel` WRITE;
/*!40000 ALTER TABLE `cms_permission_channel` DISABLE KEYS */;
INSERT INTO `cms_permission_channel` VALUES (1,1,1,'1',2,-1,'全部',-1),(2,1,1,'2',2,-1,'全部',-1),(3,1,1,'3',2,-1,'全部',-1),(4,1,1,'4',2,-1,'全部',-1),(5,1,1,'5',2,-1,'全部',-1),(6,1,1,'6',2,-1,'全部',-1),(7,1,1,'7',2,-1,'全部',-1),(8,1,1,'8',2,-1,'全部',-1),(9,1,1,'9',2,-1,'全部',-1);
/*!40000 ALTER TABLE `cms_permission_channel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `component_log`
--

DROP TABLE IF EXISTS `component_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `component_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` longtext,
  `methodExcuteTime` int(11) DEFAULT NULL,
  `operateTable` varchar(255) NOT NULL,
  `operateTime` datetime DEFAULT NULL,
  `operationCode` varchar(255) DEFAULT NULL,
  `operatorBrowser` varchar(255) DEFAULT NULL,
  `operatorIP` varchar(255) DEFAULT NULL,
  `operatorId` bigint(20) DEFAULT NULL,
  `operatorName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `component_log`
--

LOCK TABLES `component_log` WRITE;
/*!40000 ALTER TABLE `component_log` DISABLE KEYS */;
INSERT INTO `component_log` VALUES (1,'<string>新建/修改了 (id:1, name:布局器组) 节点</string>',11,'门户组件','2015-01-05 18:23:17','saveComponent',NULL,NULL,-1,'Admin'),(2,'<string>新建/修改了 (id:2, name:默认布局器) 节点</string>',3,'门户组件','2015-01-05 18:23:17','saveComponent',NULL,NULL,-1,'Admin'),(3,'<string>新增/修改了系统参数：com.jinhe.tss.framework.component.param.Param@6fad2ccb[id=8,code=db.connection.url,name=db.connection.url,value=jdbc:mysql://localhost:3306/tss,text=&lt;null&gt;,modality=0,type=1,description=&lt;null&gt;,parentId=2,seqNo=6,decode=0000100006,levelNo=2,disabled=0,hidden=0,creatorId=-1,createTime=2015-01-05 18:23:17.0,creatorName=Admin,updatorId=&lt;null&gt;,updateTime=&lt;null&gt;,updatorName=&lt;null&gt;,lockVersion=0]</string>',4,'系统参数','2015-01-05 18:23:17','saveParam',NULL,NULL,-1,'Admin'),(4,'<string>新建/修改了 (id:3, name:修饰器组) 节点</string>',4,'门户组件','2015-01-05 18:23:17','saveComponent',NULL,NULL,-1,'Admin'),(5,'<string>新增/修改了系统参数：com.jinhe.tss.framework.component.param.Param@2c703723[id=9,code=session.cyclelife,name=session.cyclelife,value=1800,text=&lt;null&gt;,modality=0,type=1,description=&lt;null&gt;,parentId=2,seqNo=7,decode=0000100007,levelNo=2,disabled=0,hidden=0,creatorId=-1,createTime=2015-01-05 18:23:17.0,creatorName=Admin,updatorId=&lt;null&gt;,updateTime=&lt;null&gt;,updatorName=&lt;null&gt;,lockVersion=0]</string>',3,'系统参数','2015-01-05 18:23:17','saveParam',NULL,NULL,-1,'Admin');
/*!40000 ALTER TABLE `component_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `component_param`
--

DROP TABLE IF EXISTS `component_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `component_param` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `creatorId` bigint(20) DEFAULT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `lockVersion` int(11) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatorId` bigint(20) DEFAULT NULL,
  `updatorName` varchar(255) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `decode` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `disabled` int(11) DEFAULT NULL,
  `hidden` int(11) DEFAULT NULL,
  `levelNo` int(11) DEFAULT NULL,
  `modality` int(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `seqNo` int(11) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `value` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `component_param`
--

LOCK TABLES `component_param` WRITE;
/*!40000 ALTER TABLE `component_param` DISABLE KEYS */;
INSERT INTO `component_param` VALUES (1,NULL,NULL,NULL,0,NULL,NULL,NULL,'0','00000',NULL,NULL,1,NULL,NULL,'0',NULL,NULL,'0',NULL,'0'),(2,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,NULL,'00001',NULL,0,0,1,NULL,'系统参数',0,1,NULL,0,NULL),(3,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'log_flush_max_size','0000100001',NULL,0,0,2,0,'log_flush_max_size',2,1,NULL,1,'12'),(4,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'upload_path','0000100002',NULL,0,0,2,0,'upload_path',2,2,NULL,1,'D:/temp/upload'),(5,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'class.name.LoginCostomizer','0000100003',NULL,0,0,2,0,'class.name.LoginCostomizer',2,3,NULL,1,'com.jinhe.tss.um.sso.FetchPermissionAfterLogin'),(6,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'bean.onlineManager','0000100004',NULL,0,0,2,0,'bean.onlineManager',2,4,NULL,1,'DBOnlineUserManagerService'),(7,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'application.code','0000100005',NULL,0,0,2,0,'application.code',2,5,NULL,1,'TSS'),(8,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'db.connection.url','0000100006',NULL,0,0,2,0,'db.connection.url',2,6,NULL,1,'jdbc:mysql://localhost:3306/tss'),(9,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'session.cyclelife','0000100007',NULL,0,0,2,0,'session.cyclelife',2,7,NULL,1,'1800'),(10,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'class.name.IdentityGetter','0000100008',NULL,0,0,2,0,'class.name.IdentityGetter',2,8,NULL,1,'com.jinhe.tss.um.sso.UMIdentityGetter'),(11,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'db.connection.password','0000100009',NULL,0,0,2,0,'db.connection.password',2,9,NULL,1,'800best@com'),(12,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'db.connection.driver_class','0000100010',NULL,0,0,2,0,'db.connection.driver_class',2,10,NULL,1,'com.mysql.jdbc.Driver'),(13,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'environment','0000100011',NULL,0,0,2,0,'environment',2,11,NULL,1,'dev'),(14,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'last.package.time','0000100012',NULL,0,0,2,0,'last.package.time',2,12,NULL,1,'yyyy-MM-dd hh:mi:ss'),(15,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'db.connection.username','0000100013',NULL,0,0,2,0,'db.connection.username',2,13,NULL,1,'root'),(16,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'useFreemarker','0000100014',NULL,0,0,2,0,'useFreemarker',2,14,NULL,1,'true'),(17,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'job.enable','0000100015',NULL,0,0,2,0,'job.enable',2,15,NULL,1,'true'),(18,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,NULL,'00002',NULL,0,0,1,NULL,'应用服务配置',0,2,NULL,0,NULL),(19,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'TSS','0000200001',NULL,0,0,2,0,'TSS',18,1,NULL,1,'<server code=\"TSS\" framework=\"tss\" name=\"TSS\" sessionIdName=\"JSESSIONID\" baseURL=\"http://localhost:8088/tss\"/>'),(20,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'DMS','0000200002',NULL,0,0,2,0,'DMS',18,2,NULL,1,'<server code=\"DMS\" framework=\"tss\" name=\"DMS\" sessionIdName=\"JSESSIONID\" baseURL=\"http://localhost:8088/dms\"/>');
/*!40000 ALTER TABLE `component_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `online_user`
--

DROP TABLE IF EXISTS `online_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `online_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appCode` varchar(255) DEFAULT NULL,
  `sessionId` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  `clientIp` varchar(255) DEFAULT NULL,
  `loginTime` datetime DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `online_user`
--

LOCK TABLES `online_user` WRITE;
/*!40000 ALTER TABLE `online_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `online_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portal_component`
--

DROP TABLE IF EXISTS `portal_component`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portal_component` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `creatorId` bigint(20) DEFAULT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `lockVersion` int(11) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatorId` bigint(20) DEFAULT NULL,
  `updatorName` varchar(255) DEFAULT NULL,
  `decode` varchar(255) DEFAULT NULL,
  `definition` longtext,
  `description` longtext,
  `disabled` int(11) DEFAULT NULL,
  `isDefault` int(11) DEFAULT NULL,
  `isGroup` bit(1) NOT NULL,
  `levelNo` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `parentId` bigint(20) NOT NULL,
  `portNumber` int(11) NOT NULL,
  `seqNo` int(11) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `parentId` (`parentId`,`type`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portal_component`
--

LOCK TABLES `portal_component` WRITE;
/*!40000 ALTER TABLE `portal_component` DISABLE KEYS */;
INSERT INTO `portal_component` VALUES (1,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'00001',NULL,NULL,0,0,'',1,'布局器组',0,0,1,1,NULL),(2,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'0000100001','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<layout>\n	<property>\n		<name>默认布局器</name>\n		<portNumber>-1</portNumber>\n	</property>\n	<html>\n		<![CDATA[\n			<table id=\"${id}\">\n				<tr>\n					<td>${port0}</td>\n				</tr>\n			</table>\n		]]>\n	</html>\n</layout>',NULL,0,1,'\0',2,'默认布局器',1,-1,1,1,NULL),(3,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'00002',NULL,NULL,0,0,'',1,'修饰器组',0,0,2,2,NULL),(4,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'0000200001','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<decorator>\n	<property>\n		<name>默认修饰器</name>\n	</property>\n	<script><![CDATA[]]></script>\n	<style><![CDATA[]]></style>\n	<html>\n		<![CDATA[<table id=${id} class=\"decorator\">\n		  <tbody>\n			<tr>\n			  <td>\n				${content}\n			  </td>\n			</tr>\n		  </tbody>\n		</table>]]>\n	</html>\n	<events/>\n	<parameters/>\n</decorator>',NULL,0,1,'\0',2,'默认修饰器',3,0,1,2,NULL),(5,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'00003',NULL,NULL,0,0,'',1,'portlet组',0,0,3,3,NULL);
/*!40000 ALTER TABLE `portal_component` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portal_flowrate`
--

DROP TABLE IF EXISTS `portal_flowrate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portal_flowrate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) DEFAULT NULL,
  `pageId` bigint(20) DEFAULT NULL,
  `visitTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portal_flowrate`
--

LOCK TABLES `portal_flowrate` WRITE;
/*!40000 ALTER TABLE `portal_flowrate` DISABLE KEYS */;
/*!40000 ALTER TABLE `portal_flowrate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portal_navigator`
--

DROP TABLE IF EXISTS `portal_navigator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portal_navigator` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `creatorId` bigint(20) DEFAULT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `lockVersion` int(11) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatorId` bigint(20) DEFAULT NULL,
  `updatorName` varchar(255) DEFAULT NULL,
  `decode` varchar(255) DEFAULT NULL,
  `description` longtext,
  `disabled` int(11) DEFAULT NULL,
  `levelNo` int(11) DEFAULT NULL,
  `methodName` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `params` varchar(255) DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `portalId` bigint(20) DEFAULT NULL,
  `seqNo` int(11) NOT NULL,
  `target` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `toContent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `parentId` (`parentId`,`name`),
  KEY `FKDD021644D7745F02` (`content_id`),
  KEY `FKDD021644E31CCFBD` (`toContent_id`),
  CONSTRAINT `FKDD021644D7745F02` FOREIGN KEY (`content_id`) REFERENCES `portal_structure` (`id`),
  CONSTRAINT `FKDD021644E31CCFBD` FOREIGN KEY (`toContent_id`) REFERENCES `portal_structure` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portal_navigator`
--

LOCK TABLES `portal_navigator` WRITE;
/*!40000 ALTER TABLE `portal_navigator` DISABLE KEYS */;
INSERT INTO `portal_navigator` VALUES (1,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'0000100001',NULL,0,2,NULL,'应用菜单组',NULL,0,NULL,1,NULL,1,NULL,NULL,NULL);
/*!40000 ALTER TABLE `portal_navigator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portal_permission_navigator`
--

DROP TABLE IF EXISTS `portal_permission_navigator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portal_permission_navigator` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `isGrant` int(11) DEFAULT NULL,
  `isPass` int(11) DEFAULT NULL,
  `operationId` varchar(255) NOT NULL,
  `permissionState` int(11) DEFAULT NULL,
  `resourceId` bigint(20) NOT NULL,
  `resourceName` varchar(255) DEFAULT NULL,
  `roleId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portal_permission_navigator`
--

LOCK TABLES `portal_permission_navigator` WRITE;
/*!40000 ALTER TABLE `portal_permission_navigator` DISABLE KEYS */;
INSERT INTO `portal_permission_navigator` VALUES (1,1,1,'1',2,0,'root',-1),(2,1,1,'2',2,0,'root',-1),(3,1,1,'1',2,1,'应用菜单组',-1),(4,1,1,'2',2,1,'应用菜单组',-1);
/*!40000 ALTER TABLE `portal_permission_navigator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portal_permission_portal`
--

DROP TABLE IF EXISTS `portal_permission_portal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portal_permission_portal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `isGrant` int(11) DEFAULT NULL,
  `isPass` int(11) DEFAULT NULL,
  `operationId` varchar(255) NOT NULL,
  `permissionState` int(11) DEFAULT NULL,
  `resourceId` bigint(20) NOT NULL,
  `resourceName` varchar(255) DEFAULT NULL,
  `roleId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portal_permission_portal`
--

LOCK TABLES `portal_permission_portal` WRITE;
/*!40000 ALTER TABLE `portal_permission_portal` DISABLE KEYS */;
INSERT INTO `portal_permission_portal` VALUES (1,1,1,'1',2,0,'root',-1),(2,1,1,'2',2,0,'root',-1),(3,1,1,'3',2,0,'root',-1),(4,1,1,'4',2,0,'root',-1),(5,1,1,'5',2,0,'root',-1),(6,1,1,'6',2,0,'root',-1),(7,1,1,'7',2,0,'root',-1);
/*!40000 ALTER TABLE `portal_permission_portal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portal_release_config`
--

DROP TABLE IF EXISTS `portal_release_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portal_release_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `visitUrl` varchar(255) NOT NULL,
  `page_id` bigint(20) DEFAULT NULL,
  `portal_id` bigint(20) DEFAULT NULL,
  `theme_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FKCF1E12AD78D9CB08` (`theme_id`),
  KEY `FKCF1E12ADA44A22F` (`portal_id`),
  KEY `FKCF1E12AD97CA712C` (`page_id`),
  CONSTRAINT `FKCF1E12AD78D9CB08` FOREIGN KEY (`theme_id`) REFERENCES `portal_theme` (`id`),
  CONSTRAINT `FKCF1E12AD97CA712C` FOREIGN KEY (`page_id`) REFERENCES `portal_structure` (`id`),
  CONSTRAINT `FKCF1E12ADA44A22F` FOREIGN KEY (`portal_id`) REFERENCES `portal_structure` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portal_release_config`
--

LOCK TABLES `portal_release_config` WRITE;
/*!40000 ALTER TABLE `portal_release_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `portal_release_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portal_structure`
--

DROP TABLE IF EXISTS `portal_structure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portal_structure` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `creatorId` bigint(20) DEFAULT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `lockVersion` int(11) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatorId` bigint(20) DEFAULT NULL,
  `updatorName` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `decode` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `disabled` int(11) DEFAULT NULL,
  `levelNo` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `parameters` varchar(255) DEFAULT NULL,
  `parentId` bigint(20) NOT NULL,
  `portalId` bigint(20) DEFAULT NULL,
  `seqNo` int(11) NOT NULL,
  `supplement` longtext,
  `type` int(11) NOT NULL,
  `currentTheme_id` bigint(20) DEFAULT NULL,
  `definer_id` bigint(20) DEFAULT NULL,
  `theme_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `parentId` (`parentId`,`name`),
  KEY `FKB8C019602EF1582E` (`definer_id`),
  KEY `FKB8C0196078D9CB08` (`theme_id`),
  KEY `FKB8C01960FB50FF81` (`currentTheme_id`),
  CONSTRAINT `FKB8C019602EF1582E` FOREIGN KEY (`definer_id`) REFERENCES `portal_component` (`id`),
  CONSTRAINT `FKB8C0196078D9CB08` FOREIGN KEY (`theme_id`) REFERENCES `portal_theme` (`id`),
  CONSTRAINT `FKB8C01960FB50FF81` FOREIGN KEY (`currentTheme_id`) REFERENCES `portal_theme` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portal_structure`
--

LOCK TABLES `portal_structure` WRITE;
/*!40000 ALTER TABLE `portal_structure` DISABLE KEYS */;
/*!40000 ALTER TABLE `portal_structure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portal_theme`
--

DROP TABLE IF EXISTS `portal_theme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portal_theme` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `creatorId` bigint(20) DEFAULT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `lockVersion` int(11) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatorId` bigint(20) DEFAULT NULL,
  `updatorName` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `portalId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `portalId` (`portalId`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portal_theme`
--

LOCK TABLES `portal_theme` WRITE;
/*!40000 ALTER TABLE `portal_theme` DISABLE KEYS */;
/*!40000 ALTER TABLE `portal_theme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portal_theme_info`
--

DROP TABLE IF EXISTS `portal_theme_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portal_theme_info` (
  `structureId` bigint(20) NOT NULL,
  `themeId` bigint(20) NOT NULL,
  `parameters` varchar(255) DEFAULT NULL,
  `decorator_id` bigint(20) DEFAULT NULL,
  `layout_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`structureId`,`themeId`),
  KEY `FK74F26F1719B53E3B` (`layout_id`),
  KEY `FK74F26F177199B8A` (`decorator_id`),
  CONSTRAINT `FK74F26F1719B53E3B` FOREIGN KEY (`layout_id`) REFERENCES `portal_component` (`id`),
  CONSTRAINT `FK74F26F177199B8A` FOREIGN KEY (`decorator_id`) REFERENCES `portal_component` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portal_theme_info`
--

LOCK TABLES `portal_theme_info` WRITE;
/*!40000 ALTER TABLE `portal_theme_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `portal_theme_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portal_theme_personal`
--

DROP TABLE IF EXISTS `portal_theme_personal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portal_theme_personal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `portalId` bigint(20) DEFAULT NULL,
  `themeId` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portal_theme_personal`
--

LOCK TABLES `portal_theme_personal` WRITE;
/*!40000 ALTER TABLE `portal_theme_personal` DISABLE KEYS */;
/*!40000 ALTER TABLE `portal_theme_personal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `um_application`
--

DROP TABLE IF EXISTS `um_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `um_application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `creatorId` bigint(20) DEFAULT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `lockVersion` int(11) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatorId` bigint(20) DEFAULT NULL,
  `updatorName` varchar(255) DEFAULT NULL,
  `applicationId` varchar(50) NOT NULL,
  `applicationType` varchar(10) DEFAULT NULL,
  `dataSourceType` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `paramDesc` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `applicationId` (`applicationId`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `um_application`
--

LOCK TABLES `um_application` WRITE;
/*!40000 ALTER TABLE `um_application` DISABLE KEYS */;
INSERT INTO `um_application` VALUES (1,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'tss','-1',NULL,NULL,'TSS',NULL);
/*!40000 ALTER TABLE `um_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `um_group`
--

DROP TABLE IF EXISTS `um_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `um_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `creatorId` bigint(20) DEFAULT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `lockVersion` int(11) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatorId` bigint(20) DEFAULT NULL,
  `updatorName` varchar(255) DEFAULT NULL,
  `decode` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `disabled` int(11) DEFAULT NULL,
  `fromApp` varchar(255) DEFAULT NULL,
  `fromGroupId` varchar(255) DEFAULT NULL,
  `groupType` int(11) NOT NULL,
  `levelNo` int(11) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `seqNo` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `parentId` (`parentId`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `um_group`
--

LOCK TABLES `um_group` WRITE;
/*!40000 ALTER TABLE `um_group` DISABLE KEYS */;
INSERT INTO `um_group` VALUES (-7,NULL,NULL,NULL,0,NULL,NULL,NULL,'000010000100001',NULL,0,NULL,NULL,1,3,'自注册用户组',-2,1),(-3,NULL,NULL,NULL,0,NULL,NULL,NULL,'0000100002',NULL,0,NULL,NULL,2,2,'辅助用户组',-1,2),(-2,NULL,NULL,NULL,0,NULL,NULL,NULL,'0000100001',NULL,0,NULL,NULL,1,2,'主用户组',-1,1),(-1,NULL,NULL,NULL,0,NULL,NULL,NULL,'00001',NULL,0,NULL,NULL,0,1,'root',0,1);
/*!40000 ALTER TABLE `um_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `um_groupuser`
--

DROP TABLE IF EXISTS `um_groupuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `um_groupuser` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `groupId` bigint(20) NOT NULL,
  `userId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `groupId` (`groupId`,`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `um_groupuser`
--

LOCK TABLES `um_groupuser` WRITE;
/*!40000 ALTER TABLE `um_groupuser` DISABLE KEYS */;
INSERT INTO `um_groupuser` VALUES (-2,-2,-10000),(-1,-2,-1);
/*!40000 ALTER TABLE `um_groupuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `um_operation`
--

DROP TABLE IF EXISTS `um_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `um_operation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `applicationId` varchar(50) NOT NULL,
  `dependId` varchar(255) DEFAULT NULL,
  `dependParent` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `operationId` varchar(50) NOT NULL,
  `resourceTypeId` varchar(50) NOT NULL,
  `seqNo` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `applicationId` (`applicationId`,`resourceTypeId`,`operationId`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `um_operation`
--

LOCK TABLES `um_operation` WRITE;
/*!40000 ALTER TABLE `um_operation` DISABLE KEYS */;
INSERT INTO `um_operation` VALUES (1,'tss',NULL,'2',NULL,'查看','1','1',1),(2,'tss','opt1',NULL,NULL,'管理','2','1',2),(3,'tss',NULL,'2',NULL,'查看','1','2',1),(4,'tss','opt1',NULL,NULL,'管理','2','2',2),(5,'tss',NULL,'2',NULL,'查看浏览','1','3',1),(6,'tss','opt1',NULL,NULL,'新建栏目','2','3',2),(7,'tss','opt1',NULL,NULL,'新建文章','3','3',3),(8,'tss','opt1','3',NULL,'发布文章','4','3',4),(9,'tss','opt1',NULL,NULL,'编辑权限','5','3',5),(10,'tss','opt1','3',NULL,'删除权限','6','3',6),(11,'tss','opt1','2,3',NULL,'停用启用','7','3',7),(12,'tss','opt1',NULL,NULL,'排序权限','8','3',8),(13,'tss','opt1',NULL,NULL,'移动权限','9','3',9),(14,'tss',NULL,'2','查看门户结构','查看','1','4',1),(15,'tss','opt1',NULL,'编辑门户结构','编辑','2','4',2),(16,'tss','opt1','3','删除门户结构','删除','3','4',3),(17,'tss','opt1',NULL,'增加门户结构','增加','4','4',4),(18,'tss','opt1',NULL,'排序门户结构','排序','5','4',5),(19,'tss','opt1','3','停用门户结构','停用','6','4',6),(20,'tss','opt1','2','启用门户结构','启用','7','4',7),(21,'tss',NULL,'2','浏览','浏览','1','5',1),(22,'tss',NULL,NULL,'维护','维护','2','5',2);
/*!40000 ALTER TABLE `um_operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `um_permission_group`
--

DROP TABLE IF EXISTS `um_permission_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `um_permission_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `isGrant` int(11) DEFAULT NULL,
  `isPass` int(11) DEFAULT NULL,
  `operationId` varchar(255) NOT NULL,
  `permissionState` int(11) DEFAULT NULL,
  `resourceId` bigint(20) NOT NULL,
  `resourceName` varchar(255) DEFAULT NULL,
  `roleId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `um_permission_group`
--

LOCK TABLES `um_permission_group` WRITE;
/*!40000 ALTER TABLE `um_permission_group` DISABLE KEYS */;
INSERT INTO `um_permission_group` VALUES (1,1,1,'1',2,-1,'root',-1),(2,1,1,'1',2,-2,'主用户组',-1),(3,1,1,'1',2,-7,'自注册用户组',-1),(4,1,1,'1',2,-3,'辅助用户组',-1),(5,1,1,'2',2,-1,'root',-1),(6,1,1,'2',2,-2,'主用户组',-1),(7,1,1,'2',2,-7,'自注册用户组',-1),(8,1,1,'2',2,-3,'辅助用户组',-1);
/*!40000 ALTER TABLE `um_permission_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `um_permission_role`
--

DROP TABLE IF EXISTS `um_permission_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `um_permission_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `isGrant` int(11) DEFAULT NULL,
  `isPass` int(11) DEFAULT NULL,
  `operationId` varchar(255) NOT NULL,
  `permissionState` int(11) DEFAULT NULL,
  `resourceId` bigint(20) NOT NULL,
  `resourceName` varchar(255) DEFAULT NULL,
  `roleId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `um_permission_role`
--

LOCK TABLES `um_permission_role` WRITE;
/*!40000 ALTER TABLE `um_permission_role` DISABLE KEYS */;
INSERT INTO `um_permission_role` VALUES (1,1,1,'1',2,-6,'全部',-1),(2,1,1,'1',2,-1,'系统管理员',-1),(3,1,1,'1',2,-10000,'匿名角色',-1),(4,1,1,'2',2,-6,'全部',-1),(5,1,1,'2',2,-1,'系统管理员',-1),(6,1,1,'2',2,-10000,'匿名角色',-1);
/*!40000 ALTER TABLE `um_permission_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `um_resourcetype`
--

DROP TABLE IF EXISTS `um_resourcetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `um_resourcetype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `creatorId` bigint(20) DEFAULT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `lockVersion` int(11) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatorId` bigint(20) DEFAULT NULL,
  `updatorName` varchar(255) DEFAULT NULL,
  `applicationId` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `permissionTable` varchar(255) NOT NULL,
  `resourceTable` varchar(255) NOT NULL,
  `resourceTypeId` varchar(50) NOT NULL,
  `rootId` bigint(20) DEFAULT NULL,
  `seqNo` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `applicationId` (`applicationId`,`resourceTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `um_resourcetype`
--

LOCK TABLES `um_resourcetype` WRITE;
/*!40000 ALTER TABLE `um_resourcetype` DISABLE KEYS */;
INSERT INTO `um_resourcetype` VALUES (1,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'tss','用户组织资源类型','用户组','com.jinhe.tss.um.entity.permission.GroupPermission','com.jinhe.tss.um.entity.permission.GroupResource','1',-1,1),(2,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'tss','角色资源类型','角色','com.jinhe.tss.um.entity.permission.RolePermission','com.jinhe.tss.um.entity.permission.RoleResource','2',-6,2),(3,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'tss','内容栏目资源类型','内容栏目','com.jinhe.tss.cms.entity.permission.ChannelPermission','com.jinhe.tss.cms.entity.permission.ChannelResource','3',-1,3),(4,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'tss','门户结构资源类型','门户结构','com.jinhe.tss.portal.entity.permission.PortalPermission','com.jinhe.tss.portal.entity.permission.PortalResource','4',0,4),(5,'2015-01-05 18:23:17',-1,'Admin',0,NULL,NULL,NULL,'tss','菜单导航资源类型','菜单导航','com.jinhe.tss.portal.entity.permission.NavigatorPermission','com.jinhe.tss.portal.entity.permission.NavigatorResource','5',0,5);
/*!40000 ALTER TABLE `um_resourcetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `um_resourcetype_root`
--

DROP TABLE IF EXISTS `um_resourcetype_root`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `um_resourcetype_root` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `applicationId` varchar(255) DEFAULT NULL,
  `resourceTypeId` varchar(255) DEFAULT NULL,
  `rootId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `um_resourcetype_root`
--

LOCK TABLES `um_resourcetype_root` WRITE;
/*!40000 ALTER TABLE `um_resourcetype_root` DISABLE KEYS */;
INSERT INTO `um_resourcetype_root` VALUES (1,'tss','1',-1),(2,'tss','2',-6),(3,'tss','3',-1),(4,'tss','4',0),(5,'tss','5',0);
/*!40000 ALTER TABLE `um_resourcetype_root` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `um_role`
--

DROP TABLE IF EXISTS `um_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `um_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `creatorId` bigint(20) DEFAULT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `lockVersion` int(11) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatorId` bigint(20) DEFAULT NULL,
  `updatorName` varchar(255) DEFAULT NULL,
  `decode` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `disabled` int(11) DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `isGroup` int(11) DEFAULT NULL,
  `levelNo` int(11) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `seqNo` int(11) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `parentId` (`parentId`,`name`,`isGroup`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `um_role`
--

LOCK TABLES `um_role` WRITE;
/*!40000 ALTER TABLE `um_role` DISABLE KEYS */;
INSERT INTO `um_role` VALUES (-10000,NULL,NULL,NULL,0,NULL,NULL,NULL,'0000100002',NULL,0,'2099-01-01 00:00:00',0,2,'匿名角色',-6,2,'2015-01-05 18:23:15'),(-6,NULL,NULL,NULL,0,NULL,NULL,NULL,'00001',NULL,0,NULL,1,1,'全部',0,1,NULL),(-1,NULL,NULL,NULL,0,NULL,NULL,NULL,'0000100001',NULL,0,'2099-01-01 00:00:00',0,2,'系统管理员',-6,1,'2015-01-05 18:23:15');
/*!40000 ALTER TABLE `um_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `um_rolegroup`
--

DROP TABLE IF EXISTS `um_rolegroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `um_rolegroup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `groupId` bigint(20) DEFAULT NULL,
  `roleId` bigint(20) DEFAULT NULL,
  `strategyId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `roleId` (`roleId`,`groupId`,`strategyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `um_rolegroup`
--

LOCK TABLES `um_rolegroup` WRITE;
/*!40000 ALTER TABLE `um_rolegroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `um_rolegroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `um_roleuser`
--

DROP TABLE IF EXISTS `um_roleuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `um_roleuser` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `roleId` bigint(20) DEFAULT NULL,
  `strategyId` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `roleId` (`roleId`,`userId`,`strategyId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `um_roleuser`
--

LOCK TABLES `um_roleuser` WRITE;
/*!40000 ALTER TABLE `um_roleuser` DISABLE KEYS */;
INSERT INTO `um_roleuser` VALUES (1,-1,NULL,-1);
/*!40000 ALTER TABLE `um_roleuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `um_roleusermapping`
--

DROP TABLE IF EXISTS `um_roleusermapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `um_roleusermapping` (
  `roleId` bigint(20) NOT NULL,
  `userId` bigint(20) NOT NULL,
  PRIMARY KEY (`roleId`,`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `um_roleusermapping`
--

LOCK TABLES `um_roleusermapping` WRITE;
/*!40000 ALTER TABLE `um_roleusermapping` DISABLE KEYS */;
INSERT INTO `um_roleusermapping` VALUES (-10000,-10000),(-10000,-1),(-1,-1);
/*!40000 ALTER TABLE `um_roleusermapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `um_sub_authorize`
--

DROP TABLE IF EXISTS `um_sub_authorize`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `um_sub_authorize` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `creatorId` bigint(20) DEFAULT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `lockVersion` int(11) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatorId` bigint(20) DEFAULT NULL,
  `updatorName` varchar(255) DEFAULT NULL,
  `description` longtext,
  `disabled` int(11) DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `startDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `um_sub_authorize`
--

LOCK TABLES `um_sub_authorize` WRITE;
/*!40000 ALTER TABLE `um_sub_authorize` DISABLE KEYS */;
/*!40000 ALTER TABLE `um_sub_authorize` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `um_user`
--

DROP TABLE IF EXISTS `um_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `um_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `creatorId` bigint(20) DEFAULT NULL,
  `creatorName` varchar(255) DEFAULT NULL,
  `lockVersion` int(11) NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updatorId` bigint(20) DEFAULT NULL,
  `updatorName` varchar(255) DEFAULT NULL,
  `accountLife` datetime DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `authMethod` varchar(255) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `certificate` varchar(255) DEFAULT NULL,
  `certificateNo` varchar(255) DEFAULT NULL,
  `disabled` int(11) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `employeeNo` varchar(255) DEFAULT NULL,
  `fromUserId` varchar(255) DEFAULT NULL,
  `loginName` varchar(50) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `passwordAnswer` varchar(255) DEFAULT NULL,
  `passwordQuestion` varchar(255) DEFAULT NULL,
  `passwordStrength` int(11) DEFAULT NULL,
  `postalCode` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `loginName` (`loginName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `um_user`
--

LOCK TABLES `um_user` WRITE;
/*!40000 ALTER TABLE `um_user` DISABLE KEYS */;
INSERT INTO `um_user` VALUES (-10000,NULL,NULL,NULL,0,NULL,NULL,NULL,'2099-01-01 00:00:00',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,'ANONYMOUS',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'匿名用户'),(-1,NULL,NULL,NULL,0,NULL,NULL,NULL,'2099-01-01 00:00:00',NULL,'com.jinhe.tss.um.sso.UMPasswordIdentifier',NULL,NULL,NULL,0,NULL,NULL,NULL,'Admin','E5E0A2593A3AE4C038081D5F113CEC78',NULL,NULL,NULL,NULL,NULL,NULL,'系统管理员');
/*!40000 ALTER TABLE `um_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `view_channel_resource`
--

DROP TABLE IF EXISTS `view_channel_resource`;
/*!50001 DROP VIEW IF EXISTS `view_channel_resource`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `view_channel_resource` (
  `id` tinyint NOT NULL,
  `name` tinyint NOT NULL,
  `parentId` tinyint NOT NULL,
  `decode` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `view_group_resource`
--

DROP TABLE IF EXISTS `view_group_resource`;
/*!50001 DROP VIEW IF EXISTS `view_group_resource`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `view_group_resource` (
  `ID` tinyint NOT NULL,
  `parentId` tinyint NOT NULL,
  `name` tinyint NOT NULL,
  `decode` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `view_navigator_resource`
--

DROP TABLE IF EXISTS `view_navigator_resource`;
/*!50001 DROP VIEW IF EXISTS `view_navigator_resource`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `view_navigator_resource` (
  `id` tinyint NOT NULL,
  `name` tinyint NOT NULL,
  `parentId` tinyint NOT NULL,
  `decode` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `view_portal_resource`
--

DROP TABLE IF EXISTS `view_portal_resource`;
/*!50001 DROP VIEW IF EXISTS `view_portal_resource`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `view_portal_resource` (
  `id` tinyint NOT NULL,
  `name` tinyint NOT NULL,
  `parentId` tinyint NOT NULL,
  `decode` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `view_role_resource`
--

DROP TABLE IF EXISTS `view_role_resource`;
/*!50001 DROP VIEW IF EXISTS `view_role_resource`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `view_role_resource` (
  `ID` tinyint NOT NULL,
  `parentId` tinyint NOT NULL,
  `name` tinyint NOT NULL,
  `decode` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `view_roleuser`
--

DROP TABLE IF EXISTS `view_roleuser`;
/*!50001 DROP VIEW IF EXISTS `view_roleuser`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `view_roleuser` (
  `userId` tinyint NOT NULL,
  `roleId` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `view_roleuser4subauthorize`
--

DROP TABLE IF EXISTS `view_roleuser4subauthorize`;
/*!50001 DROP VIEW IF EXISTS `view_roleuser4subauthorize`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `view_roleuser4subauthorize` (
  `userId` tinyint NOT NULL,
  `roleId` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `view_channel_resource`
--

/*!50001 DROP TABLE IF EXISTS `view_channel_resource`*/;
/*!50001 DROP VIEW IF EXISTS `view_channel_resource`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_channel_resource` AS select -(1) AS `id`,'全部' AS `name`,0 AS `parentId`,'00001' AS `decode` union select `cms_channel`.`id` AS `id`,`cms_channel`.`name` AS `name`,`cms_channel`.`parentId` AS `parentId`,`cms_channel`.`decode` AS `decode` from `cms_channel` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_group_resource`
--

/*!50001 DROP TABLE IF EXISTS `view_group_resource`*/;
/*!50001 DROP VIEW IF EXISTS `view_group_resource`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_group_resource` AS select `um_group`.`id` AS `ID`,`um_group`.`parentId` AS `parentId`,`um_group`.`name` AS `name`,`um_group`.`decode` AS `decode` from `um_group` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_navigator_resource`
--

/*!50001 DROP TABLE IF EXISTS `view_navigator_resource`*/;
/*!50001 DROP VIEW IF EXISTS `view_navigator_resource`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_navigator_resource` AS select 0 AS `id`,'root' AS `name`,-(1) AS `parentId`,'00001' AS `decode` union select `portal_navigator`.`id` AS `id`,`portal_navigator`.`name` AS `name`,`portal_navigator`.`parentId` AS `parentid`,`portal_navigator`.`decode` AS `decode` from `portal_navigator` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_portal_resource`
--

/*!50001 DROP TABLE IF EXISTS `view_portal_resource`*/;
/*!50001 DROP VIEW IF EXISTS `view_portal_resource`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_portal_resource` AS select 0 AS `id`,'root' AS `name`,-(1) AS `parentId`,'00001' AS `decode` union select `portal_structure`.`id` AS `id`,`portal_structure`.`name` AS `name`,`portal_structure`.`parentId` AS `parentId`,`portal_structure`.`decode` AS `decode` from `portal_structure` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_role_resource`
--

/*!50001 DROP TABLE IF EXISTS `view_role_resource`*/;
/*!50001 DROP VIEW IF EXISTS `view_role_resource`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_role_resource` AS select `um_role`.`id` AS `ID`,`um_role`.`parentId` AS `parentId`,`um_role`.`name` AS `name`,`um_role`.`decode` AS `decode` from `um_role` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_roleuser`
--

/*!50001 DROP TABLE IF EXISTS `view_roleuser`*/;
/*!50001 DROP VIEW IF EXISTS `view_roleuser`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_roleuser` AS select `u`.`id` AS `userId`,`r`.`id` AS `roleId` from ((`um_role` `r` join `um_roleuser` `ru`) join `um_user` `u`) where ((`r`.`id` = `ru`.`roleId`) and (`ru`.`userId` = `u`.`id`) and (`u`.`disabled` <> 1) and (`r`.`disabled` <> 1) and (sysdate() between `r`.`startDate` and `r`.`endDate`) and isnull(`ru`.`strategyId`)) union all select `u`.`id` AS `userId`,`r`.`id` AS `roleId` from (((`um_role` `r` join `um_roleuser` `ru`) join `um_user` `u`) join `um_sub_authorize` `s`) where ((`r`.`id` = `ru`.`roleId`) and (`ru`.`userId` = `u`.`id`) and (`u`.`disabled` <> 1) and (`r`.`disabled` <> 1) and (sysdate() between `r`.`startDate` and `r`.`endDate`) and (`s`.`id` = `ru`.`strategyId`) and (sysdate() between `s`.`startDate` and `s`.`endDate`) and (`s`.`disabled` <> 1)) union all select `u`.`id` AS `userId`,`r`.`id` AS `roleId` from ((((`um_group` `g` join `um_rolegroup` `rg`) join `um_role` `r`) join `um_groupuser` `gu`) join `um_user` `u`) where ((`g`.`id` = `rg`.`groupId`) and (`rg`.`roleId` = `r`.`id`) and (`g`.`id` = `gu`.`groupId`) and (`gu`.`userId` = `u`.`id`) and (`u`.`disabled` <> 1) and (`r`.`disabled` <> 1) and (`g`.`disabled` <> 1) and (sysdate() between `r`.`startDate` and `r`.`endDate`) and isnull(`rg`.`strategyId`)) union all select `u`.`id` AS `userId`,`r`.`id` AS `roleId` from (((((`um_group` `g` join `um_rolegroup` `rg`) join `um_role` `r`) join `um_groupuser` `gu`) join `um_user` `u`) join `um_sub_authorize` `s`) where ((`g`.`id` = `rg`.`groupId`) and (`rg`.`roleId` = `r`.`id`) and (`g`.`id` = `gu`.`groupId`) and (`gu`.`userId` = `u`.`id`) and (`u`.`disabled` <> 1) and (`r`.`disabled` <> 1) and (`g`.`disabled` <> 1) and (sysdate() between `r`.`startDate` and `r`.`endDate`) and (`s`.`id` = `rg`.`strategyId`) and (sysdate() between `s`.`startDate` and `s`.`endDate`) and (`s`.`disabled` <> 1)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_roleuser4subauthorize`
--

/*!50001 DROP TABLE IF EXISTS `view_roleuser4subauthorize`*/;
/*!50001 DROP VIEW IF EXISTS `view_roleuser4subauthorize`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_roleuser4subauthorize` AS select `u`.`id` AS `userId`,`r`.`id` AS `roleId` from ((`um_role` `r` join `um_roleuser` `ru`) join `um_user` `u`) where ((`r`.`id` = `ru`.`roleId`) and (`ru`.`userId` = `u`.`id`) and (`u`.`disabled` <> 1) and (`r`.`disabled` <> 1) and (sysdate() between `r`.`startDate` and `r`.`endDate`) and isnull(`ru`.`strategyId`)) union all select `u`.`id` AS `userId`,`r`.`id` AS `roleId` from ((((`um_group` `g` join `um_rolegroup` `rg`) join `um_role` `r`) join `um_groupuser` `gu`) join `um_user` `u`) where ((`g`.`id` = `rg`.`groupId`) and (`rg`.`roleId` = `r`.`id`) and (`g`.`id` = `gu`.`groupId`) and (`gu`.`userId` = `u`.`id`) and (`u`.`disabled` <> 1) and (`r`.`disabled` <> 1) and (`g`.`disabled` <> 1) and (sysdate() between `r`.`startDate` and `r`.`endDate`) and isnull(`rg`.`strategyId`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-02-04 12:55:57
