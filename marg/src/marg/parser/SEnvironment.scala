package marg.parser

import marg.lang.data.SType

import scala.collection.mutable

class SEnvironment(outer: SEnvironment) {
  val map = mutable.Map[String, SType]()


  def find(key: String): Boolean =
    map.contains(key) || ((outer != null) && outer.find(key))

  def get(key: String): SType = {
    if (map.contains(key)) map(key)
    else if (outer != null) outer.get(key)
    else null
  }

  def put(key: String, o: SType) {
    if (find(key)) {
      if (map.contains(key)) map.put(key, o)
      else outer.put(key, o)
    }
    else map.put(key, o)
  }
}
