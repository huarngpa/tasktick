package app.huarngpa.projectmanager.api

import akka.{Done, NotUsed}
import app.huarngpa.projectmanager.api.model.Project
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait ProjectManagerService extends Service {

  def getProject(id: String): ServiceCall[NotUsed, Project]

  def createProject: ServiceCall[CreateProject, Done]

  override final def descriptor: Descriptor = {
    import Service._
    named("projects")
      .withCalls(
        restCall(Method.GET, "/api/project/:id", getProject _),
        restCall(Method.POST, "/api/project/add", createProject)
      )
      .withAutoAcl(true)
  }
}

object ProjectManagerService

