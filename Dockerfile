FROM sunbird/openjdk-java11-alpine:latest
WORKDIR  /home
RUN apk update \
       && apk add unzip \
       && mkdir -p /home
COPY ./target/demo-hcx-1.0-SNAPSHOT-dist.zip  /home
RUN unzip /home/demo-hcx-1.0-SNAPSHOT-dist.zip -d /home
RUN rm /home/demo-hcx-1.0-SNAPSHOT-dist.zip
CMD java -cp '/home/demo-hcx-1.0-SNAPSHOT/lib/*'  -Dplay.http.secret.key='QCY?tAnfk?aZ?iwrNwnxIlR6CTf:G3gf:90Latabg@5241AB`R5W:1uDFN];Ik@n' play.core.server.ProdServerStart  /home/demo-hcx-1.0-SNAPSHOT