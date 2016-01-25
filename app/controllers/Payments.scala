package controllers

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.libs.json.Json
import play.api.libs.ws.ning.NingWSClient
import play.api.mvc._

case class HitForm(hit: String, amountInPence: Int)

class Payments @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {
  val form = Form(mapping(
      "hit" -> nonEmptyText,
      "amountInPence" -> number
    )(HitForm.apply)(HitForm.unapply)
  )

  def index = Action {
    Ok(views.html.index(form))
  }

  def start = Action.async { implicit request =>
    implicit val wsClient = NingWSClient()

    form.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.index(formWithErrors))),
      hitForm => {
        val body = Json.obj(
          "return_url" -> s"${routes.Payments.created().absoluteURL}",
          "account_id" -> "789",
          "description" -> hitForm.hit,
          "reference" -> UUID.randomUUID.toString,
          "amount" -> hitForm.amountInPence
        )

        wsClient.url(s"https://publicapi-integration-1.pymnt.uk/v1/payments")
          .withHeaders("Content-Type" -> "application/json",
                       "Authorization" -> "Bearer 58fb8c50-1617-4489-adfe-db51451af0ca").post(body).map { response =>
          if (response.status == 201) {
            Redirect(((response.json \ "links")(1) \ "href").as[String])
          } else {
            Redirect(routes.Payments.error(Json.prettyPrint(response.json)), 400)
          }
        }
      }
    )
  }

  def created = Action {
    println(s"HELP")
    Ok(views.html.index(form))
  }

  def error(json: String) = Action {
    //Ok(views.html.index(json))
    Ok(views.html.index(form))
  }

}
