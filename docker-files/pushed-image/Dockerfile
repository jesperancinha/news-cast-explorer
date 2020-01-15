FROM nginx

RUN nginx

RUN nginx -t

RUN apt -y update

RUN apt -y install python3-pip

RUN pip3 install flask

RUN apt -y install curl

RUN apt -y install rpm

RUN apt -y install wget

RUN wget --no-cookies --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" https://download.oracle.com/otn-pub/java/jdk/13.0.2+8/d4173c853231432d94f001e99d882ca7/jdk-13.0.2_linux-x64_bin.deb

RUN apt -y install ./jdk-13.0.2_linux-x64_bin.deb

RUN update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-13.0.2/bin/java 1

RUN apt-get -y install cron

RUN apt-get -y install vim
