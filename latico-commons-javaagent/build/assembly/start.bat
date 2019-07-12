set /p programname=<./programname.cfg
set /p javapath=<./javapath.cfg
set classpathconf=${classpath}
title %programname%
%javapath% -Dprogramname=%programname% -XX:+HeapDumpOnOutOfMemoryError -cp lib\%programname%.jar;%classpathconf% ${mainClass} ${mainArg} 2>err.log
pause