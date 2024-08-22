package com.tecknobit.refy.ui.screens.session

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.refy.helpers.NavigationHelper
import com.tecknobit.refy.helpers.NavigationHelper.Companion.activeTab
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.ui.screens.Screen.Routes.PROFILE
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.utilities.Logo
import displayFontFamily
import navigator
import org.jetbrains.compose.resources.stringResource

/**
 * The **Home** class is the screen where the user can navigate between his/her links, collections
 * teams and custom links
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 */
class Home : Screen() {

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
    @Composable
    override fun ShowContent() {
        Scaffold (
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        activeTab.value.onFabClick(activeTab.value.screen)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            },
            bottomBar = { NavigationHelper.getInstance().BottomNavigationBar() }
        ) { paddingValues ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        bottom = paddingValues.calculateBottomPadding()
                    ),
            ) {
                Row (
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(activeTab.value.name),
                        fontFamily = displayFontFamily,
                        style = AppTypography.titleLarge,
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Column (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Logo(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                ),
                            picSize = 75.dp,
                            picUrl = localUser.profilePic,
                            onClick = { navigator.navigate(PROFILE.name) }
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier
                        .padding(
                            top = 10.dp
                        ),
                )
                Column (
                    modifier = Modifier
                        .padding(
                            all = 16.dp
                        ),
                    content = { activeTab.value.content.invoke(this, activeTab.value.screen) }
                )
            }
        }
    }

}