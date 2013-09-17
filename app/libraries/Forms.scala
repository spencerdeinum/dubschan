package libraries

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

object Forms {

  def threadForm = Form(
    tuple(
      "Title" -> text.verifying(nonEmpty, maxLength(255)),
      "Content" -> text.verifying(nonEmpty, maxLength(255))
    )
  )

  def postForm =  Form("Content" -> text.verifying(nonEmpty, maxLength(0)))

}
