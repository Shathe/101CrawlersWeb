From ubuntu:14.04
MAINTAINER inigo alonso <inigol22zgz@gmail.com>

WORKDIR /root/

# Get the package containing apt-add-repository installed for adding repositories
RUN apt-get update && apt-get install -y software-properties-common

# Add the repository that we'll pull java down from.
RUN add-apt-repository -y ppa:webupd8team/java && apt-get update && apt-get upgrade -y
# Get Oracle Java 1.7 installed
RUN echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && apt-get install -y oracle-java7-installer oracle-java7-set-default

# Install various dependencies
RUN apt-get install -y ant openssh-server vim telnet subversion rsync curl build-essential git

# Convenience symlink to Nutch runtime local
RUN ln -s nutch_source/runtime/local $HOME/nutch

# Set up JAVA_HOME
RUN echo 'export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")' >> $HOME/.bashrc

RUN JAVA_HOME=/usr/lib/jvm/java-7-oracle
RUN export JAVA_HOME
RUN PATH=$PATH:$HOME
RUN echo PATH=$PATH:$HOME >> /etc/environment

#Para conseguir que pueda configurarse la versión de nutch tendria que hacerse con
#RUN svn checkout https://svn.apache.org/repos/asf/nutch/trunk/ nutch_source && cd nutch_source && ant para la ultima version disponible o
#RUN svn checkout http://svn.apache.org/repos/asf/nutch/branches/branch-1.9/ nutch_source && cd nutch_source && ant
#Siendo 1.9 la version que eliges, después tan solo habría que ejecutar esto
#RUN ln -s nutch_source/runtime/local $HOME/crawler
#También comentar la intruccion de descarga de github y crear estas carpetas
#Run mkdir crawler/urls
#Run mkdir crawler/salida
#Run mkdir crawler/micrawl
#Run mkdir crawler/micrawl/segments
#Run mkdir crawler/micrawl/linkbd
#Run mkdir crawler/micrawl/crawlbd

RUN git clone https://github.com/Shathe/nutchCrawlers.git crawler && cd crawler
# Checkout and build the nutch trunk

ENV JAVA_HOME /usr/lib/jvm/java-7-oracle
RUN echo https://dzone.com/articles/using-yaml-java-application >> crawler/urls/seeds.txt
RUN echo https://dzone.com/articles/ >> crawler/urls/seeds.txt
ADD src/test/resources/es/unizar/iaaa/crawler/butler/builders//home/shathe/Documents/workspaceSpring/Crawlers101/butler/src/test/resources/es/unizar/iaaa/crawler/butler/builders/juntarSalidas.sh crawler/juntarSalidas.sh
ADD src/test/resources/es/unizar/iaaa/crawler/butler/builders//home/shathe/Documents/workspaceSpring/Crawlers101/butler/src/test/resources/es/unizar/iaaa/crawler/butler/builders/run.sh crawler/run.sh
ADD /home/shathe/Documents/workspaceSpring/Crawlers101/butler/src/test/resources/es/unizar/iaaa/crawler/butler/builders/nutch-site.xml crawler/conf/nutch-site.xml

Run mkdir crawler/plugins/protocol-file
ADD /home/shathe/Documents/workspaceSpring/Crawlers101/butler/src/test/resources/es/unizar/iaaa/crawler/butler/builders/protocol-file.xml crawler/plugins/protocol-file/plugin.xml
ADD /home/shathe/Documents/workspaceSpring/Crawlers101/butler/src/test/resources/es/unizar/iaaa/crawler/butler/builders/protocol-file.jar crawler/plugins/protocol-file/protocol-file.jar
Run mkdir crawler/plugins/protocol-file2
ADD /home/shathe/Documents/workspaceSpring/Crawlers101/butler/src/test/resources/es/unizar/iaaa/crawler/butler/builders/protocol-file2.xml crawler/plugins/protocol-file2/plugin.xml
ADD /home/shathe/Documents/workspaceSpring/Crawlers101/butler/src/test/resources/es/unizar/iaaa/crawler/butler/builders/protocol-file2.jar crawler/plugins/protocol-file2/protocol-file2.jar
