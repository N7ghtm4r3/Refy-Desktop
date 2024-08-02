

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import com.tecknobit.refy.ui.theme.RefyTheme
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.titillium
import refy.composeapp.generated.resources.ubuntu

/**
 * **bodyFontFamily** -> the Refy's body font family
 */
lateinit var bodyFontFamily: FontFamily

/**
 * **displayFontFamily** -> the Refy's font family
 */
lateinit var displayFontFamily: FontFamily

@Composable
@Preview
fun App() {
    bodyFontFamily = FontFamily(Font(Res.font.titillium))
    displayFontFamily = FontFamily(Font(Res.font.ubuntu))
    RefyTheme {


    }
}