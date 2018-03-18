@echo off

CP=

OPTIONS=Djava.util.logging.config.file=config\logging.cfg

%JAVA_HOME% -jar -%OPTIONS% -classpath %CP% com.kyriba.parser.application.ConsoleLauncher