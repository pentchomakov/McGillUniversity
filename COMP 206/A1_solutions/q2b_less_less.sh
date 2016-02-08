#!/bin/bash
#Pentcho Tchomakov 260632861

clear
echo "Script begins"
ps -u
var=$(id -u)
echo $var
kill -9 `ps -u $var | pidof 'less'`
ps -u
