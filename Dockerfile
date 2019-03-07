FROM  findepi/graalvm:polyglot

MAINTAINER  Quang Luong <luci+docker@devel.faith>

ADD build/libs/graalvm_polyglot-1.0-SNAPSHOT-fat.jar app.jar

RUN adduser -D quangio
USER quangio

CMD java -jar app.jar
