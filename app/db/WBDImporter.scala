package db

import slick.driver.MySQLDriver.api._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.Source

/**
  * Created by Patrick on 15/11/26.
  */
object WBDImporter extends App {
  def importCountries(): Unit = {
    val countryFilePath = "/Users/patrick/Downloads/ny.gdp.pcap.cd_Indicator_en_csv_v2/Metadata_Country_ny.gdp.pcap.cd_Indicator_en_csv_v2.csv"
    val countryFile = Source.fromFile(countryFilePath)
    val lines = for (cols <- countryFile.getLines.drop(1).map(l => l.substring(0, l.length - 1).split("\",\"").map(_.replace("\"", "").trim)))
      yield Country(cols(1), cols(0), cols(2), cols(3), cols(4))

    val db = Database.forConfig("mysqldb")
    val countrySql = TableQuery[Countries]

    lines.foreach { l =>
      println(l)
      Await.result(db.run {
        countrySql += l
      }, 5000 millis)
    }
    db.close()
    countryFile.close()

    println("Total countries imported: " + lines.size)
  }

  def importIndicatorValues(): Unit = {
    val ivFilePath = "/Users/patrick/Downloads/world bank data/inflation/fp.cpi.totl.zg_Indicator_en_csv_v2.csv"
    val ivFile = Source.fromFile(ivFilePath)

    val db = Database.forConfig("mysqldb")
    val ivSql = TableQuery[Indicator_Values]

    ivFile.getLines().toSeq.drop(5).foreach { line =>
      val split = line.split("\",\"").map(x => x.replace("\"", "")).dropRight(1)
      val country_code: String = split(1)
      val indicator_code: String = split(3)
      val year_indicators = split.splitAt(4)._2.map(x => if (x.nonEmpty) x.toDouble else 0.0)
      val year_indi = (0 to year_indicators.length).zip(year_indicators)

      println("Indicator: " + indicator_code + ", country: " + country_code + ", Total indicators imported: " + year_indi.size)

      // Special case, ignore 'INX' row. The row is not classified.
      if (country_code == "INX")
        println("INX row encoutered, ignore")
      else {
        var arr = List[Indicator_Value]()
        year_indi.foreach(x => arr = Indicator_Value(country_code, indicator_code, x._1 + 1960, x._2) :: arr)

        Await.result(db.run {
          ivSql ++= arr
        }, 5000 millis)
      }
    }
    db.close()
    ivFile.close()
  }

  importIndicatorValues()
}
