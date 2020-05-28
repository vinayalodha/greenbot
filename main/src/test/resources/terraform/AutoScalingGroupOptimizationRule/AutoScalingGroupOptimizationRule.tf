/*
 * Copyright 2020 Vinay Lodha (https://github.com/vinay-lodha)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
provider "aws" {
  region = "us-east-2"
}


data "aws_ami" "ubuntu" {
  most_recent = true

  filter {
    name = "name"
    values = [
      "ubuntu/images/hvm-ssd/ubuntu-trusty-14.04-amd64-server-*"]
  }

  filter {
    name = "virtualization-type"
    values = [
      "hvm"]
  }

  owners = [
    "099720109477"]
  # Canonical
}

resource "aws_launch_template" "launch-template" {
  name_prefix = "foobar"
  image_id = "${data.aws_ami.ubuntu.id}"
  instance_type = "t3a.nano"
}

resource "aws_autoscaling_group" "bar" {
  availability_zones = [
    "us-east-2a"]
  desired_capacity = 1
  max_size = 1
  min_size = 1

  launch_template {
    id = "${aws_launch_template.launch-template.id}"
    version = "$Latest"
  }
  tags = [
    {
      key = "owner"
      value = "vinay"
      propagate_at_launch = true
    },
    {
      key = "count"
      value = "2"
      propagate_at_launch = false
    }
  ]
}


resource "aws_autoscaling_group" "example" {
  availability_zones = [
    "us-east-2a"]
  desired_capacity = 2
  max_size = 5
  min_size = 2

  mixed_instances_policy {
    launch_template {
      launch_template_specification {
        launch_template_id = "${aws_launch_template.launch-template.id}"
      }
    }
    instances_distribution {
      on_demand_base_capacity = 0
      on_demand_percentage_above_base_capacity = 0
    }
  }
  tags = [
    {
      key = "owner"
      value = "vinay"
      propagate_at_launch = true
    },
    {
      key = "count"
      value = "1"
      propagate_at_launch = false
    }
  ]
}
