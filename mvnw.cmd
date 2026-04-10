@echo off
setlocal
set "MVNW_DIR=%~dp0"
"%MVNW_DIR%.tools\apache-maven-3.9.9\bin\mvn.cmd" %*
