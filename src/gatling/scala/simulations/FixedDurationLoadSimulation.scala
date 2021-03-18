/*
package simulations

import baseConfig.BaseSimulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt


class FixedDurationLoadSimulation extends BaseSimulation {

  def getAllCustomers() = {
    exec(
      http("Get All Customers - 1st call")
        .get("customers")
        .check(status.is(200)))
  }

  def getSpecificCustomer() = {
    exec(http("Get Specific Customer")
      .get("customers/2")
      .check(status.is(200)))
  }

  val scn = scenario("Customer DB")
      .forever() {
        exec(getAllCustomers())
          .pause(5)
          .exec(getSpecificCustomer())
          .pause(5)
          .exec(getAllCustomers())
      }


  // Run for a fixed duration
  setUp(
    scn.inject(
            nothingFor(5 seconds),
            atOnceUsers(10),
            rampUsers(50) over (30 second)
    ).protocols(httpConf.inferHtmlResources()))
    .maxDuration(1 minute)



}
*/
