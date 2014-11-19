package marg.parser

import marg.lang.data.SType

import scala.collection.mutable

class Env(outer: Env) {
  val map = mutable.Map[String, SType]()


  def find(key: String): Boolean =
    map.contains(key) || ((outer != null) && outer.find(key))

  def get(key: String): SType =
    if (map.contains(key)) map(key)
    else if (outer != null) outer.get(key)
    else null

  def +=(t: (String, SType)): Unit =
    if (find(t._1))
      if (map.contains(t._1)) map += t
      else outer += t
    else map += t
}

