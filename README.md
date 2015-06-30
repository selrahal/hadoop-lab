Hadoop Tech Talk (8/21/2015)
================

[Hortonworks Sandbox For VirtualBox](http://hortonworks.com/products/hortonworks-sandbox/#install)


What is big data?
* Variety, Volume, Velocity
* structured vs unstructred data

What is hadoop? Platform for analyzing/processing/storing data.
Show picture of components
explain that everything is built on hdfs, yarn, and mapreduce

MapReduce overview
* Two basic phases
 * Map phase
 * Reduce phase

Hadoop distributed file system (HDFS) overview
* many data nodes in the cluster
* files get split up and replicated

Yet another resource manager (YARN) overview
* orchestrates tasks in hadoop

That's core hadoop!!

different projects: hive, pig, hdfs, yarn, ambari, sqoop, flume, spark, oozie

How to get data into/outof Hadoop?
webhdfs, sqoop, flume

How to analyze data in hadoop?
pig, hive, spark

solr
* search

storm
* stream processing

hbase
* nosql db

hcatalog
* stores table metadata for pig/hive

zookeeper
* cluster config

operations
* ambari


Lab time!

boot up sandbox
overview of cluster architecture
confirm cluster is started
load sample data (yelp, nyse, something cool....)
git clone java implemented map reduce job (skeleton?)
build map reduce job, implement it 

load data into hadoop using cli and webfs
create table, query it with hive
create table, transform it with pig

cover monitoring with ambari



## LAB ##
In this lab we will create a purchasing profile for our credit card customers. This will allow us to use historical data in judging the confidence value of a credit card transaction on the fly. First we will upload our data to the Hadoop cluster and perform some basic queries. Then we will create a MapReduce job using Drools to define our purchasing profile.

## Start the Hadoop Cluster ##
1.) Install [VirtualBox](https://www.virtualbox.org/wiki/Linux_Downloads)
  * You might need to run `/etc/init.d/vboxdrv setup'
    * You will need to make sure gcc is installed with `sudo yum install gcc`
    * You will need to make sure your kernel source/headers are installed with `sudo yum install kernel-devel kernel-headers`
2.) Download [Hortonworks 2.3 Sandbox For VirtualBox](http://hortonworks.com/products/hortonworks-sandbox/#install)
3.) Import Hortworks appliance into VirtualBox, default values are fine
4.) Start Hortonworks VM
5.) Navigate to http://localhost:8000 to view Hue (Web interface for Hadoop)
6.) SSH into the vm on port 2222 with user:`root` and pass:`hadoop`

## Upload the data ##
1.) Inspect the transactions.csv file to discover the format
2.) In Hue, navigate to the file manager
3.) Upload data/transactions.csv file

## Create a view for this data ##
1.) In Hue, navigate to HCatalog
2.) Select `Create a new table from a file`
  * Table Name: simple_transactions
  * Default values for columns is fine
3.) After it is done, browse the data in the table

## Stream some data with Flume ##
1.) SSH into the sandbox and install flume with `yum install flume`
2.) Inspect the flume.conf file in stream/ folder
3.) SCP the flume file into the sandbox under the folder /etc/flume/conf

```shell
scp -P 2222 stream/flume.conf root@localhost:/etc/flume/conf
```

5.) Start flume

```shell
flume-ng agent -c /etc/flume/conf -f /etc/flume/conf/flume.conf -n sandbox
```

6.) Copy the generate_transactions.py script from stream/ to the sandbox using scp:

```shell
scp -P 2222 stream/generate_transactions.py root@localhost:~
```

7.) On the sandbox, run the generate_transactions.py script:

```shell
python generate_transactions.py
```

8.) Use Hive to generate a view for this data

```SQL
CREATE TABLE TRANSACTIONS(CC STRING, city STRING, state STRING, amount DOUBLE) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LOCATION '/flume/transactions';
```

9.) Use HCatalog to browse the data

## Use Hive to find all expensive transactions ##
1.) Navigate to the Hive query editor
2.) Run the following HQL

```SQL
select * from transactions order by amount desc limit 10
```

## Use PIG to find all transactions in North Carolina ##
1.) Navigate to the PIG script editor
2.) Title the script 'NC Transactions'
3.) Using the PIG helper load the data from the transactions table

```
A = LOAD 'default.transactions' USING org.apache.hive.hcatalog.pig.HCatLoader();
```

4.) Add the `-useHCatalog` parameter at the bottom of your screen
5.) Filter the data by checking the state field (its index is 2)

```
B = FILTER A BY $2 == 'NC' ;
```

6.)  Output the result as the last step

```
DUMP B;
```

7.) Perform a syntax check (note that even the syntax check is done as a map reduce job!)
8.) Execute the query and examine the results


## Use MapReduce job to create Purchasing Profile ##



