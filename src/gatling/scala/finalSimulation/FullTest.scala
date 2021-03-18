package finalSimulation

import baseConfig.BaseSimulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt
import scala.util.Random

class FullTest extends BaseSimulation {

  def userCount: Int = getProperty("USERS", "3").toInt
  def rampDuration: Int = getProperty("RAMP_DURATION", "10").toInt
  def testDuration: Int = getProperty("DURATION", "60").toInt

  val rnd = new Random()

  private def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  def randomString(length: Int) = {
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  val customerCustomFeeder = Iterator.continually(Map(
    "title" -> ("mr"),
    "name" -> ("name-" + randomString(5))
  ))

  val tourCustomFeeder = Iterator.continually(Map(
    "tourPackageCode" -> ("BW"),
    "title" -> ("title-" + randomString(5)),
    "duration" -> (rnd.nextDouble() + " hours"),
    "price" -> (rnd.nextInt(99) + 1)
  ))

  before {
    println(s"Running test with ${userCount} users")
    println(s"Ramping users over ${rampDuration} seconds")
    println(s"Total Test duration: ${testDuration} seconds")
  }

  /////////////////////////////////////////////////////

  def getAllCustomers(expected: Int) = {
    exec(
      http("Get All Customers")
        .get("customers")
        .check(status.is(expected)))
  }

  def postCustomer(expected: Int) = {
      feed(customerCustomFeeder).
        exec(http("Post Customer")
          .post("customers")
          .body(ElFileBody("NewCustomerTemplate.json")).asJSON
          .check(status.is(expected))
        .check(jsonPath("$.id").saveAs("customerId"))
        .check(jsonPath("$.title").saveAs("customerTitle"))
        .check(jsonPath("$.name").saveAs("customerName")))
  }

  def postCustomerAgain(expected: Int) = {
      exec(http("Post Customer Again")
        .post("customers")
        .body(StringBody("""{"title": ${customerTitle},"name": ${customerName}}""")).asJSON
        .check(status.is(expected)))
  }

  def updatePostedCustomer(expected: Int) = {
    exec(http("Update Posted Customer")
      .put("customers/${customerId}")
      .body(StringBody("""{"title": "ms"}""")).asJSON
      .check(status.is(expected)))
  }

  def getLastPostedCustomer(expected: Int) = {
    exec(http("Get Last Posted Customer")
      .get("customers/${customerId}")
      .check(jsonPath("$.name").is("${name}"))
      .check(status.is(expected)))
  }

  def deleteLastPostedCustomer(expected: Int) = {
    exec(http("Delete Last Posted Customer")
      .delete("customers/${customerId}")
      .check(status.is(expected)))
  }

  /////////////////////////////////////////////////////

  def getAllTours(expected: Int) = {
    exec(
      http("Get All Tours")
        .get("tours")
        .check(status.is(expected)))
  }

  def postTour(expected: Int) = {
    feed(tourCustomFeeder).
      exec(http("Post Tour")
        .post("tours")
        .body(ElFileBody("NewTourTemplate.json")).asJSON
        .check(status.is(expected))
        .check(jsonPath("$.id").saveAs("tourId"))
        .check(jsonPath("$.title").saveAs("tourTitle"))
        .check(jsonPath("$.tourPackage.code").saveAs("tourPackageCode")))
  }

  def postTourAgain(expected: Int) = {
      exec(http("Post Tour Again")
        .post("tours")
        .body(StringBody("""{"title": ${tourTitle},"tourPackageCode": ${tourPackageCode}, "duration":"1.0 hours"}""")).asJSON
        .check(status.is(expected)))
  }

  def updatePostedTour(expected: Int) = {
    exec(http("Update Posted Tour")
      .put("tours/${tourId}")
      .body(StringBody("""{"price": 22}""")).asJSON
      .check(status.is(expected)))
  }

  def getLastPostedTour(expected: Int) = {
    exec(http("Get Last Posted Tour")
      .get("tours/${tourId}")
      .check(jsonPath("$.title").is("${title}"))
      .check(status.is(expected))
      .check(jsonPath("$.tourPackage.location").saveAs("tourLocation")))
  }

  def getToursByLocation(expected: Int) = {
    exec(http("Get Tours by Location")
      .get("tours/byLocation/${tourLocation}")
      .check(status.is(expected)))
  }

  def deleteLastPostedTour(expected: Int) = {
    exec(http("Delete Last Posted Tour")
      .delete("tours/${tourId}")
      .check(status.is(expected)))
  }

  /////////////////////////////////////////////////////

  def getAllBookings(expected: Int) = {
    exec(http("Get All Bookings")
        .get("tours/bookings")
        .check(status.is(expected)))
  }

  def postBooking(expected: Int) = {
      exec(http("Post Booking")
        .post("tours/${tourId}/bookings")
        .body(StringBody("""{"customerId": ${customerId},"participants": 1,"pickupDateTime": "2021-06-26T17:30:00","pickupLocation": "Hotel"}""")).asJSON
        .check(status.is(expected))
        .check(jsonPath("$.bookingId").saveAs("bookingId")))
  }

  def getLastPostedBooking(expected: Int) = {
    exec(http("Get Last Posted Booking")
      .get("tours/booking/${bookingId}")
      .check(status.is(expected)))
  }

  def deleteLastPostedBooking(expected: Int) = {
    exec(http("Delete Last Posted Booking")
      .delete("tours/booking/${bookingId}")
      .check(status.is(expected)))
  }

  def deleteBookingByCustomer(expected: Int) = {
    exec(http("Delete Bookings by Customer")
      .delete("tours/bookings/${customerId}")
      .check(status.is(expected)))
  }

  def deleteBookingsByTour(expected: Int) = {
    exec(http("Delete Bookings by Tour")
      .delete("tours/${tourId}/bookings")
      .check(status.is(expected)))
  }

  /////////////////////////////////////////////////////

  val scn = scenario("Customers DB")
      .forever() {
           exec(getAllCustomers(200))
             .pause(2)
          .exec(postCustomer(201))
             .pause(2)
          .exec(postCustomerAgain(400))
             .pause(2)
          .exec(getLastPostedCustomer(200))
             .pause(2)
          .exec(updatePostedCustomer(200))
             .pause(2)
          .exec(getAllTours(200))
             .pause(2)
          .exec(postTour(201))
             .pause(2)
          .exec(postTourAgain(400))
             .pause(2)
          .exec(getLastPostedTour(200))
             .pause(2)
          .exec(updatePostedTour(200))
             .pause(2)
          .exec(getToursByLocation(200))
             .pause(2)
          .exec(getAllBookings(200))
             .pause(2)
          .exec(postBooking(201))
             .pause(2)
          .exec(getLastPostedBooking(200))
             .pause(2)
          .exec(deleteLastPostedBooking(200))
             .pause(2)
          .exec(deleteLastPostedBooking(400))
             .pause(2)
          .exec(getLastPostedBooking(400))
              .pause(2)
          .exec(postBooking(201))
             .pause(2)
          .exec(getLastPostedBooking(200))
             .pause(2)
          .exec(deleteBookingByCustomer(200))
             .pause(2)
          .exec(deleteBookingByCustomer(400))
             .pause(2)
          .exec(getLastPostedBooking(400))
              .pause(2)
          .exec(postBooking(201)
             .pause(2)
          .exec(getLastPostedBooking(200))
             .pause(2)
          .exec(deleteBookingsByTour(200))
             .pause(2)
          .exec(getLastPostedBooking(400))
              .pause(2)
          .exec(deleteLastPostedCustomer(200))
             .pause(2)
          .exec(deleteLastPostedCustomer(400))
            .pause(2)
          .exec(deleteLastPostedTour(200))
            .pause(2)
          .exec(deleteLastPostedTour(400)))
      }

  setUp(
    scn.inject(
      nothingFor(5 seconds),
      rampUsers(userCount) over (rampDuration seconds))
  )
    .protocols(httpConf)
    .maxDuration(testDuration seconds)

  after {
    println("Stress test completed")
  }
}
