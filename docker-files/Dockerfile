FROM nginx

RUN mv /etc/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf.disabled

RUN nginx

RUN nginx -t

RUN apt -y update

RUN apt -y install python3-pip

RUN pip3 install flask

RUN apt -y install curl

RUN apt -y install rpm

RUN apt -y install wget

RUN wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" https://download.oracle.com/otn-pub/java/jdk/12.0.2+10/e482c34c86bd4bf8b56c0b35558996b9/jdk-12.0.2_linux-x64_bin.deb

RUN apt -y install ./jdk-12.0.2_linux-x64_bin.deb

RUN update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-12.0.2/bin/java 1

RUN apt-get -y install cron

RUN apt-get -y install vim
