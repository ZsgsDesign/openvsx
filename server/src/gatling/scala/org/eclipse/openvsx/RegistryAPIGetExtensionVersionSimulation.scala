/** ******************************************************************************
 * Copyright (c) 2021 Precies. Software and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 * ****************************************************************************** */
package org.eclipse.openvsx

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RegistryAPIGetExtensionVersionSimulation extends Simulation {
  val conf = ConfigFactory.load()

  val httpProtocol = http
    .baseUrl(conf.getString("baseUrl"))
    .disableCaching

  var headers: Map[String,String] = Map()
  if(conf.hasPath("auth")) {
    headers += "Authorization" -> conf.getString("auth");
  }

  val scn = scenario("RegistryAPI: Get Extension Version")
    .repeat(744) {
      feed(csv("extension-versions.csv"))
        .exec(http("RegistryAPI.getExtensionVersion")
          .get("""/api/${namespace}/${name}/${version}""")
          .headers(headers))
//          .check(status.is(200)))
    }

  setUp(scn.inject(atOnceUsers(4))).protocols(httpProtocol)
}
