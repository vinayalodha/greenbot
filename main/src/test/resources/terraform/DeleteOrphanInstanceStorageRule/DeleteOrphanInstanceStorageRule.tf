provider "aws" {
  region = "us-east-2"
}

resource "aws_ebs_volume" "unattached" {
  size              = 1
  availability_zone = "us-east-2a"

  tags = {
    Name  = "unattached"
    owner = "greenbot"
  }
}

resource "aws_ebs_volume" "attached" {
  size              = 1
  availability_zone = "us-east-2a"

  tags = {
    Name  = "attached"
    owner = "greenbot"
  }
}


resource "aws_instance" "web" {
  ami               = "${data.aws_ami.ubuntu.id}"
  instance_type     = "t3.micro"
  availability_zone = "us-east-2a"

  tags = {
    Name  = "ebs_ec2"
    owner = "greenbot"
  }
}

resource "aws_volume_attachment" "ebs_att" {
  device_name = "/dev/sdh"
  volume_id   = "${aws_ebs_volume.attached.id}"
  instance_id = "${aws_instance.web.id}"
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


