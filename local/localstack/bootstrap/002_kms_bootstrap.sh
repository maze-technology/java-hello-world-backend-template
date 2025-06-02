#!/usr/bin/env bash

set -euo pipefail

# enable debug
# set -x

# Print KMS configuration message
echo "Configuring KMS"
echo "==================="

# Define variables
LOCALSTACK_HOST=localhost
AWS_REGION=eu-central-1
AWS_CMD="aws --endpoint-url=http://${LOCALSTACK_HOST}:4566"
KMS_ALIAS_NAME=alias/local

# Function to create KMS key and alias
create_kms() {
  KMS_KEY_ID=$($AWS_CMD \
    kms create-key \
    --query KeyMetadata.KeyId \
    --output text)
  echo "KMS_KEY_ID: ${KMS_KEY_ID}"
  $AWS_CMD \
    kms create-alias \
    --alias-name ${KMS_ALIAS_NAME} \
    --target-key-id ${KMS_KEY_ID}
}

# Create KMS key and alias
create_kms
