package model

import play.api.data.FormError

object FormErrors {

  val errorEmailDuplicated = FormError("emial", "このメールアドレスは既に使われています")
  val errorLoginEmail      = FormError("email", "メールアドレスまたはパスワードが間違っています")
  val errorLoginPassword   = FormError("password", "メールアドレスまたはパスワードが間違っています")
}
