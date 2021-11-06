FROM adoptopenjdk/openjdk13 AS build
WORKDIR /app
ADD gradlew /app/
ADD gradle /app/gradle/
RUN sed -i 's/\r//' gradlew && bash ./gradlew --version
ADD build.gradle /app
RUN bash ./gradlew classes || \
 bash ./gradlew classes || \
 bash ./gradlew classes || \
 bash ./gradlew classes || \
 bash ./gradlew classes || \
 bash ./gradlew classes
ADD src /app/src/
RUN bash ./gradlew assemble || \
 bash ./gradlew assemble || \
 bash ./gradlew assemble

FROM ubuntu:focal AS wkhtmltopdf
RUN sed 's/main$/main universe/' -i /etc/apt/sources.list
RUN apt-get update
RUN apt-get upgrade -y
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get install -y build-essential xorg libssl-dev libxrender-dev wget gdebi
RUN wget https://github.com/wkhtmltopdf/packaging/releases/download/0.12.6-rc/wkhtmltox_0.12.6-0.20200605.30.rc.faa06fa.focal_amd64.deb
RUN gdebi --n wkhtmltox_0.12.6-0.20200605.30.rc.faa06fa.focal_amd64.deb

RUN apt-get install -y xvfb openjdk-17-jre-headless

CMD /bin/sh -c "Xvfb -screen 0 1280x1024x16 -ac &  export DISPLAY=:0 ; exec java -jar /app/app*.jar"
