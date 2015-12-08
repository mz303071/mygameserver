Getting Started by Mike Zharov

(Forwarding Usama's project foundation tutorials)
Setting up Tomcat on Windows, Mac OS X

http://www3.ntu.edu.sg/home/ehchua/programming/howto/tomcat_howto.html#zz-2.6

Setting up MySQL 5

http://www3.ntu.edu.sg/home/ehchua/programming/sql/MySQL_HowTo.html

Intro to JDBC

http://www3.ntu.edu.sg/home/ehchua/programming/java/JDBC_Basic.html


tips:
Folow tutorials to get a base system running
needed: Tomcat, JDBC plugin, JDK, MYSQLServer
make a user for mysql and tomcat as USERNAME: myuser PASSWORD: xxxx
make you have to setup a JAVAHOME variable in your variables pannel
should take you about 2-4 hours setting it up:


launch adjustments:
once you have completed everything, drop the folder 'gameserver' into your webaps folder
inside gameserver copy inside file GAMESERVER.ods (apache open office format can be openned by google drive if need to )
into your MYSQL terminal(will generate database)

I am open to insights on how to modify the DB

design insights:

web.xml in folder directory maps the .java servlets with the online adresses
using <form type=get action"adress"> html for buttons and input fields
servlets generate html for browser
request.getParameter("x") gets the value fun as is passed by /myservlet?x=fun&z=joy

