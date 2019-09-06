# druid-monitor-detector
Druid Monitor Detector to analysis druid monitor API data and send monitoring notification

## Function
- Monitor & Detect Project Bad Method excution health
- Monitor & Detect Project Bad SQL execution health

## Strategy
- Base on execute error count, execute average time, execute time too long!

## TODO
- Monitor & Detect Project URI execution

## Step to run:
### build and start
```
1. git clone https://github.com/Jawf/druid-monitor-detector.git
2. mvn clean package
3. mvn spring-boot:run
```
### use druid-monitor ui
```
access to http://localhost:8080/druid
```
### prepare data:
1. Please according to the controller to prepare data for testing
2. GET http://localhost:8080/user/person?email=xxx&firstName=test  to create user
3. GET http://localhost:8080/users  to search user

## Integrate to your project
1. Copy package: **config, component, bean, schedule** to your project
2. Able to implement **send notication method** to monitor system heath.
3. Ready to work!

### Normally runing log
- Shedule Run Normally Log:
```
2018-12-20 23:48:01.002 DEBUG 39876 --- [PoolScheduler10] c.d.m.d.c.MonitorDetectorComponent       : monitorAndDetectBadMethodExcution-execute...
2018-12-20 23:48:01.004 DEBUG 39876 --- [PoolScheduler10] c.d.m.d.c.MonitorDetectorComponent       : monitorAndDetectBadMethodExcution-executed query and result process.1..
2018-12-20 23:48:01.005 DEBUG 39876 --- [PoolScheduler10] c.d.m.d.c.MonitorDetectorComponent       : monitorAndDetectBadMethodExcution-executed query and result process.2..
2018-12-20 23:48:01.006 DEBUG 39876 --- [PoolScheduler10] c.d.m.d.c.MonitorDetectorComponent       : monitorAndDetectBadMethodExcution-executed query and result process.3..
2018-12-20 23:48:01.006 DEBUG 39876 --- [PoolScheduler10] c.d.m.d.c.MonitorDetectorComponent       : monitorAndDetectBadMethodExcution-executed query and result process.4..
2018-12-20 23:48:01.007 DEBUG 39876 --- [PoolScheduler10] c.d.m.d.c.MonitorDetectorComponent       : monitorAndDetectBadMethodExcution-executed query and result process.5..
2018-12-20 23:48:10.001 DEBUG 39876 --- [tPoolScheduler3] c.d.m.d.c.MonitorDetectorComponent       : monitorAndDetectBadSqlExcution-execute...
2018-12-20 23:48:10.004 DEBUG 39876 --- [tPoolScheduler3] c.d.m.d.c.MonitorDetectorComponent       : monitorAndDetectBadSqlExcution-executed query and result process.1..
2018-12-20 23:48:10.010 DEBUG 39876 --- [tPoolScheduler3] c.d.m.d.c.MonitorDetectorComponent       : monitorAndDetectBadSqlExcution-executed query and result process.2..
2018-12-20 23:48:10.012 DEBUG 39876 --- [tPoolScheduler3] c.d.m.d.c.MonitorDetectorComponent       : monitorAndDetectBadSqlExcution-executed query and result process.3..
2018-12-20 23:48:10.014 DEBUG 39876 --- [tPoolScheduler3] c.d.m.d.c.MonitorDetectorComponent       : monitorAndDetectBadSqlExcution-executed query and result process.4..
2018-12-20 23:48:10.016 DEBUG 39876 --- [tPoolScheduler3] c.d.m.d.c.MonitorDetectorComponent       : monitorAndDetectBadSqlExcution-executed query and result process.5..

```

### based on druid-monitor
![Ruid-Monitor SQL Stat](https://github.com/Jawf/druid-monitor-detector/blob/master/src/main/resources/static/sql-stat-monitor.png)
![Ruid-Monitor Spring Method Stat](https://github.com/Jawf/druid-monitor-detector/blob/master/src/main/resources/static/spring-method-monitor.png)
![Ruid-Monitor Request URI Stat](https://github.com/Jawf/druid-monitor-detector/blob/master/src/main/resources/static/URI-stat-monitor.png)

### Mail Notification Content
![Method Notification Content Screenshot](https://github.com/Jawf/druid-monitor-detector/blob/master/src/main/resources/static/SuspectedMethodNotification.jpg)
