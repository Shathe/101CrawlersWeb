#!/bin/bash

set -x

# append ssh public key to authorized_keys file
echo $AUTHORIZED_SSH_PUBLIC_KEY >> /home/hduser/.ssh/authorized_keys

# format the namenode if it's not already done
su -l -c 'mkdir -p /home/hduser/data/hadoop/nn /home/hduser/data/hadoop/dn && /opt/hadoop/bin/hadoop namenode -format' hduser

# start ssh daemon
service ssh start

# start zookeeper used for HDFS
service zookeeper start

# clear hadoop logs
rm -fr /opt/hadoop/logs/*

# start MAPRED
su -l -c '/opt/hadoop/bin/start-mapred.sh' hduser

# start HDFS
su -l -c '/opt/hadoop/bin/start-dfs.sh' hduser

#start HBASE
su -l -c '/opt/hbase/bin/start-hbase.sh' hduser

#start HBASE thrift
su -l -c '/opt/hbase/bin/hbase thrift start > /opt/hbase/logs/thrift.log 2>&1 &' hduser

su -l -c '$HADOOP_PREFIX/bin/hadoop-daemon.sh start historyserver --config $HADOOP_CONF_DIR' hduser

sleep 1

# tail log directory
tail -n 1000 -f /opt/hadoop/logs/*.log
