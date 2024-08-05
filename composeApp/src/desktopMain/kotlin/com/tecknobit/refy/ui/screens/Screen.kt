package com.tecknobit.refy.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import navigator

abstract class Screen {

    protected lateinit var screenViewModel: EquinoxViewModel

    companion object {

        val snackbarHostState = SnackbarHostState()

    }

    enum class Routes {

        SPLASHSCREEN,

        HOME,

        CREATE_COLLECTION_SCREEN,

        COLLECTION_SCREEN,

        CREATE_TEAM_SCREEN,

        TEAM_SCREEN,

        PROFILE

    }

    @Composable
    abstract fun ShowContent()

    protected fun navToDedicatedItemScreen(
        itemId: String,
        destination: Routes
    ) {
        navigator.navigate("$destination/$itemId")
    }

}