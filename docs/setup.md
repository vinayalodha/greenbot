# Getting Started

* Make sure you have JDK 8 installed \(AWS SDK is incompatible with JDK 9 and above\)
  * Oracle JDK 8 download [link](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html) or
  * AWS corretto JDK 8 download [link](https://docs.aws.amazon.com/corretto/latest/corretto-8-ug/downloads-list.html), installation [guide](https://docs.aws.amazon.com/corretto/latest/corretto-8-ug/windows-7-install.html)
* Install and configure[ AWS CLI ](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-welcome.html)using `aws configure`
* Download **main.jar** from [GreenBot release](https://github.com/vinay-lodha/greenbot/releases)
* Open terminal and execute `java -jar main.jar`, application should now be up on [`http://localhost:5000`](http://localhost:5000)

## Permissions

Below are the aggregated permissions IAM user need for execution all rules.

* [ec2:DescribeRegions](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeRegions.html)
* [ec2:DescribeInstances](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeInstances.html)
* [ec2:DescribeVolumes](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeVolumes.html)
* [ec2:DescribeImages](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeImages.html)
* [cloudwatch:GetMetricStatistics](https://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_GetMetricStatistics.html)
* [rds:DescribeDBInstances](https://docs.aws.amazon.com/cli/latest/reference/rds/describe-db-instances.html)
* [elasticache:DescribeCacheClusters](https://docs.aws.amazon.com/cli/latest/reference/elasticache/describe-cache-clusters.html)
* [autoscaling:DescribeAutoScalingGroups](https://docs.aws.amazon.com/cli/latest/reference/autoscaling/describe-auto-scaling-groups.html)

## Screenshot

![](.gitbook/assets/screenshot_1%20%281%29.png)

