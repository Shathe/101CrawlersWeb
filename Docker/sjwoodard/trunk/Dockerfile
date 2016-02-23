FROM lewuathe/hadoop-base:latest
MAINTAINER S. Woodard

# Add Hadoop to PATH
ENV PATH /usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/opt/bin:/usr/local/hadoop/bin

# Download and build Nutch
RUN yum -y install git ant && \
    mkdir /opt/nutch && \
    cd /opt/nutch && \
    git clone https://github.com/apache/nutch.git && \
    mv nutch/* . && \
    ant && \
    sed s/HOSTNAME/master/ $HADOOP_PREFIX/etc/hadoop/core-site.xml.template > /usr/local/hadoop/etc/hadoop/core-site.xml && \
    sed s/RESOURCEMANAGER/master/ $HADOOP_PREFIX/etc/hadoop/yarn-site.xml.template > /usr/local/hadoop/etc/hadoop/yarn-site.xml

VOLUME ["/opt/nutch/conf", /opt/nutch/runtime/deploy"]
WORKDIR /opt/nutch