[![CircleCI](https://circleci.com/gh/artshishkin/art-sfg-recipe-project.svg?style=svg)](https://circleci.com/gh/artshishkin/art-sfg-recipe-project)
[![codecov](https://codecov.io/gh/artshishkin/art-sfg-recipe-project/branch/master/graph/badge.svg)](https://codecov.io/gh/artshishkin/art-sfg-recipe-project)
# art-sfg-recipe-project
Recipe Project for SFG Tutorial on Spring Boot

## 107 Axis TCPMon

## 180 Continuous Integration Testing with Circle CI

## `311` Schema Generation with Hibernate
- we can generate schema using metadata by configuring properties application-dev.yml
- add ";" at the end of all lines and execute in workbench
- `dev, db_remote` - we can set active profiles in IDE Run Configuration

## `312` Refactor Database Initialization for MySQL
- add default profile properties
- `spring.datasource.platform=h2` - Platform to use in the DDL or DML scripts (such as schema-${platform}.sql or data-${platform}.sql).
- rename `data.sql` to `data-h2.sql` to load data only on h2 platform

