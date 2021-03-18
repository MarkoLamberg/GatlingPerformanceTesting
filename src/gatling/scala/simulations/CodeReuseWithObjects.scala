/*
package simulations

import baseConfig.BaseSimulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CodeReuseWithObjects extends BaseSimulation {

  def getAllCustomers() = {
    exec(http("Get All Customers")
      .get("customers")
      .check(status.is(200)))
  }

  def getSpecificCustomer() = {
    exec(http("Get Specific Customer")
      .get("customers/1")
      .check(status.in(200 to 210)))
  }

  // Now we can rewrite the scenario like this
  val scn = scenario("Customers DB")
    .exec(getAllCustomers())
    .pause(5)
    .exec(getSpecificCustomer())
    .pause(5)
    .exec(getAllCustomers())

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)



}
*/
