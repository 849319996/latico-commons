FROM hub.c.163.com/library/java

MAINTAINER springboot

RUN mkdir -p /home/laticosoft/springboot

COPY ./latico-commons-docker.jar /home/laticosoft/springboot/latico-commons-docker.jar

ENTRYPOINT ["java","-jar","latico-commons-docker.jar"]
