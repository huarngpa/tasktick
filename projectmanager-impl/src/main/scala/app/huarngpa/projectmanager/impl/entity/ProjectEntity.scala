package app.huarngpa.projectmanager.impl.entity

import java.util.UUID

import akka.Done
import app.huarngpa.projectmanager.api.model._
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import org.slf4j.LoggerFactory
import play.api.libs.json.Json.WithDefaultValues
import play.api.libs.json.{Format, Json}

import scala.collection.immutable

class ProjectEntity extends PersistentEntity {

  private val log = LoggerFactory.getLogger(this.getClass)

  override type Command = ProjectCommand
  override type Event = ProjectEvent
  override type State = Option[ProjectState]

  /** The initial state is a "Null" project */
  override def initialState: Option[ProjectState] = None

  override def behavior: Behavior = {
    case None => uninitialized
    case _    => defaultActions
  }

  private val getState = {
    Actions()
      .onReadOnlyCommand[GetState.type, State] {
        case (_, ctx, state) =>
          ctx.reply(state)
      }
  }

  private val uninitialized = {
    Actions()
      .onCommand[CreateProjectCommand, Done] {
        case (cmd: CreateProjectCommand, ctx, None) =>
          ctx.thenPersist(ProjectCreatedEvent(
            name = cmd.name,
            owner = cmd.owner,
            team = cmd.team,
            description = cmd.description,
            imgUrl = cmd.imgUrl
          )) { _ =>
            log.info(s"Project=$entityId - Successfully created.")
            ctx.reply(Done)
          }
      }
      .onEvent {
        case (e: ProjectCreatedEvent, _) =>
          Some(Created(Project(
            id = UUID.fromString(entityId).toString,
            name = e.name,
            owner = e.owner,
            team = e.team,
            description = e.description,
            imgUrl = e.imgUrl
          )))
      }
  }

  private val defaultActions =
    Actions()
      .orElse(getState)
}

/** The current state held by the persistent entity */
sealed trait ProjectState {
  def project: Project
  def updated(newData: Project): ProjectState
}

object ProjectState {
  implicit val format: Format[ProjectState] = Json.using[WithDefaultValues].format
}

case class Created(project: Project) extends ProjectState {
  override def updated(newData: Project): ProjectState = this.copy(newData)
}

object Created {
  implicit val format: Format[Created] = Json.using[WithDefaultValues].format
}

object ProjectSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: immutable.Seq[JsonSerializer[_]] =
    immutable.Seq(
      // Commands
      JsonSerializer[GetState.type],
      JsonSerializer[CreateProjectCommand],
      // Events
      JsonSerializer[ProjectCreatedEvent],
      // State
      JsonSerializer[ProjectState],
      JsonSerializer[Created],
    )
}
