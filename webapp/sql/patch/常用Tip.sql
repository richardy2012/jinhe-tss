DATE_ADD(now(),  INTERVAL 2 DAY)
date_format(日期字段,’%Y-%m-%d’)


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
