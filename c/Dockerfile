FROM adoptopenjdk/openjdk13 AS build
WORKDIR /app
ADD gradlew /app/
ADD gradle /app/gradle/
RUN bash ./gradlew --version
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

FROM ubuntu:14.04 AS wkhtmltopdf
RUN sed 's/main$/main universe/' -i /etc/apt/sources.list
RUN apt-get update
RUN apt-get upgrade -y
RUN apt-get install -y build-essential xorg libssl-dev libxrender-dev wget gdebi
RUN wget https://bitbucket.org/wkhtmltopdf/wkhtmltopdf/downloads/wkhtmltox-0.13.0-alpha-7b36694_linux-trusty-amd64.deb
RUN gdebi --n wkhtmltox-0.13.0-alpha-7b36694_linux-trusty-amd64.deb

FROM adoptopenjdk/openjdk13:debian-jre
RUN apt-get update && apt-get install -y xvfb
COPY --from=build /app/build/libs/app*.jar /app/
COPY --from=wkhtmltopdf /usr/local/bin/wkhtmltopdf /usr/local/bin/
COPY --from=wkhtmltopdf /usr/lib/x86_64-linux-gnu/libxcb.so.1 /usr/lib/
COPY --from=wkhtmltopdf /usr/lib/x86_64-linux-gnu/libfontconfig.so.1 /usr/lib/
COPY --from=wkhtmltopdf /usr/lib/x86_64-linux-gnu/libfreetype.so.6 /usr/lib/
COPY --from=wkhtmltopdf /usr/lib/x86_64-linux-gnu/libjpeg.so.8 /usr/lib/
COPY --from=wkhtmltopdf /usr/lib/x86_64-linux-gnu/libicui18n.so.52 /usr/lib/
COPY --from=wkhtmltopdf /usr/lib/x86_64-linux-gnu/libicuuc.so.52 /usr/lib/
COPY --from=wkhtmltopdf /lib/x86_64-linux-gnu/libpng12.so.0 /usr/lib/
COPY --from=wkhtmltopdf /usr/lib/x86_64-linux-gnu/libstdc++.so.6 /usr/lib/
COPY --from=wkhtmltopdf /usr/lib/x86_64-linux-gnu/libXau.so.6 /usr/lib/
COPY --from=wkhtmltopdf /usr/lib/x86_64-linux-gnu/libXdmcp.so.6 /usr/lib/
COPY --from=wkhtmltopdf /lib/x86_64-linux-gnu/libexpat.so.1 /usr/lib/
COPY --from=wkhtmltopdf /usr/lib/x86_64-linux-gnu/libicudata.so.52 /usr/lib/

CMD /bin/sh -c "Xvfb -screen 0 1280x1024x16 -ac &  export DISPLAY=:0 ; exec java -jar /app/app*.jar"
