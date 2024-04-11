FROM gradle:8.6.0-jdk20

WORKDIR /

COPY / .

RUN gradle installDist

CMD ./build/install/app/bin/app