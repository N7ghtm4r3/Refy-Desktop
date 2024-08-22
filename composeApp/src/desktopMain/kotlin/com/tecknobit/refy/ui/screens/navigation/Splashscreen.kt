package com.tecknobit.refy.ui.screens.navigation

import FrequencyVisibility
import OctocatKDUConfig
import UpdaterDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinox.inputs.InputValidator.DEFAULT_LANGUAGE
import com.tecknobit.equinox.inputs.InputValidator.LANGUAGES_SUPPORTED
import com.tecknobit.refy.helpers.DesktopRefyLocalUser
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.ui.screens.Screen.Routes.CONNECT_SCREEN
import com.tecknobit.refy.ui.screens.Screen.Routes.HOME
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refycore.helpers.RefyRequester
import displayFontFamily
import navigator
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.app_name
import refy.composeapp.generated.resources.app_version
import java.util.*

/**
 * The **Splashscreen** class is the entry point of the application
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 */
class Splashscreen : Screen() {

    companion object {

        /**
         * **localUser** the user of the current logged-in session, used to make the requests to the
         * backed
         */
        val localUser = DesktopRefyLocalUser()

        /**
         * **requester** -> the instance to manage the requests with the backend
         */
        lateinit var requester: RefyRequester

    }

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
    @Composable
    override fun ShowContent() {
        Column (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inversePrimary)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text (
                    text = stringResource(Res.string.app_name),
                    color = Color.White,
                    style = AppTypography.displayLarge,
                    fontSize = 55.sp,
                )
            }
            Row (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(30.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "by Tecknobit",
                    color = Color.White,
                    fontFamily = displayFontFamily,
                    fontSize = 14.sp,
                )
            }
        }
        NavToFirstScreen()
    }

    /**
     * Function to get the first screen to display, this is based on whether the [localUser]
     * is already authenticated or not
     *
     * No-any params required
     */
    @Composable
    private fun NavToFirstScreen() {
        var startApp by remember { mutableStateOf(true) }
        setLocale()
        UpdaterDialog(
            config = OctocatKDUConfig(
                frequencyVisibility = FrequencyVisibility.ONCE_PER_DAY,
                appName = stringResource(Res.string.app_name),
                currentVersion = stringResource(Res.string.app_version),
                onUpdateAvailable = {
                    startApp = false
                },
                dismissAction = {
                    startApp = true
                }
            )
        )
        if (startApp) {
            requester = RefyRequester(
                host = localUser.hostAddress,
                userId = localUser.userId,
                userToken = localUser.userToken
            )
            navigator.navigate(
                if (localUser.isAuthenticated)
                    HOME.name
                else
                    CONNECT_SCREEN.name
            )
        }
    }

    /**
     * Function to set locale language for the application
     *
     * No-any params required
     */
    private fun setLocale() {
        var tag: String = DEFAULT_LANGUAGE
        LANGUAGES_SUPPORTED.keys.forEach { key ->
            if (key == localUser.language) {
                tag = key
                return@forEach
            }
        }
        Locale.setDefault(Locale.forLanguageTag(tag))
    }

}