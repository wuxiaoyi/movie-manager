FROM registry.cn-beijing.aliyuncs.com/fishtrip/java:8

ENV APPLICATION=jarvis

WORKDIR /$APPLICATION

COPY target/$APPLICATION.jar app.jar

EXPOSE 8848
