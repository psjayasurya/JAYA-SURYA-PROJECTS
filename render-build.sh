#!/bin/bash
apt-get update
apt-get install -y curl
curl -sSL https://sdk.cloud.google.com | bash
export PATH=$PATH:/root/google-cloud-sdk/bin
pip install -r requirements.txt
