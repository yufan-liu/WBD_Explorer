package db

import slick.driver.MySQLDriver.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by Patrick on 15/11/19.
  */
object WDIExplorer extends App {
  val db = Database.forConfig("mysqldb")
  try {
    val countrySql = TableQuery[Countries]
    val indicatorSql = TableQuery[Indicators]
    val ivSql = TableQuery[Indicator_Values]
    Await.result(db.run(DBIO.seq(
      //      countrySql +=("CHN", "China", "East Asia & Pacific", "Upper middle income", "On 1 July 1997 China resumed its exercise of sovereignty over Hong Kong; and on 20 December 1999 China resumed its exercise of sovereignty over Macao. Unless otherwise noted, data for China do not include data for Hong Kong SAR, China; Macao SAR, China; or Taiwan, China. Based on data from the National Bureau of Statistics, the methodology for national accounts exports and imports of goods and services in constant prices have been revised from 2000 onward.")
      //            indicatorSql +=("NY.GDP.PCAP.CD", "GDP per capita (current US$)", "GDP per capita is gross domestic product divided by midyear population. GDP is the sum of gross value added by all resident producers in the economy plus any product taxes and minus any subsidies not included in the value of the products. It is calculated without making deductions for depreciation of fabricated assets or for depletion and degradation of natural resources. Data are in current U.S. dollars.", "World Bank national accounts data, and OECD National Accounts data files.")
      //            ivSql +=("CHN", "NY.GDP.PCAP.CD", 1960, 88.72249756)
      countrySql.filter(x => x.region === "South Asia").result.map(println)
    )), Duration.Inf)
  } finally db.close()
}
