<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>

<body>
${book?if_exists }
${(books.b)!"我是默认的"}
${date?string('yyyy-MM-dd')} //日期格式
${number?string.number} 20 //三种不同的数字格式
${number?string.currency}<#-- $20.00 -->
${number?string.percent}<#-- 20% -->
<#assign foo=true /> //声明变量,插入布尔值进行显示
${foo?string("yes","no")} <#-- yes -->
${book?cap_first}
${user.id}-------${user.name}------${user.group!} <#-- !后为空就不输出 -->
<#--${user.group.name!}--><#-- 按照以上的方式加! freemarker仅仅只会判断group.name是不是空值 -->
${(user.group.name)!"1234"}

${(a.b)!"没有a.b元素"}

<#--
!:指定缺失变量的默认值
??:判断某个变量是否存在,返回boolean值
-->
<#if (a.b)??> <#--if后不用加$-->
    不为空
<#else>
    为空
</#if>
<a href='www.baidu.com' src='#'>阿里巴巴</a>
</body>
</html>