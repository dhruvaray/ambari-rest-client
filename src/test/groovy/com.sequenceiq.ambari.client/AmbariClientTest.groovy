/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sequenceiq.ambari.client

import groovy.json.JsonSlurper
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import spock.lang.Specification

class AmbariClientTest extends Specification {

  def rest = Mock(RESTClient)
  def slurper = Mock(JsonSlurper)
  def decorator = Mock(HttpResponseDecorator)
  def ambari = new AmbariClient(rest, slurper)

  def "get the name of the cluster"() {
    given:
    def request = [path: "clusters", query: [fields: "Clusters"]]
    def items = new ArrayList()
    items.add([Clusters: [cluster_name: "cluster1"]])
    def mockResponse = ["items": items]
    rest.get(request) >> decorator
    decorator.data >> [text: "map"]
    slurper.parseText("map") >> mockResponse

    when:
    def result = ambari.getClusterName()

    then:
    "cluster1" == result
  }

  def "get the name when there is no cluster"() {
    given:
    def request = [path: "clusters", query: [fields: "Clusters"]]
    def mockResponse = ["items": new ArrayList()]
    rest.get(request) >> decorator
    decorator.data >> [text: "map"]
    slurper.parseText("map") >> mockResponse

    when:
    def result = ambari.getClusterName()

    then:
    null == result
  }
}