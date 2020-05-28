/*
Copyright 2020 Vinay Lodha (https://github.com/vinay-lodha)
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
provider "aws" {
  region = "us-east-2"
}

resource "aws_elastic_beanstalk_application" "beanstalk_application" {
  name = "beanstalk_application"
  description = "beanstalk_application"
  tags = {
    owner = "vinay"
  }
}

resource "aws_elastic_beanstalk_environment" "beanstalk_application_env" {
  name = "beanstalk-application"
  application = "${aws_elastic_beanstalk_application.beanstalk_application.name}"
  solution_stack_name = "64bit Amazon Linux 2 v3.0.1 running Corretto 11"

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name = "InstanceType"
    value = "t3a.small"
  }
  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name = "IamInstanceProfile"
    value = "aws-elasticbeanstalk-ec2-role"
  }

  tags = {
    owner = "vinay"
  }
}