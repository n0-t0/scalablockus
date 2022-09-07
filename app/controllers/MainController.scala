package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.i18n.I18nSupport
import javax.inject._

@Singleton
class MainController @Inject()(val cc: ControllerComponents) extends AbstractController(cc) with I18nSupport{
  import MainController.LoginUser
  val loginUserForm: Form[LoginUser] = Form(mapping(
    "userid" -> text.verifying("ユーザIDを入力してください", { _.nonEmpty })
  )
  (LoginUser.apply)(LoginUser.unapply)
  )

  def userRegister(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.userLogin(loginUserForm))
  }
  def loginSubmit(): Action[AnyContent] = Action { implicit request =>
//    loginUserForm.bindFromRequest.fold()
    ???
  }
}
object MainController {
  case class LoginUser(userId: String)
}