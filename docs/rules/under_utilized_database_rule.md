# under\_utilized\_database\_rule

This rule checks if average CPU utilization of RDS is below 10%, analysis window is controlled by **cloudwatch\_config\_duration** config param \(default is 1 week\)

## Permissions

* [ec2:DescribeRegions](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeRegions.html)
* [rds:DescribeDBInstances](https://docs.aws.amazon.com/cli/latest/reference/rds/describe-db-instances.html)
* [cloudwatch:GetMetricStatistics](https://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_GetMetricStatistics.html)



