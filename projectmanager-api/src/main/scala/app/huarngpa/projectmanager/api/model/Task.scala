package app.huarngpa.projectmanager.api.model

import java.time.ZonedDateTime

import play.api.libs.json.Json.WithDefaultValues
import play.api.libs.json.{Format, Json}

case class Task(id: String,
                project: String,
                name: String,
                description: Option[String],
                done: Boolean,
                assigned: Option[String],
                startDate: Option[ZonedDateTime],
                endDate: Option[ZonedDateTime],
                lastModified: ZonedDateTime,
                section: String,
                parent: Option[String],
                notes: Seq[Note] = Seq.empty)

object Task {
  implicit val format: Format[Task] = Json.using[WithDefaultValues].format
}
