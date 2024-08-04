package com.tecknobit.refy.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.utilities.ExpandTeamMembers
import com.tecknobit.refy.ui.utilities.ItemDescription
import com.tecknobit.refy.ui.utilities.drawOneSideBorder
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.MAX_TEAMS_DISPLAYED
import displayFontFamily
import imageLoader

abstract class Screen {

    protected lateinit var screenViewModel: EquinoxViewModel

    companion object {

        val snackbarHostState = SnackbarHostState()

    }

    enum class Routes {

        SPLASHSCREEN,

        HOME

    }

    @Composable
    abstract fun ShowContent()

}