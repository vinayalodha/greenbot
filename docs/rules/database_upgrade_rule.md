# database\_upgrade\_rule

1. Check if RDS instances are using latest generation of instances
2.  Suggest migration of MySQL, Postgres and Mariadb to Amazon Aurora as it offers better price to performance ration

## Permissions

* [ec2:DescribeRegions](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeRegions.html)
* [rds:DescribeDBInstances](https://docs.aws.amazon.com/cli/latest/reference/rds/describe-db-instances.html)

## Config Parameters used

* included\_tag
* excluded\_tag





