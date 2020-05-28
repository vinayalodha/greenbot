package greenbot.provider.aws.utils;

public interface AwsTags {
    String ASG_NAME = "aws:autoscaling:groupName";
    String ELASTIC_BEANSTALK_APP_NAME = "elasticbeanstalk:environment-name";
    String FLEET_ID = "aws:ec2spot:fleet-request-id";
}
