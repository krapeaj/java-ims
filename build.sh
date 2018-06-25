#! /bin/bash

export JAVA_HOME="$HOME/java"
export JAVA_OPTS="$JAVA_OPTS -Djava.security.egd=file:/dev/./urandom"

REPO=$HOME/java-ims
TARGET=$REPO/target
JAR_FILE_NAME=$REPO/build/libs/java-ims-1.0.0.jar

cd $REPO
git fetch
master=$(git rev-parse nginx)
remote=$(git rev-parse origin/nginx)

if [[ $master == $remote ]]; then
    echo "[$(date)] Nothing to do"
    exit 0
fi
echo "[$(date)] Start deploy.."

echo "0. Git pull.."
git merge origin/nginx 

echo "1.Stop server.."
CURRENT_PID=$(pgrep -f java-ims)

echo "$CURRENT_PID"

if [ -z $CURRENT_PID ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -2 $CURRENT_PID"
    kill -2 $CURRENT_PID
    sleep 5
fi

echo "2.Build gradle"
./gradlew clean build
# check gradle build
if [ ! -d $TARGET ]
then
    echo "Error: $TARGET directory not exist!"
    exit 1
fi

cd $TARGET
java -jar $JAR_FILE_NAME > $HOME/error.log 2>&1
