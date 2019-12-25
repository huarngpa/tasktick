package app.huarngpa.projectmanager.impl

import java.util.UUID

import akka.{Done, NotUsed}
import app.huarngpa.projectmanager.api.model.Project
import app.huarngpa.projectmanager.api.{CreateProject, ProjectManagerService}
import app.huarngpa.projectmanager.impl.entity.ProjectEntity
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext

class ProjectManagerServiceImpl(persistentEntityRegistry: PersistentEntityRegistry)
                               (implicit ec: ExecutionContext)
  extends ProjectManagerService {

  override def getProject(id: String): ServiceCall[NotUsed, Project] = ServiceCall { _ =>
    val ref = persistentEntityRegistry.refFor[ProjectEntity](id)
    ref.ask(entity.GetState).map {
      case Some(state) => state.project
      case None        => throw NotFound(s"Project $id not found")
    }
  }

  override def createProject: ServiceCall[CreateProject, Done] = ServiceCall { req =>
    val newProjectId = UUID.randomUUID.toString
    val ref = persistentEntityRegistry.refFor[ProjectEntity](newProjectId)
    ref.ask(entity.CreateProjectCommand(
      name = req.name,
      owner = req.owner,
      team = req.team,
      description = req.description,
      imgUrl = req.imgUrl
    ))
  }
}
