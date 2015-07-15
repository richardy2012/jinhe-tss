
TOMCAT_HOME="/Users/jinpujun/Desktop/tomcat7063"


mvn clean install -Pdemo -Dmaven.test.skip=true


set -m

rm -rf $TOMCAT_HOME/webapps/tss

cp webapp/target/tss.war $TOMCAT_HOME/webapps

cd $TOMCAT_HOME/bin

./startup.sh

tail -f $TOMCAT_HOME/logs/catalina.out

