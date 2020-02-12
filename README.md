# server_practice


### 적용된 것 
1. connection pool 
2. jwt auth
3. simple web view (powered by FreeMarker engine & Bootstrap)
4. DB (postgresql)

### 서버 실행 방법 
1. postgresql 서버를 실행시킨다.
  - 해당 서버에 원하는 이름의 database를 새로 생성한다.(또는 기존것 사용)
2. 아래의 파일을 열고 
`
code .tmpServer1/src/repository/DatabaseFactory.kt
`
<br>
네번째 라인에 있는 {your db name}과 {your user name}에 위에서 생성한 database 이름과 해당 database접속한 유저name을 적고 저장
<br>
```java
private fun hikari(): HikariDataSource {
    val config = HikariConfig()
    config.driverClassName = "org.postgresql.Driver"
    config.jdbcUrl = "jdbc:postgresql:{your db name}?user={your user name}" // System.getenv("JDBC_DATABASE_URL")
    config.maximumPoolSize = 3
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    return HikariDataSource(config)
}
```
tmpServer1 디렉토리에서 실행
`
./gradlew run
`
