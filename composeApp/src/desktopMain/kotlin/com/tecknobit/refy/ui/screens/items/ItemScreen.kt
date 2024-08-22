package com.tecknobit.refy.ui.screens.items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.refy.ui.getCompleteMediaItemUrl
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.utilities.ExpandTeamMembers
import com.tecknobit.refy.ui.utilities.ItemDescription
import com.tecknobit.refy.ui.utilities.drawOneSideBorder
import com.tecknobit.refy.ui.utilities.isItemOwner
import com.tecknobit.refycore.records.RefyItem
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.MAX_TEAMS_DISPLAYED
import displayFontFamily
import imageLoader

/**
 * The **ItemScreen** class is useful to give the basic structure for a Refy's screen to display
 * the [RefyItem]'s list
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see Screen
 */
@Structure
abstract class ItemScreen : Screen() {

    /**
     * Function to execute the fab action previously set
     *
     * No-any params required
     */
    abstract fun executeFabAction()

    /**
     * Function to restart the refresher of the screen displayed
     *
     * No-any params required
     */
    fun restartScreenRefreshing() {
        screenViewModel.setActiveContext(context)
        screenViewModel.restartRefresher()
    }

    /**
     * Function to suspend the refresher of the screen displayed
     *
     * No-any params required
     */
    fun suspendScreenRefreshing() {
        screenViewModel.suspendRefresher()
    }

    /**
     * Function to create a [Card] to display the [RefyItem]'s details
     *
     * @param item: the item to display
     * @param borderColor: the color to apply to one border
     * @param onClick: the action to execute when the card has been clicked
     * @param onDoubleClick: the action to execute when the card has been clicked twice
     * @param onLongClick: the action to execute when the card has been clicked for a long period
     * @param title: the title of the item
     * @param description: the description of the item
     * @param teams: the teams where the item is shared
     * @param optionsBar: the options bar available for the item
     */
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    @NonRestartableComposable
    protected fun ItemCard(
        item: RefyItem,
        borderColor: Color? = null,
        onClick: () -> Unit,
        onDoubleClick: (() -> Unit)? = null,
        onLongClick: () -> Unit,
        title: String,
        description: String?,
        teams: List<Team>,
        optionsBar: @Composable () -> Unit
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onDoubleClick,
                onLongClick = if (isItemOwner(item))
                    onLongClick
                else
                    null
            )
        Card(
            modifier = if(borderColor != null)
                modifier.drawOneSideBorder(
                    width = 10.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp
                    )
                )
            else
                modifier,
            shape = RoundedCornerShape(
                size = 8.dp
            )
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 16.dp,
                            start = if (borderColor == null)
                                16.dp
                            else
                                21.dp,
                            end = 16.dp,
                            bottom = 5.dp
                        )
                ) {
                    Text(
                        text = title,
                        fontFamily = displayFontFamily,
                        fontSize = 25.sp,
                        fontStyle = AppTypography.titleMedium.fontStyle
                    )
                    ItemDescription(
                        description = description
                    )
                    if(teams.isNotEmpty()) {
                        TeamSections(
                            teams = teams
                        )
                    }
                }
                optionsBar.invoke()
            }
        }
    }

    /**
     * Function to display the section relating the teams where the item is shared
     *
     * @param teams: the teams where the item is shared
     */
    @Composable
    @NonRestartableComposable
    private fun TeamSections(
        teams: List<Team>
    ) {
        val expandTeamMembers = remember { mutableStateOf(false) }
        LazyRow(
            modifier = Modifier
                .padding(
                    top = 5.dp,
                    bottom = 5.dp
                )
        ) {
            item {
                ExpandTeamMembers(
                    viewModel = screenViewModel,
                    show = expandTeamMembers,
                    teams = teams
                )
                PicturesRow(
                    pictures = {
                        val profiles = mutableListOf<String>()
                        teams.forEach { team ->
                            profiles.add(team.logoPic)
                        }
                        profiles
                    },
                    onClick = { expandTeamMembers.value = true }
                )
            }
        }
    }

    /**
     * Function to create a [Card] to display the [RefyItem]'s details
     *
     * @param onClick: the action to execute when the card has been clicked
     * @param pictures: the pictures to display in a row
     * @param pictureSize: the size to apply to the pictures
     */
    @Composable
    @NonRestartableComposable
    protected fun PicturesRow(
        onClick: (() -> Unit)? = null,
        pictures: () -> List<String>,
        pictureSize: Dp = 25.dp
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    enabled = onClick != null,
                    onClick = {
                        onClick?.invoke()
                    }
                )
        ) {
            pictures.invoke().forEachIndexed { index, picture ->
                if(index == MAX_TEAMS_DISPLAYED)
                    return@forEachIndexed
                AsyncImage(
                    modifier = Modifier
                        .padding(
                            start = index * 15.dp
                        )
                        .clip(CircleShape)
                        .size(pictureSize),
                    imageLoader = imageLoader,
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(
                            getCompleteMediaItemUrl(
                                relativeMediaUrl = picture
                            )
                        )
                        .crossfade(enable = true)
                        .crossfade(500)
                        //.error() //TODO: TO SET THE ERROR IMAGE CORRECTLY
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }

}