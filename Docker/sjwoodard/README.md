Docker - Nutch 1.x on Hadoop
============================
Source repo: https://github.com/sjwoodard/hadoop-nutch-1x  
Docker Hub repo: https://hub.docker.com/r/sjwoodard/hadoop-nutch-1x  
Nutch 1x repo: https://github.com/apache/nutch  

Usage
=====
Nutch normally runs with it's own internal copy of Hadoop, which works fine for a single machine. This build runs on top of a Hadoop container (https://github.com/Lewuathe/docker-hadoop-cluster) and makes it easier to set up your own distributed Nutch cluster. You can mount your Hadoop configuration volume to `/usr/local/hadoop/etc/hadoop` in the container to point to your cluster.

You need to mount your Nutch `conf` directory when running the container. After running the container you can edit the configuration to setup Nutch -- at a minimum you need to rename `nutch-site.xml.template` to `nutch-site.xml` and adding the following inside the `<configuration>` element.

```
<property>
  <name>http.agent.name</name>
  <value>NutchAgent</value>
</property>
```

__Important note__ The `conf` directory is rolled into the Hadoop jar so you need to rebuild Nutch using `ant` in order to include any changes your configuration. You must rebuild Nutch this after every change to regenerate the Hadoop jar. These containers are large because they contain all the components to build Nutch, which speeds up build times.

Example
=======
___Prepare___  
Get a copy of the Nutch `conf` directory from https://github.com/apache/nutch/tree/trunk/conf, customize it, and save it to ~/nutch/conf on you host. The container will automatically build your Hadoop job using the mounted `conf` directory. In this example, the job will be saved to `~/nutch/job` on your host and then we'll mount it to subsequent containers and send it to YARN for execution.

___Create a Hadoop Cluster___  
Note: Do not change the host name from `master` as this is hardcoded into the underlying Hadoop container.
```
docker network create hadoop-network
docker run -d -p 50070:50070 -p 8088:8088 --net hadoop-network --name master -h master lewuathe/hadoop-master
docker run -d --name slave1 -h slave1 --net hadoop-network lewuathe/hadoop-slave
```

___Execute Crawl on YARN___
```
NUTCH_REPO=sjwoodard/hadoop-nutch-1x:latest
docker run --rm -v ~/nutch/conf:/opt/nutch/conf -v ~/nutch/job:/opt/nutch/runtime/deploy ${NUTCH_REPO} ant
docker run --rm --net hadoop-network -ti ${NUTCH_REPO} sh -c "mkdir urls && echo 'http://nutch.apache.org/' > urls/seed.txt && /usr/local/hadoop/bin/hadoop fs -mkdir -p /nutch && /usr/local/hadoop/bin/hadoop fs -put urls /nutch"
docker run --rm -v ~/nutch/job:/opt/nutch/runtime/deploy ${NUTCH_REPO} runtime/deploy/bin/crawl /nutch/urls /nutch/crawldb 3
```