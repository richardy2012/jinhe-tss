DATE_ADD(now(),  INTERVAL 2 DAY)
date_format(日期字段,’%Y-%m-%d’)

SET SQL_SAFE_UPDATES = 0;


{
    "customizerClass": "com.jinhe.tss.framework.persistence.connpool.ConnPoolCustomizer",
    "poolClass": "com.jinhe.tss.cache.extension.ReusablePool",
    "code": "connpool-wms",
    "name": "DB连接池-WMS",
    "cyclelife": "180000",
    "paramFile": "oracle.jdbc.driver.OracleDriver,jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS = (PROTOCOL = TCP)(HOST = 10.8.73.139)(PORT = 1521)) (CONNECT_DATA = (SERVER = DEDICATED) (SERVICE_NAME = wms))),wms,wms800best",
    "interruptTime": "1000",
    "initNum":"1",
    "poolSize": "10"
}

{
    "customizerClass": "com.jinhe.tss.framework.persistence.connpool.ConnPoolCustomizer",
    "poolClass": "com.jinhe.tss.cache.extension.ReusablePool",
    "code": "connpool-wms-mysql",
    "name": "DB连接池-WMS-MySQL",
    "cyclelife": "180000",
    "paramFile": "com.mysql.jdbc.Driver,jdbc:mysql://10.8.10.30:3306/wmsdata?useUnicode=true&amp;characterEncoding=utf8,wmsxProd,wmsdata@800best",
    "interruptTime": "1000",
    "initNum":"1",
    "poolSize": "10"
}

{
    "customizerClass": "com.jinhe.tss.framework.persistence.connpool.ConnPoolCustomizer",
    "poolClass": "com.jinhe.tss.cache.extension.ReusablePool",
    "code": "connpool-bsd-oracle",
    "name": "DB连接池-波塞冬",
    "cyclelife": "720000",
    "paramFile": "oracle.jdbc.driver.OracleDriver,jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS = (PROTOCOL = TCP)(HOST = 10.8.72.70)(PORT = 1521))(CONNECT_DATA = (SERVER = DEDICATED)(SERVICE_NAME = kybi.800best.com))),usr_vf,usr_vf2",
    "interruptTime": "1000",
    "initNum":"0",
    "poolSize": "20"
}
