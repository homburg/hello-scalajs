package tutorial.webapp

import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom.html
import scalatags.Text.all._
import scala.scalajs.js.Date

object TutorialApp extends JSApp {
  def main(): Unit = {
    val ls = ((1 to 17).mkString(", "))
    println(s"Hello world! $ls")

    val work = dom.document.getElementById("work")
    work.innerHTML = getFragment().toString
    
    var ctr = 0;

    val preChange = dom.document.getElementById("pre-change")

    val frm = work.getElementsByTagName("form")(0).asInstanceOf[html.Form]

    frm.onsubmit = { (e: dom.Event) => {
      e.preventDefault()
      false
    }}

    val input = work.getElementsByTagName("input")(0)

    var changed = false

    var lastEvents = List[dom.Event]()

    val inputHandler = {
      (e:dom.Event) => {
        changed = true
        lastEvents = e :: lastEvents
        lastEvents = lastEvents.take(13)
        println(e)
        preChange.innerHTML = lastEvents
          .map((e: dom.Event) => { e match {
            case ke: dom.KeyboardEvent => s"${ke.`type`} / ${ke.keyCode} / ${ke.shiftKey} / ${ke.altKey} / ${ke.ctrlKey}"
            case ke: dom.MouseEvent => s"${ke.`type`} / ${ke.screenX} / ${ke.screenY} / ${ke.timeStamp}"
            case _ => "[unknown]"
          }})
          .mkString("\n")
      }
    }

    val eventTypes = List("click", "mouseover", "mouseout", "mouseenter", "mouseleave", "keyup", "keypress", "dragstart", "dragstop", "dragend", "mousemove")

    for (eventType <- eventTypes) {
      input.addEventListener(eventType, inputHandler)
      println(s"Bound $eventType")
    }

    dom.setInterval(() => {
      if (!changed) {
        ctr += 1;
        val date = (new Date()).toISOString
        preChange.innerHTML = s"${ctr.toString} / ${date}"
      }
    }, 100)
  }

  def getFragment() = {
    div(
      h1("This is my title"),
      form(
        input(`type`:="text"),
        label("Submit this")
      ),
      pre("{\"This\": \"is some json\"}"),
      pre(id:="pre-change")
    )
  }
}
