
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

resource "aws_instance" "one" {
  ami           = "${data.aws_ami.ubuntu.id}"
  instance_type = "t2.micro"

  tags = {
    owner = "greenbot"
  }
}

resource "aws_instance" "two" {
  ami           = "${data.aws_ami.ubuntu.id}"
  instance_type = "t3.micro"

  tags = {
    owner = "greenbot"
  }
}


resource "aws_instance" "three" {
  ami           = "${data.aws_ami.ubuntu.id}"
  instance_type = "t3.micro"

  tags = {
    owner = "greenbot"
  }
}


resource "aws_instance" "four" {
  ami           = "${data.aws_ami.ubuntu.id}"
  instance_type = "t3a.micro"

  tags = {
    owner = "greenbot"
  }
}
