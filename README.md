# Dis_Sys_Exercise
This is an exercise for distributed systems

To create the postgres database open a database
and type this:
docker run --name ds-lab-pg --rm -e POSTGRES_PASSWORD=pass123 -e POSTGRES_USER=dbuser -e POSTGRES_DB=students -d -p 5432:5432 -v ds-lab-vol:/var/lib/postgresql/data postgres:14
