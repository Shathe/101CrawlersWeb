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

from ubuntu:14.04
MAINTAINER Nutch Developers <dev@nutch.apache.org>

WORKDIR /root/

# Install package with add-apt-repository
RUN apt-get update && apt-get install -y software-properties-common

# Enable Ubuntu repositories
RUN add-apt-repository -y multiverse && \
  add-apt-repository -y restricted && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && apt-get upgrade -y

# Install latest Oracle Java from PPA
RUN echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
  apt-get install -y oracle-java7-installer oracle-java7-set-default

# Install dependencies
RUN apt-get install -y ant openssh-server zookeeperd vim telnet subversion rsync curl build-essential maven


# Download Hadoop
RUN wget -q 'http://archive.apache.org/dist/hadoop/core/hadoop-0.20.204.0/hadoop-0.20.204.0-bin.tar.gz'
RUN wget -q 'http://archive.apache.org/dist/hbase/hbase-0.98.8/hbase-0.98.8-src.tar.gz'
RUN svn checkout http://svn.apache.org/repos/asf/nutch/branches/branch-1.9/ nutch-sources
# Install util packages
RUN apt-get install -y rsync curl

# Setup system user and group to own and run Hadoop
RUN addgroup hadoop && adduser --ingroup hadoop hduser
RUN usermod -a -G hadoop zookeeper

# Setup SSH keys for Hadoop
RUN su -l -c 'ssh-keygen -t rsa -f /home/hduser/.ssh/id_rsa -P ""' hduser && \
  cat /home/hduser/.ssh/id_rsa.pub | su -l -c 'tee -a /home/hduser/.ssh/authorized_keys' hduser
ADD config/ssh-config /home/hduser/.ssh/config
RUN chmod 600 /home/hduser/.ssh/config
RUN chown hduser /home/hduser/.ssh/config

# Fix Ubuntu 13.10 SSH daemon problem with docker: http://docs.docker.io/en/latest/examples/running_ssh_service/
RUN sed -ri 's/session[[:blank:]]+required[[:blank:]]+pam_loginuid.so/session optional pam_loginuid.so/g' /etc/pam.d/sshd

# Deploy and setup file permissions
RUN tar xvfz /root/hadoop-0.20.204.0-bin.tar.gz -C /opt && \
  ln -s /opt/hadoop-0.20.204.0 /opt/hadoop && \
  chown -R root:root /opt/hadoop-0.20.204.0 && \
  mkdir /opt/hadoop-0.20.204.0/logs && \
  chown -R hduser:hadoop /opt/hadoop-0.20.204.0/logs

# Deploy and setup file permissions
RUN tar xvfz /root/hbase-0.98.8-src.tar.gz -C /opt && \
  ln -s /opt/hbase-0.98.8 /opt/hbase && \
  chown -R root:root /opt/hbase-0.98.8 && \
  mkdir /opt/hbase-0.98.8/logs && \
  chown -R hduser:hadoop /opt/hbase-0.98.8/logs

 # Deploy and setup file permissions
RUN tar xvfz /root/apache-nutch-2.2.1-src.tar.gz -C /opt && \
  chown -R root:root /opt/apache-nutch-2.2.1 && \
  mkdir /opt/apache-nutch-2.2.1/logs && \
  chown -R hduser:hadoop /opt/apache-nutch-2.2.1/logs

# Setup hduser environment
ADD config/bashrc /home/hduser/.bashrc

# Configure Hadoo
ADD config/mapred-site.xml /tmp/hadoop-etc/mapred-site.xml
ADD config/core-site.xml /tmp/hadoop-etc/core-site.xml
ADD config/hdfs-site.xml /tmp/hadoop-etc/hdfs-site.xml
ADD config/nutch-site.xml /tmp/nutch-etc/nutch-site.xml
ADD config/hbase-site.xml /tmp/hbase-etc/hbase-site.xml
RUN cp /tmp/hadoop-etc/* /opt/hadoop/conf/
RUN mv /tmp/hadoop-etc/* /opt/hadoop/etc/hadoop
RUN mv /tmp/hbase-etc/* /opt/hbase/conf/
RUN mv /tmp/nutch-etc/* /opt/apache-nutch-2.2.1/conf/


ENV NUTCH_ROOT /opt/apache-nutch-2.2.1
RUN echo 'gora.datastore.default=org.apache.gora.hbase.store.HBaseStore' >> /opt/apache-nutch-2.2.1/conf/gora.properties
RUN vim -c 'g/name="gora-hbase"/+1d' -c 'x' $NUTCH_ROOT/ivy/ivy.xml
RUN vim -c 'g/name="gora-hbase"/-1d' -c 'x' $NUTCH_ROOT/ivy/ivy.xml
RUN cd $NUTCH_ROOT && ant runtime
RUN ln -s /opt/apache-nutch-2.2.1/runtime/local /opt/nutch
ENV NUTCH_HOME /opt/nutch
ENV HADOOP_HOME /opt/hadoop
ENV NUTCHSERVER_PORT 8899

# NUTCH
EXPOSE 8899
EXPOSE 9000

# Expose SSHD
EXPOSE 22

# QuorumPeerMain (Zookeeper)
EXPOSE 2181 39534

# NameNode (HDFS)
EXPOSE 8020 50070 9000

# DataNode (HDFS)
EXPOSE 50010 50020 50075

# SecondaryNameNode (HDFS)
EXPOSE 50090

# Trackers
EXPOSE 50030 50060

#HBASE
EXPOSE 6000 60010 60020 60030

# Thrift
EXPOSE 9090 9095


# Create start script
ADD config/run-hadoop.sh /root/run-hadoop.sh
RUN chmod +x /root/run-hadoop.sh

CMD ["/root/run-hadoop.sh"]

RUN JAVA_HOME=/usr/lib/jvm/java-7-oracle
RUN export JAVA_HOME
RUN PATH=$PATH:$HOME
RUN echo PATH=$PATH:$HOME >> /etc/environment

ENV JAVA_HOME /usr/lib/jvm/java-7-oracle
ENV PATH $PATH:$JAVA_HOME/bin
