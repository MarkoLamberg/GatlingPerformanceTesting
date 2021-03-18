/*
package simulations

import baseConfig.BaseSimulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class AddPauseTime extends BaseSimulation {

  val scn = scenario("Customers DB")

    .exec(http("Get All Customers - 1st call")
      .get("customers"))
      .pause(5) // pause for 5 seconds

    .exec(http("Get specific game")
      .get("customers/1"))
      .pause(1, 20) // pause for 1 - 20 seconds

    .exec(http("Get All Video Games - 2nd call")
      .get("customers"))
      .pause(3000.milliseconds) // pause for 1000 MS - Note : for the syntax 3000.milliseconds you need to import scala.concurrent.duration.DurationInt


  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)


}*/
