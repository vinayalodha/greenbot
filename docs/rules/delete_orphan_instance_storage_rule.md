# delete\_orphan\_instance\_storage\_rule

Orphan EBS drives are usually unused.

This rule check if there are orphan EBS drives \(EBS drives which is not attached to any EC2\).

### Permissions

* [ec2:DescribeRegions](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeRegions.html)
* [ec2:DescribeVolumes](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeVolumes.html)

