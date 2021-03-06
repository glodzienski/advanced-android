package com.example.webmoments.dummy

import com.example.webmoments.entity.Moment
import java.util.HashMap

object MomentsContent {

    val ITEMS: MutableList<Moment> = mutableListOf()

    val ITEM_MAP: MutableMap<String, Moment> = HashMap()

    fun clear() {
        ITEMS.clear()
        ITEM_MAP.clear()
    }

    fun remove(moment: Moment) {
        ITEMS.remove(moment)
        ITEM_MAP.remove(moment.id)
    }
}
