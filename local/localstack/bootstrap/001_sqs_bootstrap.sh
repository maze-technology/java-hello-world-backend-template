#!/usr/bin/env bash

set -euo pipefail

# enable debug
# set -x

# Print SQS configuration message
echo "Configuring SQS"
echo "==================="

# Define variables
LOCALSTACK_HOST=localhost
AWS_REGION=eu-central-1
AWS_CMD="aws --endpoint-url=http://${LOCALSTACK_HOST}:4566"

# Function to create SQS queue
create_queue() {
  local QUEUE_NAME_TO_CREATE=$1
  $AWS_CMD \
    sqs create-queue \
    --queue-name ${QUEUE_NAME_TO_CREATE} \
    --region ${AWS_REGION} \
    --attributes VisibilityTimeout=30
}

# Create queues named queue-in and queue-out
create_queue "queue-in"
create_queue "queue-out"
