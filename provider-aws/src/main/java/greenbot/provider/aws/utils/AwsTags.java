package greenbot.provider.aws.utils;

public interface AwsTags {
    public static final String ASG_NAME = "aws:autoscaling:groupName";
    public static final String ELASTIC_BEANSTALK_APP_NAME = "elasticbeanstalk:environment-name";
    public static final String FLEET_ID = "aws:ec2spot:fleet-request-id";
}
