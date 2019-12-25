package app.huarngpa.projectmanager.test

import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpecLike, Matchers}

trait UnitSpec
  extends FlatSpecLike
  with Matchers
  with ScalaFutures
  with BeforeAndAfterAll
  with BeforeAndAfterEach
  with Eventually
