package db

import slick.driver.MySQLDriver.api._

/**
  * Created by Patrick on 15/11/18.
  */
object MySQLHelper {
  val db = Database.forConfig("mysqldb")
  val countrySQL = TableQuery[Countries]
  val indicatorSQL = TableQuery[Indicators]
  val indi_valueSQL = TableQuery[Indicator_Values]

  def allCountries() = {
    db.run(countrySQL.result)
  }

  def allIndicators() = {
    db.run(indicatorSQL.result)
  }

  def getIndiValue(indi: String, ctry: String) = {
    db.run(indi_valueSQL.filter(iv => iv.indicator_code === indi && iv.country_code === ctry).sorted(iv => iv.year).result)
  }
}
