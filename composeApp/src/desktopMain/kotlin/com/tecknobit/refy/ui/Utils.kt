package com.tecknobit.refy.ui

import androidx.compose.ui.graphics.Color
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import com.tecknobit.refycore.records.RefyItem
import java.util.*

/**
 * Function to generate a random color for a collection
 *
 * No-any params required
 *
 * @return random color as [Color]
 */
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

/**
 * Function to transform a [Color] value in the corresponding hex code
 *
 * No-any params required
 *
 * @return hex code of the color as [String]
 */
fun Color.toHex(): String {
    val red = (this.red * 255).toInt()
    val green = (this.green * 255).toInt()
    val blue = (this.blue * 255).toInt()
    return String.format("#%02X%02X%02X", red, green, blue)
}

/**
 * Function to get from a [RefyItem] list the item with the corresponding to the identifier pass
 * as parameter
 *
 * @param itemId: the item identifier
 *
 * @return the corresponding item, if exists as [T], null if not exists
 *
 * @param T: the type of the item in the list
 */
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

/**
 * Function to get the complete media url with the current [localUser.hostAddress] value to display
 * a media item such profile pictures or logo pictures
 *
 * @param relativeMediaUrl: the media relative url
 *
 * @return the complete media url as [String]
 */
fun getCompleteMediaItemUrl(
    relativeMediaUrl: String
): String {
    return "${localUser.hostAddress}/$relativeMediaUrl"
}