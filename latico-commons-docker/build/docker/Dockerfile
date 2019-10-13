FROM hub.c.163.com/library/java

MAINTAINER springboot

RUN mkdir -p /home/latico/springboot

ADD ./latico-commons-docker.jar /home/latico/springboot/latico-commons-docker.jar

ENTRYPOINT ["java","-jar","/home/latico/springboot/latico-commons-docker.jar"]
