2014-10-10
1、提升后台代码测试覆盖率至90%

2014-09-20
1、升级BtrBI；迁移wmsBI至 TSS3.0平台。

2014-09-17
1、TSS 3.0 （2014中秋版） release
 
2014-09-09 
1、重写所有web代码，全面支持各大新版本的主流浏览器
      tssJS 8.1 --> 8.2
      Menu、Balloon 8.3--> 8.4
      tssAjax、WS 8.5 --> 8.11
      tssGrid 8.12 --> 8.17
      tssForm 8.18 --> 8.19
      tssTree 8.20--> 8.24
      tssUtils Log Cache Param 8.25 --> 8.29
      tssUM 8.30 -->8.31 & 9.6
      tssCMS 9.1
      tssPortal 9.2
      tssDM 9.3 -- 9.5
      IE and FF  test 9.7 --> 9.8 

2014-07-16
1、重写定时任务配置模块，由系统参数模块配置实现。可定时同步用户、发布文章；定时执行报表查询并发送邮件等。

2014-05-25
1、开发完成了第一个基于TSS基础平台的扩展系统jinhe-dms（数据挖掘系统），借此验证了TSS的权限模型及SSO机制的有效性
2、完善了TSS管理后台的管理界面，改用门户生成，可授权。
3、基于jinhe-dms的百世快运BtrBI2.0发布并正式启用

2014-01-02
1、TSS 2.0 （2014元旦版） release。
2、解决了浏览器兼容性问题（支持IE及Chrome），及提高测试覆盖率至80%。

2013-10-08
1、TSS 1.0 （2013国庆Beta版） release
2、Junit框架升级至4.6，测试覆盖率提升至70%

2013-10-02
1、MVC框架由webwork改成spring mvc，后台action地址采用restful风格
2、modules（cache、param、log）完成前后台重构
3、um、cms、portal完成前后台重构，

2013-05-07
1、Transfer menu、balloon、workspace、XFrom、Grid、Tree from HTC to common js， oop style

2013-02-04
1、common dom function、event、xml operation、ajax and so on refactor

2013-01-13
1、开始重构web前台

2013-01-01
1、完成新一轮升级
    -   svn --> git，代码托管到Github
    -   oracle --> h2 / mysql
    -    tomcat6.0 --> tomcat7.0,  升至 servlet 3.0，use annotation config web framework

2012-12-21
1、framework refactor

2012-12-19
1、缓存池代码code review及部分重构; test cache
2、 test cache's function and performance

2012-04-07
1、完成自动化持续构建，包括生成项目网站，代码质量分析报告，代码bug报告，单元测试报告，测试覆盖率报告等

2012-04-04
1、不再使用Ant自动构建测试，重新使用Maven来跑测试，绕了一圈又回到了原点，为了hudson，一切为了CI。

2012-03-05
1、TSS单元测试完结
2、使用Ant执行单元测试，生成测试报告
3、用clover统计测试覆盖率
4、完成database schema及数据初始化.
5、depoly tss to tomcat6014

2012-02-20 
1、 完成对portlet模块的单元测试；完成对门户布局器、修饰器模块的单元测试
2、完成对门户结构的基本测试

2012-01-14
1、完成对UM项目的代码重构及测试

2011-09-22
1、改用标准JPA实现数据存取，所有实体和DAO进行改造、

2011-09-13
1、开始对Core项目重构后的代码进行单元测试

2011-08-28: 
1、在线用户库模块代码重构
2、 单点登录SSO模块代码重构

2011-08-12
1、代码托管至Google code 

2011-08-17 -- 2012-01-01：
1、升级平台：
    - Ant --> Maven2
    - Spring1.2 --> Spring3.0
    - Hibernate --> JPA2.0
    - JDK1.6 --> JDK1.6
    - 普通测试 --> TDD
2、重构Framework后台代码
3、理清并统一权限过滤机制;瘦身：去掉一些不常用的功能操作
4、引入内存数据库H2作为单元测试数据库
5、单元测试：完善重点模块的单元测试，SSO、Cache、全文检索(lucene)、权限、门户机制等
6、Servlet测试：引入jetty作为内嵌web服务器测试web层

2009-05-27 -- 2011-08-17
1、北漂百日后回杭
2、做仓储系统，全面接受TDD开发思想；接触数据分析

2009-05-27
1、 引入工作流引擎（JBPM）、规则引擎drolls
2、第二轮重构，整理代码结构，合并模块，整合配置，整合数据库，完善平台Demo。

2009-01-05
1、实现门户静态发布及远程发布，，常用门户组件标准化、图形化创建栏目相关菜单、实现组件预览

2008-11-1 
1、实施了浙江交行内网门户
2、实现平台外应用到平台应用的SSO解决方案。（财通证券项目：汇信CA-->Domino-->Websphere-->平台）

2008-07-12
1、实施了贵州财政厅FOA二期
2、平台框架包级结构调整：
     1、util工具包，异常、cache核心部分，parser
     2、框架Framework(主要是SSO)
     3、support(公用超类、进度条，hibernate/spring/webwork相关扩展)
     4、参数管理
     5、日志、动态属性（基于UMS授权）、回收站、cache展示部分
     6、UM、Portal、CMS等各个应用
3、回收站、系统参数管理、进度条、用户同步等模块代码重构

2007-12-20
1、实施了贵州财政厅FOA一期

2007-06-11 
1、UM模块代码重构（2007-04月）
2、portal模块代码重构，权限、日志、层码decode、操作信息operator、动态属性等按AOP模式进行拦截配置，将这些模块功能与业务代码分离开来
3、portal模块新增了动态Portlet（采用freemarker模板引擎实现）

2007-02-12
1、平台二期结束，对一期的不足进行了完善，并补充了包括回收站、日志、缓存系统参数在内的公共模块组件。
 
2006-11-21
1、平台一期结束，实施了市工商内网门户。

2006-05-31
1、结束历时三个月的千岛湖封闭开发，基本完成编码工作。

2006-02-14
1、基础平台正式立项启动。

2005-09-01
1、基础平台前身（浙江省财政厅金财工程基础资料项目）立项启动，开始历时近半年的莲花宾馆封闭式开发。

2005-05-20
1、毕业设计答辩，题目是《企业自动化建站》

2003-08-01 -- 2005-03-01
1、在学校以做网站赚生活费谋生。