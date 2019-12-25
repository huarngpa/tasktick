package app.huarngpa.projectmanager.impl.entity

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import play.api.libs.json.Json.WithDefaultValues
import play.api.libs.json.{Format, Json}

sealed trait ProjectCommand

case object GetState extends ProjectCommand with ReplyType[Option[ProjectState]] {
  implicit val format: Format[GetState.type] = JsonSerializer.emptySingletonFormat(GetState)
}

case class CreateProjectCommand(name: String,
                                owner: Option[String],
                                team: Option[String],
                                description: Option[String],
                                imgUrl: Option[String])
  extends ProjectCommand with ReplyType[Done]

object CreateProjectCommand {
  implicit val format: Format[CreateProjectCommand] = Json.using[WithDefaultValues].format
}
