package controllers

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.mvc._

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import play.api.data.validation.Valid
import play.api.data.validation.Constraint
import play.api.data.validation.Invalid
import play.api.i18n.I18nSupport

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  val loginUserForm = Form(mapping(
    "userid" -> text.verifying("ユーザIDを入力してください", { !_.isEmpty() }),
    "userpw" -> text.verifying("パスワードを入力してください", { !_.isEmpty() })
  )
  (LoginUser.apply)(LoginUser.unapply)
  )

  def userRegister() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.userRegisterPage(loginUserForm))
  }
  def loginSubmit() = Action { implicit request =>
    loginUserForm.bindFromRequest.fold(
      errors => {
        BadRequest(views.html.userRegisterPage(errors))
      },
      success => {
        val loginUser = loginUserForm.bindFromRequest.get
        Ok(views.html.index)
      }
    )
  }
}
