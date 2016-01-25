package uk.gov.homeoffice.gds.payments

import scala.concurrent.duration._
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.ning.NingWSClient
import play.api.test.PlaySpecification
import org.specs2.concurrent.ExecutionEnv

class GDSPaymentAPISpec(implicit ev: ExecutionEnv) extends PlaySpecification {
  "GDS payments API" should {
    "create a payment" in {

      implicit val wsClient = NingWSClient()

      val payment = Json.parse("""
      {
        "return_url": "https://service-name.gov.uk/transactions/12345",
        "account_id": "789",
        "description": "New passport application",
        "reference": "12345",
        "amount": 12000
      }""")

      val response = wsClient.url(s"https://publicapi-integration-1.pymnt.uk/v1/payments")
          .withHeaders("Content-Type" -> "application/json",
                       "Authorization" -> "Bearer 58fb8c50-1617-4489-adfe-db51451af0ca").post(payment).map { response =>
        (response.status, response.json)
      }

      response must beLike[(Int, JsValue)] {
        case (status, json) => status mustEqual 201
      }.awaitFor(10 seconds)
    }

    "find payment 98" in {
      implicit val wsClient = NingWSClient()

      val response = wsClient.url(s"https://publicapi-integration-1.pymnt.uk/v1/payments/98")
        .withHeaders("Authorization" -> "Bearer 58fb8c50-1617-4489-adfe-db51451af0ca").get().map { response =>
        (response.json \ "payment_id").as[String]
      }

      response must beEqualTo("98").awaitFor(10 seconds)
    }
  }
}