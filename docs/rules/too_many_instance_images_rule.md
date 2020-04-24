# too\_many\_instance\_images\_rule

Amazon Machine Images \(AMI\) incurs monthly cost and should be cleaned if not used. 

This rule checks if there are too many AMI present. 

### Permissions

* [ec2:DescribeRegions](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeRegions.html)
* [ec2:DescribeImages](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeImages.html)

