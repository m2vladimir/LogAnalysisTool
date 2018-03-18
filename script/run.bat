@echo off
SET JAVA_HOME=

SET OPTIONS=Djava.util.logging.config.file=config\logging.cfg

SET CP=.
SET CP=%CP%;lib\*

%JAVA_HOME%java -%OPTIONS% -cp %CP% com.kyriba.parser.application.ConsoleLauncher config\config.cfg

pause