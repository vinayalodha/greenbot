# auto\_scaling\_group\_optimization\_rule

1. Check if MixedInstancePolicy is used or not
2. Check if LB attached to ALB is needed or not
3. Suggest migration to SpotFleet

## Permissions

* [ec2:DescribeRegions](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeRegions.html)
* [autoscaling:DescribeAutoScalingGroups](https://docs.aws.amazon.com/cli/latest/reference/autoscaling/describe-auto-scaling-groups.html)

## Config Parameters used

* included\_tag
* excluded\_tag



