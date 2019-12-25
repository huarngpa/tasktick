package app.huarngpa.projectmanager.impl

import app.huarngpa.projectmanager.api.ProjectManagerService
import app.huarngpa.projectmanager.impl.entity.{ProjectEntity, ProjectSerializerRegistry}
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.api.{Descriptor, ServiceLocator}
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.client.ConfigurationServiceLocatorComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class ProjectManagerLoader extends LagomApplicationLoader{
  override def load(context: LagomApplicationContext): LagomApplication =
    // new ProjectManagerApplication(context) with ConfigurationServiceLocatorComponents
    new ProjectManagerApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new ProjectManagerApplication(context) with LagomDevModeComponents

  override def describeService: Option[Descriptor] = Some(readDescriptor[ProjectManagerService])
}

abstract class ProjectManagerApplication(context: LagomApplicationContext)
  extends LagomApplication(context = context)
  with AhcWSComponents
  with CassandraPersistenceComponents
  with LagomKafkaComponents {

  override def lagomServer: LagomServer = serverFor[ProjectManagerService](wire[ProjectManagerServiceImpl])

  final lazy val projectManagerServiceJsonSerializerRegistry = ProjectSerializerRegistry
  override def jsonSerializerRegistry: JsonSerializerRegistry = projectManagerServiceJsonSerializerRegistry

  // Register entities
  persistentEntityRegistry.register(wire[ProjectEntity])
}
