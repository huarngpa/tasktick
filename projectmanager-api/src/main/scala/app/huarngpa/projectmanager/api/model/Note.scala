package app.huarngpa.projectmanager.api.model

import java.time.ZonedDateTime

import play.api.libs.json.{Format, Json}

case class Note(id: String,
                project: String,
                task: String,
                user: String,
                note: String,
                lastModified: ZonedDateTime)

object Note {
  implicit val format: Format[Note] = Json.format
}
