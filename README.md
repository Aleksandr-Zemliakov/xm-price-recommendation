# XM Crypto Recommendation Service

### Overview

Service is intended to act as a recommendation service by providing information on cryptocurrencies prices.
Service uses CSV files as source of currencies' prices and provides following information:
* List of all the cryptos available in a service, sorted in descending order by their normalized ranges (calculated as (maximum price - minimum price) / price )
* Cryptocurrency with the highest normalized range for a specific day
* Statistics on a specific cryptocurrency by providing values of oldest recorded price, newest recorded price, historical minimum price, and historical maximum price

### Architecture overview

Service consists of two applications:
1. Loader application is responsible for loading data from CSV files 
2. Recommendation application is responsible for providing cryptocurrency information

Postgres database utilized to store currency data.

### Building the project

#### Pre-requisites

In order to set up the project you'll need:
1. Java 21+ with JAVA_HOME environment variable defined
2. Docker up and running

#### Installation procedure

The easiest way to set up the project is by invoking below command from project root folder:
```
mvnw clean install
```

Once project is built, application can be launched in Docker container by invoking following command from project root folder:
```
docker compose up
```
This creates container with Postgres database to store data, and both applications.

Loader automatically populates database with data from sample_data folder and Recommendation application ready to serve requests on default 8080 port.

Information on available endpoints is listed on [Service OpenAPI specification](http://localhost:8080/swagger-ui/index.html) page

### Room for improvement

#### Database choise
Postgres was chosen due to my familiarity with it, however, given the time-series nature of the data and analytical nature of requests - it might be beneficial to move to purpose-build time-series database like TimeStream.

#### Caching of responses
It might be beneficial to cache responses of most heavy-weight analytics queries spanning large periods of data.
Redis can be used as a distributed cache, with Recommendation application lazily caching results of queries and Loader invalidating such caches upon arrival of new data.
