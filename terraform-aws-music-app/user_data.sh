#!/bin/bash
set -eux

dnf update -y
dnf install -y docker

systemctl enable docker
systemctl start docker
usermod -aG docker ec2-user

cat >/home/ec2-user/app-env.sh <<EOF
export APP_PORT="${app_port}"
export DB_ENDPOINT="${db_endpoint}"
export DB_NAME="${db_name}"
export DB_USERNAME="${db_username}"
export DB_PASSWORD="${db_password}"
export GHCR_IMAGE="${ghcr_image}"
EOF

chown ec2-user:ec2-user /home/ec2-user/app-env.sh
chmod 600 /home/ec2-user/app-env.sh
