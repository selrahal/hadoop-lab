Hadoop Lab
================
In this lab we will create a purchasing profile for our credit card customers. This will allow us to use historical data in judging the confidence value of a credit card transaction on the fly. First we will upload our data to the Hadoop cluster and perform some basic queries. Then we will create a MapReduce job to calculate spending by state. Finally we will integrate Drools with our MapReduce job to provide flexibilty in our mapping and reducing logic.

## Prerequisitse ##
1.) Download [VirtualBox](https://www.virtualbox.org/wiki/Linux_Downloads)
2.) Download [Hortonworks Sandbox For VirtualBox](http://hortonworks.com/products/hortonworks-sandbox/#install)

## Start the Hadoop Cluster ##
1.) Install VirtualBox 
  * You might need to run `/etc/init.d/vboxdrv setup'
    * You will need to make sure gcc is installed with `sudo yum install gcc`
    * You will need to make sure your kernel source/headers are installed with `sudo yum install kernel-devel kernel-headers`
2.) Import Hortworks appliance into VirtualBox, default values are fine
3.) Start Hortonworks VM
4.) Navigate to http://localhost:8000 to view Hue (Web interface for Hadoop)
5.) SSH into the vm on port 2222 with user:`root` and pass:`hadoop`

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


## Use MapReduce job to Calculate Total##
1.) Import the State Total project (state-total/) into your IDE of choice
2.) Inspect the Mapper class, `StateTotalMapper`
  * Look at the class header, why are we using those generics?
  * How are the transactions mapped together?
3.) Inspect the Reducer class, `StateTotalReducer`
  * Look at the class header, why are we using those generics?
  * What is the class reducing on and to what end?
4.) Inspect the job configuration class `StateTotalJob`
  * How are the Mapper/Reducer classes specified?
5.) Inspect the pom.xml
  * How are we ensuring there will be no ClassNotFound exceptions when we run our Java code in the hadoop cluster?
6.) Build the project

```shell
mvn clean install
```

7.) Upload the jar to the sandbox 

```shell
scp -P 2222 target/state-total-1.0.0-SNAPSHOT.jar root@localhost:
```

8.) Log into the sandbox and run the MapReduce job
ssh -p 2222 root@localhost 'hadoop jar state-total-1.0.0-SNAPSHOT.jar /flume/transactions /user/hue/output'

## Integrate Drools with MapReduce ##
1.) Import the Purchasing Profile project (purchasing-profile/) into your IDE of choice
2.) Inspect the Mapper class, `DroolsMapper`
  * Look at the class header, why are we using those generics?
  * How are the transactions mapped together?
3.) Inspect the Reducer class, `DroolsReducer`
  * Look at the class header, why are we using those generics?
  * What is the class reducing on and to what end?
4.) Inspect the job configuration class `StateTotalJob`
  * How are the Mapper/Reducer classes specified?
5.) Inspect the rules used to perform the map and reduce (`mapTransaction.drl` and `reduceTransaction.drl` respectively)
6.) Inspect the pom.xml
  * How are we ensuring there will be no ClassNotFound exceptions when we run our Java code in the hadoop cluster?
7.) Build the project

```shell
mvn clean install
```

7.) Upload the jar to the sandbox

```shell
scp -P 2222 target/purchasing-profile-1.0.0-SNAPSHOT.jar root@localhost:
```

8.) Log into the sandbox and run the MapReduce job
ssh -p 2222 root@localhost 'hadoop jar purchasing-profile-1.0.0-SNAPSHOT.jar /flume/transactions /user/hue/drools'
