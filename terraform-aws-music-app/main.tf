data "aws_availability_zones" "available" {
  state = "available"
}

data "aws_ami" "amazon_linux_2023" {
  most_recent = true
  owners      = ["137112412989"]

  filter {
    name   = "name"
    values = ["al2023-ami-2023*-x86_64"]
  }

  filter {
    name   = "architecture"
    values = ["x86_64"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

locals {
  az1 = data.aws_availability_zones.available.names[0]
  az2 = data.aws_availability_zones.available.names[1]

  common_tags = {
    Project = var.project_name
  }
}

resource "aws_vpc" "music_app_vpc" {
  cidr_block           = var.vpc_cidr
  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = merge(local.common_tags, {
    Name = "vpc-music-app"
  })
}

resource "aws_internet_gateway" "music_app_igw" {
  vpc_id = aws_vpc.music_app_vpc.id

  tags = merge(local.common_tags, {
    Name = "igw-music-app"
  })
}

resource "aws_subnet" "public_subnet_1" {
  vpc_id                  = aws_vpc.music_app_vpc.id
  cidr_block              = var.public_subnet_1_cidr
  availability_zone       = local.az1
  map_public_ip_on_launch = true

  tags = merge(local.common_tags, {
    Name = "public-subnet-1-music-app"
  })
}

resource "aws_subnet" "public_subnet_2" {
  vpc_id                  = aws_vpc.music_app_vpc.id
  cidr_block              = var.public_subnet_2_cidr
  availability_zone       = local.az2
  map_public_ip_on_launch = true

  tags = merge(local.common_tags, {
    Name = "public-subnet-2-music-app"
  })
}

resource "aws_subnet" "private_subnet_1" {
  vpc_id            = aws_vpc.music_app_vpc.id
  cidr_block        = var.private_subnet_1_cidr
  availability_zone = local.az1

  tags = merge(local.common_tags, {
    Name = "private-subnet-1-mysql"
  })
}

resource "aws_subnet" "private_subnet_2" {
  vpc_id            = aws_vpc.music_app_vpc.id
  cidr_block        = var.private_subnet_2_cidr
  availability_zone = local.az2

  tags = merge(local.common_tags, {
    Name = "private-subnet-2-mysql"
  })
}

resource "aws_eip" "nat_eip" {
  domain = "vpc"

  tags = merge(local.common_tags, {
    Name = "eip-nat-music-app"
  })
}

resource "aws_nat_gateway" "music_app_nat" {
  allocation_id = aws_eip.nat_eip.id
  subnet_id     = aws_subnet.public_subnet_1.id

  tags = merge(local.common_tags, {
    Name = "nat-music-app"
  })

  depends_on = [aws_internet_gateway.music_app_igw]
}

resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.music_app_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.music_app_igw.id
  }

  tags = merge(local.common_tags, {
    Name = "rt-public-music-app"
  })
}

resource "aws_route_table_association" "public_assoc_1" {
  subnet_id      = aws_subnet.public_subnet_1.id
  route_table_id = aws_route_table.public_rt.id
}

resource "aws_route_table_association" "public_assoc_2" {
  subnet_id      = aws_subnet.public_subnet_2.id
  route_table_id = aws_route_table.public_rt.id
}

resource "aws_route_table" "private_rt" {
  vpc_id = aws_vpc.music_app_vpc.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.music_app_nat.id
  }

  tags = merge(local.common_tags, {
    Name = "rt-private-mysql"
  })
}

resource "aws_route_table_association" "private_assoc_1" {
  subnet_id      = aws_subnet.private_subnet_1.id
  route_table_id = aws_route_table.private_rt.id
}

resource "aws_route_table_association" "private_assoc_2" {
  subnet_id      = aws_subnet.private_subnet_2.id
  route_table_id = aws_route_table.private_rt.id
}

resource "aws_security_group" "sg_serveur_musique_app" {
  name        = "sgrp-musique-app"
  description = "Security group for EC2 serveur-musique-app"
  vpc_id      = aws_vpc.music_app_vpc.id

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.allowed_ssh_cidr]
  }

  ingress {
    description = "HTTP app"
    from_port   = var.app_port
    to_port     = var.app_port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "All outbound"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(local.common_tags, {
    Name = "sgrp-musique-ap"
  })
}

resource "aws_security_group" "sg_mysql_rds" {
  name        = "sgrp-mysql-rds"
  description = "Security group for RDS MySQL"
  vpc_id      = aws_vpc.music_app_vpc.id

  ingress {
    description     = "MySQL from EC2 app server"
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.sg_serveur_musique_app.id]
  }

  egress {
    description = "All outbound"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(local.common_tags, {
    Name = "sg-mysql-rds"
  })
}

resource "aws_db_subnet_group" "mysql_subnet_group" {
  name       = "mysql-subnet-group"
  subnet_ids = [aws_subnet.private_subnet_1.id, aws_subnet.private_subnet_2.id]

  tags = merge(local.common_tags, {
    Name = "mysql-subnet-group"
  })
}

resource "aws_db_instance" "mysql" {
  identifier             = "mysql"
  allocated_storage      = var.db_allocated_storage
  engine                 = "mysql"
  engine_version         = var.db_engine_version
  instance_class         = var.db_instance_class
  db_name                = var.db_name
  username               = var.db_username
  password               = var.db_password
  parameter_group_name   = "default.mysql8.0"
  db_subnet_group_name   = aws_db_subnet_group.mysql_subnet_group.name
  vpc_security_group_ids = [aws_security_group.sg_mysql_rds.id]

  publicly_accessible     = false
  multi_az                = var.db_multi_az
  skip_final_snapshot     = true
  deletion_protection     = false
  backup_retention_period = 0

  tags = merge(local.common_tags, {
    Name = "mysql"
  })
}

resource "aws_instance" "serveur_musique_app" {
  ami                         = data.aws_ami.amazon_linux_2023.id
  instance_type               = var.instance_type
  subnet_id                   = aws_subnet.public_subnet_1.id
  vpc_security_group_ids      = [aws_security_group.sg_serveur_musique_app.id]
  associate_public_ip_address = true
  key_name                    = var.key_name

  user_data = templatefile("${path.module}/user_data.sh", {
    app_port    = var.app_port
    db_endpoint = aws_db_instance.mysql.address
    db_name     = var.db_name
    db_username = var.db_username
    db_password = var.db_password
    ghcr_image  = var.ghcr_image
  })

  tags = merge(local.common_tags, {
    Name = "serveur-musique-app"
  })
}
