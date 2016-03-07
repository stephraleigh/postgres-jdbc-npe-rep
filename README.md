# postgres-jdbc-npe-rep
Code needed to reproduce NullPointerException in Postgres JDBC driver

Update `PostgresNPE` to specify the correct JDBC URL, username and password. Then execute `gradlew run` to reproduce the NullPointerException.
Reproduced using version 9.4.1208 of the Postgres JDBC driver.
