package com.tecknobit.refy.ui

import androidx.compose.ui.graphics.Color
import com.tecknobit.refycore.records.RefyItem
import java.util.*

fun generateRandomColor() : Color {
    val random = Random()
    return Color(
        red = random.nextFloat(),
        green = random.nextFloat(),
        blue = random.nextFloat(),
        alpha = 1f
    )
}

/**
 * Function to get the color from its hex code
 *
 * @return color as [Color]
 */
fun String.toColor(): Color {
    return Color(("ff" + removePrefix("#").lowercase()).toLong(16))
}

fun Color.toHex(): String {
    val red = (this.red * 255).toInt()
    val green = (this.green * 255).toInt()
    val blue = (this.blue * 255).toInt()
    return String.format("#%02X%02X%02X", red, green, blue)
}

fun <T : RefyItem> List<T>.getRefyItem(
    itemId: String?
) : T? {
    if(itemId != null) {
        this.forEach { item ->
            if(item.id == itemId)
                return item
        }
    }
    return null
}