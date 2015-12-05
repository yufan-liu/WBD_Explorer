package controllers

import db.{Country, Indicator, Indicator_Value, MySQLHelper}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc._

object Application extends Controller {
  // Html Pages
  def index = Action {
    Ok(views.html.index())
  }

  def about = Action {
    Ok(views.html.about())
  }

  def overview = Action {
    Ok(views.html.overview())
  }

  def gdp = Action {
    Ok(views.html.gdp())
  }

  def gdpGrowth = Action {
    Ok(views.html.gdp_growth())
  }

  def gdpPerCapital = Action {
    Ok(views.html.gdp_per_capital())
  }

  // Test
  def test = Action {
    Ok(views.html.pck.test())
  }

  def d3test = Action {
    Ok(views.html.pck.d3test())
  }

  def tt = Action {
    Ok(views.html.pck.tt())
  }

  def indiTest = Action {
    Ok(views.html.pck.indi_tt())
  }

  // Json, Web Service
  implicit val countryWriters = Json.writes[Country]
  implicit val indicatorWriters = Json.writes[Indicator]
  implicit val indiValueWriters = Json.writes[Indicator_Value]

  def db = allCountries

  def allCountries = Action.async { implicit request =>
    MySQLHelper.allCountries().map(x => Ok(Json.toJson(x)))
  }

  def allIndicators = Action.async { implicit request =>
    MySQLHelper.allIndicators().map(x => Ok(Json.toJson(x)))
  }

  def indiValue(indi: String, ctry: String) = Action.async { implicit request =>
    MySQLHelper.getIndiValue(indi, ctry).map(x => Ok(Json.toJson(x)))
  }

  // Test Route syntax
  // 1. send the parameter `id` value back to client
  def getId(id: Long) = Action {
    Ok(Json.toJson(id))
  }

  // 2. get 2 parameters
  def get2Params(indi: String, ctry: String) = Action {
    Ok(Json.toJson(Seq(indi, ctry)))
  }
}