#Pentcho Tchomakov 260632861

while true
do
    PID=$(echo `ps -al|grep sleep`|cut -f4 -d' ')
    NI=$(echo `ps -al|grep sleep`|cut -f8 -d' ')
    CMD=$(echo `ps a |grep sleep`|cut -f5 -d' ')

if [ "$CMD" = "sleep" ]
then
    if [ $NI -le 4 ]
    then
    kill -9 $PID
    fi
fi
done
