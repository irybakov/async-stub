#
# async-stub Dockerfile
#
#
#

# Pull base image OpenJDK
FROM java:8

MAINTAINER Igor Rybakov <igor@rybakov.kz>

ENV APP_NAME async-stub-1.0.zip
ENV APP_DIR async-stub-1.0
ENV JAVA_OPTS -Xms128M -Xmx512M -Xss1M -XX:+CMSClassUnloadingEnabled
ENV RUN_SCRIPT async-stub

# configs
RUN mkdir -p /root/config/
#    && mkdir -p $LOG_DIR \
#    && mkdir -p $LOG_ARCHIVE_DIR

COPY ./src/main/resources/*.conf /root/config/

WORKDIR /root
COPY ./target/universal/$APP_NAME /root/
RUN unzip -q $APP_NAME

# clean zip 
RUN rm /root/$APP_NAME

WORKDIR /root/$APP_DIR/bin
CMD chmod +x $RUN_SCRIPT
EXPOSE 8082
CMD ./$RUN_SCRIPT -Dconfig.resource=/application.conf
