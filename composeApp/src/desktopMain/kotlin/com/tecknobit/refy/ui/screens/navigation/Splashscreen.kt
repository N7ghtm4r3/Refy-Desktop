package com.tecknobit.refy.ui.screens.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.library.generated.resources.Res
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.ui.screens.Screen.Routes.HOME
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refycore.records.RefyUser
import displayFontFamily
import kotlinx.coroutines.delay
import navigator
import org.jetbrains.compose.resources.stringResource

class Splashscreen : Screen() {

    companion object {

        // TODO: TO INIT CORRECTLY CHECK TO REPLACE WITH LOCALUSER INSTEAD
        val user = RefyUser("h1")

    }

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

    @Composable
    private fun NavToFirstScreen() {
        //TODO: MAKE THE REAL NAVIGATION
        LaunchedEffect(
            Unit
        ) {
            delay(500)
            navigator.navigate(HOME.name)
        }
    }

}