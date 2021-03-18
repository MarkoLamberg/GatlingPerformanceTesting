package baseConfig

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BaseSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:8081/")
    .header("Accept", "application/json")
}
