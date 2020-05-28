# instance\_upgrade\_rule

Newer generation of EC2 are cheaper and offers better price to performance ration, e.g **t3a** instances are around 15% cheaper than **t2** instances

1. This rule checks if older generation of EC2 instances are being used.
2. Can AWS inf1 chips be used for machine learning inference
3. Can ARM instance types be used instead of x86 chips

## Permissions

* [ec2:DescribeRegions](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeRegions.html)
* [ec2:DescribeInstances](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeInstances.html)

## Config Parameters used

* included\_tag
* excluded\_tag





