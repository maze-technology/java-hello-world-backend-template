#!/usr/bin/env bash

set -euo pipefail

# enable debug
# set -x

# Print SNS configuration message
echo "Configuring SNS"
echo "==================="

# Define variables
LOCALSTACK_HOST=localhost
AWS_REGION=eu-central-1
AWS_CMD="aws --endpoint-url=http://${LOCALSTACK_HOST}:4566"

# Function to create SNS topic
create_topic() {
  local TOPIC=$1
  $AWS_CMD \
    sns create-topic \
    --name ${TOPIC} \
    --region ${AWS_REGION}
}

# Create topic named local-topic
create_topic "local-topic"
