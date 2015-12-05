package db

import slick.driver.MySQLDriver.api._
import slick.lifted.ForeignKeyQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration.Duration

/**
  * Created by Patrick on 15/11/28.
  */
object SqlTest extends App {
  val db = Database.forConfig("mysqldb")
  val stu = TableQuery[Students]
  val dept = TableQuery[Departments]

  try {
    Await.result(db.run(DBIO.seq(
//      dept.schema.create,
//      dept += Department("SENG", "School of Engineering"),
//      dept += Department("SCI", "School of Schience"),
//      dept += Department("EVEM", "School of Environment"),
//      dept += Department("BUSI", "School of Business and management"),

      //      stu.schema.create,

      //      stu += Student("Patrick", 22, "SENG"),
      //      stu += Student("John", 23, "SENG"),
      //      stu += Student("Peter", 21, "BUSI"),
      //      stu += Student("Jolly", 22, "EVEM"),
      //      stu += Student("Louis", 22, "SCI"),

      // Select * from students
      stu.result.map(x => x.foreach(println)),

      // Select * from students where age = 22
      stu.filter(x => x.age === 22).result.map(x => x.foreach(println)),

      // Select * from students where department = "SENG"
      stu.filter(x => x.department === "SENG").result.map(x => x.foreach(println)),

      // select count(*) as count, DEPARTMENT from STUDNETS group by DEPARTMENT order by count desc;
      stu.groupBy(s => s.department).map { case (department, group) => (department, group.countDistinct) }.result.map(println),

      // select * from students order by department asc, age
      stu.sortBy(s => (s.department.asc, s.age)).result.map(println),

      // select max(age) from students
      stu.map(s => s.age).max.result.map(println),

      // select * from departments
      dept.result.map(println),

      // select s.NAME, d.NAME, d.DESC from STUDNETS as s, Department as d where s.DEPARTMENT = d.name;
      {
        for {
          s <- stu
          d <- s.fk_dept
        } yield (s.name, d.name, d.desc)
      }.result.map(println)

      //      stu.groupBy(s => s.department).result.map(x => x.foreach(println))
    )), Duration.Inf)
  } finally {
    db.close()
  }
}

case class Department(name: String, desc: String)

class Departments(tag: Tag) extends Table[Department](tag, "Department") {
  def name = column[String]("NAME", O.PrimaryKey)

  def desc = column[String]("DESC")

  // the * projection (e.g. select * ...) auto-transforms the tupled
  // column values to / from a User
  def * = (name, desc) <>(Department.tupled, Department.unapply)
}

case class Student(name: String, age: Int, department: String, id: Option[Int] = None)

class Students(tag: Tag) extends Table[Student](tag, "STUDNETS") {
  // Auto Increment the id primary key column
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  // The name can't be null
  def name = column[String]("NAME")

  def age = column[Int]("AGE")

  def department = column[String]("DEPARTMENT")

  // the * projection (e.g. select * ...) auto-transforms the tupled
  // column values to / from a User
  def * = (name, age, department, id.?) <>(Student.tupled, Student.unapply)

  // A reified foreign key relation that can be navigated to create a join
  def fk_dept: ForeignKeyQuery[Departments, Department] =
    foreignKey("DEPT_FK", department, TableQuery[Departments])(_.name)
}
