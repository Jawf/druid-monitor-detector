# druid-monitor-detector
Druid Monitor Detector to analysis druid monitor API data for monitor notification

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
git clone https://github.com/Jawf/druid-monitor-detector.git
mvn clean package
mvn spring-boot:run
```
### use druid-monitor ui
```
access to http://localhost:8080/druid
```
### prepare data:
- Please according to the controller to prepare data for testing
- GET http://localhost:8080/user/person?email=xxx&firstName=test  to create user
- GET http://localhost:8080/users  to search user

## Integrate to your project
- Copy package: config, component, bean, schedule to your project
- Able to implement send notication method to monitor system heath.
- Ready to work!

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

