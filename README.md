# Distributed Systems Project

**For Windows OS, to create the postgres database run this:**

docker run --name ds-lab-pg --rm -e POSTGRES_PASSWORD=pass123 -e POSTGRES_USER=dbuser -e POSTGRES_DB=students -d -p 5432:5432 -v ds-lab-vol:/var/lib/postgresql/data postgres:14

**For linux OS:**
```bash
docker run --name ds-lab-pg --rm \
-e POSTGRES_PASSWORD=pass123 \
-e POSTGRES_USER=dbuser \
-e POSTGRES_DB=students \
-d --net=host \
-v ds-lab-vol:/var/lib/postgresql/data \
postgres:14
```