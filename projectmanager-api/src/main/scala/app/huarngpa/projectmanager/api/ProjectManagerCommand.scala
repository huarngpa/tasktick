package app.huarngpa.projectmanager.api

import play.api.libs.json.Json.WithDefaultValues
import play.api.libs.json.{Format, Json}

trait ProjectManagerCommand

case class CreateProject(name: String,
                         owner: Option[String] = None,
                         team: Option[String] = None,
                         description: Option[String] = None,
                         imgUrl: Option[String] = None)
  extends ProjectManagerCommand

object CreateProject {
  implicit val format: Format[CreateProject] = Json.using[WithDefaultValues].format
}
