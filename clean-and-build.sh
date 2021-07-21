#!/bin/bash

rm -rf deployment

mkdir deployment
mkdir deployment/config

mvn clean install
cp target/printing-cost-calculator-1.0-SNAPSHOT.jar deployment/
cp src/main/resources/*.* deployment/config/


cat > "deployment/run.sh" <<- "EOF"
JAVA_OPTS="-Xms512M -Xmx1G"
java ${JAVA_OPTS} -Dloader.path="config" -jar printing-cost-calculator-1.0-SNAPSHOT.jar $*

EOF
chmod +x deployment/run.sh