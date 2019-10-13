一、windows安装docker
1、如果是windows10专业版那些高级版本，可以下载安装Docker for Windows Installer.exe；
2、其他windows版本，下载安装：DockerToolbox.exe；
注意：如果是DockerToolbox.exe安装，会安装virtualBox，安装virtualBox是要重启系统才能生效，要不然会报如下错误：
VirtualBox is not installed. Please re-run the Toolbox Installer and try again. 
Looks like something went wrong in step ´Looking for vboxmanage.exe´… Press any srcKey to continue… 

Docker Quickstart Terminal用来启动docker服务器
Oracle VM VirtualBox用来管理docker服务器（因为是虚拟机）


二、常用命令
注意：匹配容器ID和镜像ID的时候都是模糊匹配，只需要输入前面的字符保证没有跟其他的重复即可。
docker version:查看客户端和服务端版本：
docker run 镜像ID : 运行镜像，得到一个容器ID和运行容器
docker stop 容器ID：暂停容器
docker start 容器ID: 启动一个暂停的容器
docker restart  容器ID: 重启容器
docker rm 容器ID：删除一个容器
docker ps：列出所有正在运行的容器
docker ps -a：列出所有容器
docker image ls 或者docker images ：列出所有的镜像
docker-machine ip default ：查看容器的默认IP，一般都是192.168.99.100
docker info：查看docker的信息
docker rmi <image id> ：删除images，通过image的id来指定删除谁

docker build -t="jpress:latest" .

$ docker images
REPOSITORY                   TAG                 IMAGE ID            CREATED             SIZE
hub.c.163.com/public/nginx   1.2.1               2dc68ff797db        2 years ago         172MB

Administrator@latico MINGW64 /c/Program Files/Docker Toolbox
$ docker run -d -p 80:80 --name mynginx 2dc68ff797db

注意：mynginx是容器名称，一个docker里面唯一；




三、docker网络
docker跟宿主机的网络连接：
1、网桥方式（docker的默认方式），docker有自己独立的IP和端口；
2、主机模式，跟宿主机共用IP和端口；
3、不联网方式；

windows7等非windows10系统，可以通过Docker Quickstart Terminal工具连接本地的docker虚拟机中，
也可以通过其他工具，连接远程的docker虚拟机，就需要如下方式：
可以通过CRT等SSH工具连进docker虚拟机中
默认的连接方式如下：
默认IP：192.168.99.100 端口22
默认的登录名为docker，默认密码为tcuser

四、在线文档
http://www.dockerinfo.net/document


五、执行Dockerfile文件
程序打包后，把Dockerfile文件拷贝到target目录下执行。
比如执行如下命令：
docker build -t nginx:v1.1 .
-t为执行镜像的名称，千万不要忽略结尾处的.，它表示当前目录，nginx是镜像名称，v1.1是版本

然后访问：
===========

1、docker底层跟虚拟机差不多，比如网络连接也是通过桥接或者主机模式等。
-d是后台运行 -p指定端口映射，把主机上的8081映射到容器的8080，--name指定容器的名称
docker run -d -p 8081:8080 --name springboot aca5b8e97411


如果发生如下错误，说明Dockerfile的jar包目录不对
$ docker run d34001139fc3
Error: Unable to access jarfile latico-commons-docker.jar

接下来我们使用该命令进入一个已经在运行的容器

$ docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES
b4476bd383b5        aca5b8e97411        "java -jar /home/lat…"   6 minutes ago       Up 7 minutes        0.0.0.0:8080->8080/tcp   springboot

Administrator@latico MINGW64 /c/Program Files/Docker Toolbox

进入容器内部系统
$ docker exec -it b4476bd383b542 /bin/bash
root@b4476bd383b5:/#
root@b4476bd383b5:/# ls
bin  boot  dev  etc  home  lib  lib64  logs  media  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var

退出容器系统
root@b4476bd383b5:/#exit
$



整docker练习的时候，不想直接复制搭建好的虚拟机，需要获取里面的docker镜像，第一种解决方案是，将镜像推送到公有的镜像仓库，然后pull下来，第二种，将镜像打包，然后拷贝到第二台服务器中

#将镜像存储
docker save nginx:latest > /root/docker-images/nginx.tar

#导入镜像文件
docker load --input /root/docker-images/nginx.tar

#通过符号的方式来导入
docker load < /root/docker-images/nginx.tar

不同的Linux之间copy文件通常有4种方法 
ftp、samba服务、sftp、scp

拷贝单个文件
1、将本地文件拷贝到服务器

#将本地的nginx.tar 拷贝到服务器上的/root/docker-images目录
scp nginx.tar root@192.168.253.112:/root/docker-images

#将远程的test.txt文件拷贝到本地目录的dou2.txt文件
scp root@192.168.253.112:/root/docker-images/dou/test.txt dou2.txt

#将本地文件夹下面的dou文件夹拷贝到服务器上
scp -r ./dou/ root@192.168.253.112:/root/docker-images

#拷贝远程服务器的文件夹dou到本地目录下./test2文件夹
scp -r root@192.168.253.112:/root/docker-images/dou/ ./test2/






Kubernetes
本文内容仅为个人理解，如有偏颇，欢迎指正。

一、传统的运维方式
在了解Kubernetes之前，我们有必要先简单了解一下传统的运维模式。在传统的项目架构中(单体or微服务)，我们一般将项目打包为war或fatJar的方式进行部署。

在部署时，需要人工创建相应的服务器及资源，并搭建项目运行的依赖环境，预估服务需要占用的内存与CPU，同事还要考虑到高可用的部署环境，在不同配置的服务器上部署相应的服务。当服务意外崩溃或者服务器意外宕机时，需要人工处理。总结一下传统部署的不足如下:

依赖服务器环境，需要各服务器资源统一。
无法充分利用服务器等资源，使用率一般仅能达到70%。
无法或很难做到容灾恢复。
需要人工进行服务扩容，修改服务配置。
服务资源散乱(域名，服务器，负载，数据库)，无法做集中管理。
时间消耗较多，增加运维成本。
需要借助第三方工具进行资源监控，较为麻烦。
需要对开发、测试、生产环境进行区别管理。
要想解决以上的问题是相对比较麻烦的，特别是现在的项目多为微服务项目，少则几十，多则上百，极大的增加了运维的难度和成本。

一、Kubernetes是什么？
官方文档中描述为:v

Kubernetes一个用于容器集群的自动化部署、扩容以及运维的开源平台。通过Kubernetes,你可以快速有效地响应用户需求;快速而有预期地部署你的应用;极速地扩展你的应用;无缝对接新应用功能;节省资源，优化硬件资源的使用。为容器编排管理提供了完整的开源方案。

介绍一下其中提到的几个词:

容器 
我们现在常说的容器一般是指Docker容器，通过容器隔离的特性和宿主机进行解耦，使我们的服务不需要依赖于宿主机而运行，与宿主机互不影响，Docker容器十分轻量。而kubernetes则负责管理服务中所有的Docker容器，创建、运行、重启与删除容器。
快速响应 
个人理解为两个方面。一、新增或者修改需求时，可以快速进行部署测试(CICD)；二、kubernetes可以根据不同条件进行动态扩缩容，举个栗子，用户访问量突然由1000人上升到100000人时，现有的服务已经无法支撑，kubernetes会自动将用户服务模块增加更多实例以保证当前的系统访问量。
扩展 
在快速响应的特点中已经有所提及，这里再补充一点: Kubernetes内部有完善的注册发现机制，当某个服务的实例增加时，kubernetes会自动将其加入服务列表中，免除在传统运维中需要人工维护服务列表的问题。
对接新应用 
kubernetes是一个通用的容器编排框架，支持不同类型的语言，或者是语言无关的，新增加的应用都会以一个新的对象进行接入。
硬件资源 
这一点我觉得是kubernetess很基本但是非常重要的一个优点了，kubernetes在部署应用时会自动检查各个服务器的cpu与内存使用量，同时会根据服务申请的cpu与内存资源，将服务部署到最合适的服务器。(其实这就是容器调度的核心功能了)
小知识: 因kubernetes名字过长，一般简称为k8s，因为k与s之间有8个字母，故而称之。

二、Kubernetes解决了什么问题？
下面以几个case进行阐述，便于理解。

服务器环境
kubernetes是使用Docker进行容器管理的，所以天生具备Docker的所有特性，只需要使用相应环境的Docker镜像就可以运行服务，还需要关心宿主机是redhat、centos还是ubuntu，只要在宿主机上安装Docker环境即可，相比传统运维，减少了各种依赖环境的冲突，降低运维成本，也方便整体服务的迁移。

服务器资源管理
对于kubernetes来说，是不关心有几台服务器的，每个服务器都是一个资源对象(Node)，kubernetes关心的是这个Node上有多少可用的cpu和内存。例如现在有两台服务器

server01 (4c16g), 已用(2c7.5G)
server02 (4c16g), 已用(3c13G)
现在有一个服务ServiceA需要部署，ServiceA申明自己运行需要至少3G内存，这时kubernetes会根据调度策略将其部署到server01上，很明显server01的资源是更加充足的。实际上kubernetes的调度策略要复杂的多，kubernetes会监控整体服务器资源的状态进行调度，而以前的运维方式只能由人工判断资源使用。这里只做简单示例。
