2013年前代码托管在google code，摘部分提交记录以记之
  
2013-01-01: use git replace svn, still remain svn; pom adjust；h2/mysql support  
2012-12-30: tomcat7.0 and eclipse4.2 for JEE  
2012-12-29: transfer to servlet 3.0，use annotation config web framework  
2012-12-21: framework refactor  
2012-12-19: test cache's function and performance  
2012-12-09: 缓存池代码code review及部分重构; test cache  
  
  
2012-04-07: 完成自动化持续构建，包括生成项目网站，代码质量分析报告，代码bug报告，单元测试报告，测试覆盖率报告等  
2012-04-04: 不再使用Ant自动构建测试，重新使用Maven来跑测试，绕了一圈又回到了原点，为了hudson，一切为了CI。  
2012-03-05: 用clover统计测试覆盖率  
2012-03-02: depoly tss to tomcat6014。  
2012-02-29: Test one by one,  
            Test by Ant,  
            Test for Init DB,  
            Over!  
            完成database schema及数据初始化.  
2012-02-27: 使用Ant执行单元测试，生成测试报告  
2012-02-25: TSS单元测试完结  
2012-02-20: 完成对门户结构的基本测试  
2012-02-17: 完成对portlet模块的单元测试；完成对门户布局器、修饰器模块的单元测试  
2012-01-14: 完成对UM项目的代码重构及测试  
2011-09-22: 改用标准JPA实现数据存取，所有实体和DAO进行改造  
2011-09-13: 开始对Core项目重构后的代码进行单元测试  
2011-08-28: 在线用户库模块代码重构  
2011-08-27: 单点登录SSO模块代码重构  
2011-08-12: 代码开始交由Google code 托管  
2011-08-17 ----- 2012-01-01：  
   1、升级：Ant --> Maven2   
	    Spring1.2 --> Spring3.0   
	    Hibernate --> JPA2.0  
	    JDK1.6 --> JDK1.6  
	    普通测试 --> TDD  
   2、重构Framework后台代码  
   3、理清并统一权限过滤机制;瘦身：去掉一些不常用的功能操作  
   4、引入内存数据库H2作为单元测试数据库  
   5、单元测试：完善重点模块的单元测试，SSO、Cache、全文检索(lucene)、权限、门户机制等  
   6、Servlet测试：引入jetty作为内嵌web服务器测试web层