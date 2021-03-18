package simulations

import baseConfig.BaseSimulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt


class BasicLoadSimulation extends BaseSimulation {

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

  val scn = scenario("Customers DB")
    .exec(getAllCustomers())
    .pause(5)
    .exec(getSpecificCustomer())
    .pause(5)
    .exec(getAllCustomers())


  // Load Simulation 1:  basic Load Simulation
  setUp(
    scn.inject(
      nothingFor(5 seconds), // do nothing for 5 seconds
      atOnceUsers(5), // inject 5 users at once
      rampUsers(10) over (10 seconds) // inject 10 users over a period of 10 seconds
    ).protocols(httpConf.inferHtmlResources()) // inferHtmlResources will fetch everything on the page (JS, CSS, images etc.)
  )

}
