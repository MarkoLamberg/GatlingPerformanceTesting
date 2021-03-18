/*
package simulations

import baseConfig.BaseSimulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BeforeAfter extends BaseSimulation {

  // add in before and after blocks like this - useful if you have test data setup or tear down
  before {
    println("Starting Performance Test")
  }

  after {
    println("Performance Test Complete")
  }

  def getAllCustomers() = {
    exec(
      http("Get All Customers - 1st call")
        .get("customers")
        .header("From", "TheFromHeader")
        .check(regex("""Resident Evil \d"""))
        .check(status.is(200)))
  }

  def getSpecificCustomer() = {
    exec(http("Get Specific Video Game")
      .get("customers/2")
      .check(jsonPath("$.name").is("Gran Turismo 3"))
      .check(status.is(200)))
  }

  val scn = scenario("Video Game DB")
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
