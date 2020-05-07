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

data "aws_ami" "ubuntu" {
  most_recent = true

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-trusty-14.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = ["099720109477"] # Canonical
}

resource "aws_instance" "owner_greenbot_stressed_small" {
  ami           = "${data.aws_ami.ubuntu.id}"
  instance_type = "t3a.micro"
  user_data = "${file("stress_cpu.sh")}"

  tags = {
    owner = "greenbot"
    Name = "owner_greenbot_stressed_small"
  }
}

resource "aws_instance" "owner_greenbot_stressed_large" {
  ami           = "${data.aws_ami.ubuntu.id}"
  instance_type = "t3a.medium"
  user_data = "${file("stress_cpu.sh")}"

  tags = {
    owner = "greenbot"
    Name = "owner_greenbot_stressed_large"
  }
}

resource "aws_instance" "owner_greenbot_under_utilized_large" {
  ami           = "${data.aws_ami.ubuntu.id}"
  instance_type = "t3a.medium"

  tags = {
    owner = "greenbot"
    Name = "owner_greenbot_under_utilized_large"
  }
}