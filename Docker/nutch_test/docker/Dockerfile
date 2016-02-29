# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

FROM ubuntu:14.04
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


# Download Hadoop
RUN wget -q 'https://archive.apache.org/dist/hadoop/core/hadoop-2.5.1/hadoop-2.5.1.tar.gz'
RUN wget -q 'http://archive.apache.org/dist/hbase/hbase-0.98.8/hbase-0.98.8-hadoop2-bin.tar.gz'
RUN wget -q 'https://protobuf.googlecode.com/files/protobuf-2.5.0.tar.gz'


# Setup SSH keys for passwordless access
RUN su -l -c 'ssh-keygen -t rsa -f /home/hduser/.ssh/id_rsa -P ""' hduser && \
  cat /home/hduser/.ssh/id_rsa.pub | su -l -c 'tee -a /home/hduser/.ssh/authorized_keys' hduser
ADD config/ssh-config /home/hduser/.ssh/config
RUN chmod 600 /home/hduser/.ssh/config
RUN chown hduser /home/hduser/.ssh/config

# Fix Ubuntu 13.10 SSH daemon problem with docker: http://docs.docker.io/en/latest/examples/running_ssh_service/
RUN sed -ri 's/session[[:blank:]]+required[[:blank:]]+pam_loginuid.so/session optional pam_loginuid.so/g' /etc/pam.d/sshd

# Deploy and setup file permissions
RUN tar xvfz /root/hadoop-2.5.1.tar.gz -C /opt && \
  ln -s /opt/hadoop-2.5.1 /opt/hadoop && \
  chown -R root:root /opt/hadoop-2.5.1 && \
  mkdir /opt/hadoop-2.5.1/logs && \
  chown -R hduser:hadoop /opt/hadoop-2.5.1/logs

# Unpack and compile Google protobuf
RUN tar xvfz /root/protobuf-2.5.0.tar.gz -C /opt && \
  chown -R root:root /opt/protobuf-2.5.0
RUN cd /opt/protobuf-2.5.0 && ./configure && make && make check && make install

# Deploy and setup file permissions
RUN tar xvfz /root/hbase-0.98.8-hadoop2-bin.tar.gz -C /opt && \
  chown -R root:root /opt/hbase-0.98.8-hadoop2

# link binaries, create logs directory
RUN ln -s /opt/hbase-0.98.8-hadoop2 /opt/hbase &&  mkdir /opt/hbase/logs && \
  chown -R hduser:hadoop /opt/hbase/logs

 # Deploy and setup file permissions
RUN mv /root/nutch-sources /opt/apache-nutch-1.9 && \
  chown -R root:root /opt/apache-nutch-1.9 && \
  mkdir /opt/apache-nutch-1.9/logs && \
  chown -R hduser:hadoop /opt/apache-nutch-1.9/logs

# Setup hduser environment
ADD config/bashrc /home/hduser/.bashrc
RUN chmod +x /home/hduser/.bashrc




# RUN svn checkout https://svn.apache.org/repos/asf/nutch/trunk/ nutch_source && cd nutch_source && ant

# Convenience symlink to Nutch runtime local
RUN ln -s nutch_source/runtime/local $HOME/nutch


# Set up JAVA_HOME
RUN echo 'export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")' >> $HOME/.bashrc


RUN JAVA_HOME=/usr/lib/jvm/java-7-oracle
RUN export JAVA_HOME
RUN PATH=$PATH:$HOME
RUN echo PATH=$PATH:$HOME >> /etc/environment

RUN git clone https://github.com/Shathe/nutchCrawlers.git crawler && cd crawler
# Checkout and build the nutch trunk


#default seed
ARG seed='http://www.google.es'
RUN echo $seed >> crawler/urls/seeds.txt


ENV JAVA_HOME /usr/lib/jvm/java-7-oracle
