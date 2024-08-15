package com.tecknobit.refy.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.*
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tecknobit.apimanager.apis.ConsolePainter
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import navigator

abstract class Screen {

    protected lateinit var screenViewModel: EquinoxViewModel

    protected val context = this::class.java

    companion object {

        val snackbarHostState = SnackbarHostState()

    }

    enum class Routes {

        SPLASHSCREEN,

        CONNECT_SCREEN,

        HOME,

        CREATE_COLLECTION_SCREEN,

        COLLECTION_SCREEN,

        CREATE_TEAM_SCREEN,

        TEAM_SCREEN,

        CREATE_CUSTOM_LINK_SCREEN,

        CUSTOM_LINK_SCREEN,

        PROFILE

    }

    @Composable
    abstract fun ShowContent()

    //TODO: TO REMOVE
    private val painter = ConsolePainter()

    @Composable
    protected fun LifecycleManager(
        lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
        onCreate: () -> Unit = {
            painter.printBold(Lifecycle.Event.ON_CREATE, ConsolePainter.ANSIColor.CYAN)
        },
        onStart: () -> Unit = {
            painter.printBold(Lifecycle.Event.ON_START, ConsolePainter.ANSIColor.GREEN)
        },
        onResume: () -> Unit = {
            painter.printBold(Lifecycle.Event.ON_RESUME, ConsolePainter.ANSIColor.BLUE)
        },
        onPause: () -> Unit = {
            painter.printBold(Lifecycle.Event.ON_PAUSE, ConsolePainter.ANSIColor.YELLOW)
        },
        onStop: () -> Unit = {
            painter.printBold(Lifecycle.Event.ON_STOP, ConsolePainter.ANSIColor.RED)
        },
        onDestroy: () -> Unit = {
            painter.printBold(Lifecycle.Event.ON_DESTROY, ConsolePainter.ANSIColor.BRIGHT_RED)
        },
        onAny: () -> Unit = {
            painter.printBold(Lifecycle.Event.ON_ANY, ConsolePainter.ANSIColor.GRAY)
        },
        onDispose: () -> Unit = {}
    ) {
        DisposableEffect(lifecycleOwner) {
            val lifecycle = lifecycleOwner.lifecycle
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    ON_CREATE -> onCreate.invoke()
                    ON_START -> onStart.invoke()
                    ON_RESUME -> onResume.invoke()
                    ON_PAUSE -> onPause.invoke()
                    ON_STOP -> onStop.invoke()
                    ON_DESTROY -> onDestroy.invoke()
                    else -> onAny.invoke()
                }
            }
            lifecycle.addObserver(observer)
            onDispose {
                onDispose.invoke()
                lifecycle.removeObserver(observer)
            }
        }
    }

    protected fun navToDedicatedItemScreen(
        itemId: String,
        destination: Routes
    ) {
        navigator.navigate("$destination/$itemId")
    }

}