# Getting Started

* Make sure you have JDK installed  
* Configure AWS cli using `aws configure`
  * [AWS cli guide](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-welcome.html)
* Download latest [GreenBot release](https://github.com/vinay-lodha/greenbot/releases) 
* Open terminal and execute `java -jar main.jar`, application should now be up on [`http://localhost:5000`](http://localhost:5000)\`\`

### Permissions

Below are the aggregated permissions IAM user need for execution all rules.

* [ec2:DescribeRegions](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeRegions.html)
* [ec2:DescribeInstances](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeInstances.html)
* [ec2:DescribeVolumes](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeVolumes.html)
* [ec2:DescribeImages](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeImages.html)



