package app.huarngpa.projectmanager.impl.entity

import ai.x.play.json.Jsonx
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventShards, AggregateEventTag}
import play.api.libs.json.Json.WithDefaultValues
import play.api.libs.json.{Format, Json}

sealed trait ProjectEvent extends AggregateEvent[ProjectEvent] {
  override def aggregateTag: AggregateEventShards[ProjectEvent] = ProjectEvent.Tag
}

case class ProjectCreatedEvent(name: String,
                               owner: Option[String],
                               team: Option[String],
                               description: Option[String],
                               imgUrl: Option[String]) extends ProjectEvent

object ProjectCreatedEvent {
  implicit val format: Format[ProjectCreatedEvent] = Json.using[WithDefaultValues].format
}

object ProjectEvent {
  val numShards = 10
  val baseTag: String = "ProjectEvent"
  val Tag: AggregateEventShards[ProjectEvent] = AggregateEventTag.sharded[ProjectEvent](baseTag, numShards)
  implicit val format: Format[ProjectEvent] = Jsonx.formatSealed[ProjectEvent]
}
