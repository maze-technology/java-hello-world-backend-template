#!/usr/bin/env bash

set -euo pipefail

# enable debug
# set -x

# Print SNS Subscription configuration message
echo "Configuring SNS Subscription"
echo "==================="

# Define variables
LOCALSTACK_HOST=localhost
AWS_REGION=eu-central-1
AWS_CMD="aws --endpoint-url=http://${LOCALSTACK_HOST}:4566"

# Function to create SNS subscription
create_subscription() {
  local TOPIC=$1
  local QUEUE=$2
  $AWS_CMD \
    sns subscribe \
    --topic-arn arn:aws:sns:eu-central-1:000000000000:${TOPIC} \
    --protocol sqs \
    --notification-endpoint arn:aws:sqs:eu-central-1:000000000000:${QUEUE} \
    --region ${AWS_REGION}
}

# Create subscription for local-topic to queue-out
create_subscription "local-topic" "queue-out"
