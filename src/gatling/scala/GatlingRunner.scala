import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder
import simulations._
import finalSimulation._


object GatlingRunner {

  def main(args: Array[String]): Unit = {

    val simClass = classOf[FullTest].getName
    val props = new GatlingPropertiesBuilder

    props.simulationClass(simClass)

    Gatling.fromMap(props.build)
  }

}
