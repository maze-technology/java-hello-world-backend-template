#!/usr/bin/env bash

set -euo pipefail

# enable debug
# set -x

# Print SECRET configuration message
echo "Configuring SECRET"
echo "==================="

# Define variables
LOCALSTACK_HOST=localhost
AWS_REGION=eu-central-1
KMS_KEY_ID=alias/local
AWS_CMD="aws --endpoint-url=http://${LOCALSTACK_HOST}:4566"
DB_PASSWORD=local

ENCRYPTION_CONTEXT_DATA_KEY="app=local,name=data-key"
ENCRYPTION_CONTEXT_DB_PASWORD="app=local,name=db-password"

# Function to create data key
create_data_key() {
  # generate data key
  DATA_KEY=$($AWS_CMD \
    kms generate-data-key-without-plaintext \
    --key-id $KMS_KEY_ID \
    --key-spec AES_256 \
    --encryption-context $ENCRYPTION_CONTEXT_DATA_KEY \
    --query CiphertextBlob \
    --output text)
  echo "DATA_KEY = $DATA_KEY"
  $AWS_CMD \
    secretsmanager create-secret \
    --name local/data-key \
    --description "Data key for local app" \
    --secret-string $DATA_KEY
}

# Function to create encrypted database password
create_db_password() {
  ENCRYPTED_DB_PASSWORD=$($AWS_CMD \
    kms encrypt \
    --key-id $KMS_KEY_ID \
    --encryption-context $ENCRYPTION_CONTEXT_DB_PASWORD \
    --plaintext $DB_PASSWORD \
    --query CiphertextBlob \
    --output text)
  echo "ENCRYPTED_DB_PASSWORD = $ENCRYPTED_DB_PASSWORD"
  $AWS_CMD \
    secretsmanager create-secret \
    --name local/db-password \
    --secret-string "$ENCRYPTED_DB_PASSWORD"
}

# Create data key and encrypted database password
create_data_key
create_db_password
