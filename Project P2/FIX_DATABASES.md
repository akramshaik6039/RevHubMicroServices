# Fix Database Issue

## Problem
Post and Follow services need databases that don't exist.

## Solution

### Option 1: Using MySQL Workbench or Command Line
```sql
CREATE DATABASE IF NOT EXISTS revhub;
CREATE DATABASE IF NOT EXISTS revhub_post;
CREATE DATABASE IF NOT EXISTS revhub_follow;
```

### Option 2: Using Command Prompt
```bash
mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS revhub; CREATE DATABASE IF NOT EXISTS revhub_post; CREATE DATABASE IF NOT EXISTS revhub_follow;"
```

### After Creating Databases
```bash
docker restart revhub-post-service revhub-follow-service
```

## Check Status
```bash
docker ps
docker logs revhub-post-service
docker logs revhub-follow-service
```
