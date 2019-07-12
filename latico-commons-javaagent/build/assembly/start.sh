#!/bin/bash
# ${project.build.finalName}
export programname=$(cat ./programname.cfg)
export javapath=$(cat ./javapath.cfg)
export classpathconf=$(cat ./classpath-unix.properties)
workdir=$(cd $(dirname $0); pwd)

PID=`ps -ef|grep $workdir|grep ${programname}|grep -v grep|awk '{print $2}'`

if [ -z $PID ];then
    ${javapath} -Dprogramname=${programname} -Dprogrampath=$workdir -XX:+HeapDumpOnOutOfMemoryError -cp lib/${programname}.jar:${classpathconf##*=} ${mainClass} ${mainArg} >/dev/null 2>err.log &
    PID=`ps -ef|grep $workdir|grep ${programname}|grep -v grep|awk '{print $2}'`
    echo "PID=$PID the program <<$workdir && ${programname}>> starting..."
else
    echo "PID=$PID the program <<$workdir && ${programname}>> has been running.Please stop it firstly."
fi