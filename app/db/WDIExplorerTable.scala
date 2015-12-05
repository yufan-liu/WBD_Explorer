package db

/**
  * Created by Patrick on 15/11/19.
  */

import slick.driver.MySQLDriver.api._
import slick.lifted.ForeignKeyQuery

case class Country(code: String, name: String, region: String, income_group: String, notes: String)

class Countries(tag: Tag)
  extends Table[Country](tag, "country") {
  // This is the primary key column:
  def code: Rep[String] = column[String]("country_code", O.PrimaryKey)

  def name: Rep[String] = column[String]("country_name")

  def region: Rep[String] = column[String]("country_region")

  def income_group: Rep[String] = column[String]("country_income_group")

  def notes: Rep[String] = column[String]("country_notes")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (code, name, region, income_group, notes) <>(Country.tupled, Country.unapply)
}

case class Indicator(code: String, name: String, note: String, organization: String)

class Indicators(tag: Tag)
  extends Table[Indicator](tag, "indicator") {

  // This is the primary key column:
  def code: Rep[String] = column[String]("indicator_code", O.PrimaryKey)

  def name: Rep[String] = column[String]("indicator_name")

  def note: Rep[String] = column[String]("indicator_note")

  def organization: Rep[String] = column[String]("indicator_org")

  // Every table needs a * projection with the same type as the table's type parameter
  def *  = (code, name, note, organization) <> (Indicator.tupled, Indicator.unapply)
}

case class Indicator_Value(country_code: String, indicator_code: String, year: Int, value: Double, id: Option[Int] = None)

class Indicator_Values(tag: Tag)
  extends Table[Indicator_Value](tag, "indicator_values") {

  // This is the primary key column:
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def country_code: Rep[String] = column[String]("country_code")

  def indicator_code: Rep[String] = column[String]("indicator_code")

  def year: Rep[Int] = column[Int]("year")

  def value: Rep[Double] = column[Double]("value")

  // Every table needs a * projection with the same type as the table's type parameter
  def *  = (country_code, indicator_code, year, value, id.?) <> (Indicator_Value.tupled, Indicator_Value.unapply)

  def country: ForeignKeyQuery[Countries, Country] =
    foreignKey("fk_country_code", country_code, TableQuery[Countries])(_.code)

  def indicator: ForeignKeyQuery[Indicators, Indicator] =
    foreignKey("fk_indicator_code", indicator_code, TableQuery[Indicators])(_.code)
}
