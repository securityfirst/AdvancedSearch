package org.secfirst.advancedsearch.util

import android.content.Context
import android.util.AttributeSet
import pe.orbis.materialpillsbox.MaterialPillsBox
import pe.orbis.materialpillsbox.PillEntity

class SelectablePillBox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialPillsBox(context, attrs, defStyleAttr) {

    var selectedItems: List<String> = mutableListOf()

    fun setObjects(objects: List<Any>) {
        selectedItems = objects.map {
            (it as PillEntity).message
        }
        super.initFirstSetup(objects)
    }


}