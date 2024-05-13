# Distributed Systems Project

**To create the postgres database run this:**

```bash
docker run --name ds-lab-pg --rm -e POSTGRES_PASSWORD=pass123 -e POSTGRES_USER=dbuser -e POSTGRES_DB=students -d -p 5432:5432 -v ds-lab-vol:/var/lib/postgresql/data postgres:14
```

**To run the project:**
```bash
mvm clean
```
```bash
mvn package
```
```bash
java -jar target/BusinessManagement-0.0.1-SNAPSHOT.jar
```



