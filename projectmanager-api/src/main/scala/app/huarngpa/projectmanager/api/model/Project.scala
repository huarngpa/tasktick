package app.huarngpa.projectmanager.api.model

import play.api.libs.json.Json.WithDefaultValues
import play.api.libs.json.{Format, Json}

case class Project(id: String,
                   name: String,
                   owner: Option[String] = None,
                   team: Option[String] = None,
                   description: Option[String] = None,
                   imgUrl: Option[String] = None,
                   tasks: Set[Task] = Set.empty)

object Project {
  implicit val format: Format[Project] = Json.using[WithDefaultValues].format
}
