/*
 * Copyright 2020 Vinay Lodha (https://github.com/vinay-lodha)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
provider "aws" {
  region = "us-east-2"
}
resource "aws_elasticache_cluster" "example" {
  cluster_id = "cluster-example"
  engine = "memcached"
  node_type = "cache.t2.small"
  num_cache_nodes = 1
  parameter_group_name = "default.memcached1.5"
  port = 11211
  tags = {
    Name = "greenbot_db_name"
    owner = "vinay"
  }
}

