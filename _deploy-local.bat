:: deploy to windows localhost

rd /s/q D:\Tomcat7042\webapps\tss
call mvn clean install  -Pdemo -Dmaven.test.skip=true
xcopy /Y webapp\target\tss.war  D:\Tomcat7042\webapps
::start D:\Tomcat7042\bin\startup.batlocal