#!/bin/bash

##
# SETUP: ELASTICSEARCH
##
ES_CLUSTER="my-es-cluster"
ES_ADDRESS="localhost:9200"
ES_INDEX="socstream"
ES_TYPE_NAME="query-3"
ES_TYPE_SCHEMA="{
	"properties": {
		"ts": {"type": "date"},
		"pid":    {"type": "long"},
		"cells":  {
			"properties": {
				"cid":      {"type": "text"},
				"presence": {"type": "double"}
			}
		}
	}
}"

echo "[Elasticsearch]> Setting up ${ES_INDEX}/${ES_TYPE_NAME} with schema ${ES_TYPE_SCHEMA} ..."
curl -XDELETE http://${ES_ADDRESS}/${ES_INDEX};
curl -XPUT http://${ES_ADDRESS}/${ES_INDEX};
curl -XPUT http://${ES_ADDRESS}/${ES_INDEX}/_mapping/${ES_TYPE_NAME} -H "Content-Type: application/json" -d'${ES_TYPE_SCHEMA}'
echo "[Elasticsearch]> ${ES_INDEX}/${ES_TYPE_NAME} set up"