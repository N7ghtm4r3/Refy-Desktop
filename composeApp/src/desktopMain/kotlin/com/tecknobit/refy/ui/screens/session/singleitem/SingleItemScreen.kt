package com.tecknobit.refy.ui.screens.session.singleitem

import androidx.annotation.CallSuper
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.refy.ui.screens.session.RefyItemBaseScreen
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.utilities.*
import com.tecknobit.refy.utilities.RefyLinkUtilities
import com.tecknobit.refycore.records.RefyItem
import com.tecknobit.refycore.records.links.RefyLink
import displayFontFamily
import navigator
import org.jetbrains.compose.resources.StringResource

/**
 * The **SingleItemScreen** class is useful to give the base behavior of a single [RefyItem]'s
 * screen to correctly display and manage it
 *
 * @param items: the items list
 * @param invalidMessage: the resource identifier of the invalid message to display when the item is
 * not valid or not found in [items] list
 * @param itemId: the identifier of the item
 *
 * @param T: the [RefyItem] of the current activity displayed
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 * @see RefyItemBaseScreen
 * @see RefyLinkUtilities
 */
@Structure
abstract class SingleItemScreen <T : RefyItem> (
    items: List<T>,
    invalidMessage: StringResource,
    itemId: String?
): RefyItemBaseScreen<T> (
    items = items,
    invalidMessage = invalidMessage,
    itemId = itemId
), RefyLinkUtilities<RefyLink> {

    /**
     * *iconsColor* -> the color of the icons
     */
    protected var iconsColor: Color = Color.Red

    /**
     * *hasTeams* -> whether the item is shared with teams
     */
    protected var hasTeams: Boolean = true

    /**
     * *activityColorTheme* -> the color theme for the activity
     */
    protected var activityColorTheme: Color = Color.Red

    /**
     * Function to prepare the view initializing the [item] by invoking the [initItemFromIntent]
     * method
     *
     * No-any params required
     */
    @CallSuper
    protected open fun prepareView() {
        initItemFromScreen()
    }

    /**
     * Function to correctly display the content managing the different scenarios such invalid item,
     * server offline and account deleted
     *
     * @param validItemUi: the content of the view to display in a normal scenario
     */
    @Composable
    @NonRestartableComposable
    protected fun ContentView(
        validItemUi: @Composable () -> Unit
    ) {
        RefyTheme {
            if (!itemExists)
                InvalidItemUi()
            else {
                ManagedContent {
                    validItemUi.invoke()
                }
            }
        }
    }

    /**
     * Wrapper function to create a back navigation button to nav at the previous caller activity
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    protected fun NavButton() {
        iconsColor = LocalContentColor.current
        IconButton(
            onClick = { navigator.goBack() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
    }

    /**
     * Function to create an [RefyLink] card to display the details of that link and to give the rapid
     * actions such share, secure view, deleting it, etc
     *
     * @param link: the link to display
     * @param hideOptions: whether hide the rapid actions
     * @param removeAction: the action to execute when the user remove the link from a container
     */
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    @NonRestartableComposable
    protected fun RefyLinkContainerCard(
        link: RefyLink,
        hideOptions: Boolean = false,
        removeAction: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .combinedClickable(
                    onClick = {
                        openLink(
                            link = link
                        )
                    },
                    onDoubleClick = {
                        showLinkReference(
                            snackbarHostState = snackbarHostState,
                            link = link
                        )
                    },
                ),
            shape = RoundedCornerShape(
                size = 8.dp
            )
        ) {
            Column {
                if(hasTeams) {
                    TopBarDetails(
                        item = link
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = if (hasTeams)
                                5.dp
                            else
                                16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 5.dp
                        )
                ) {
                    Text(
                        text = link.title,
                        fontFamily = displayFontFamily,
                        fontSize = 25.sp,
                        fontStyle = AppTypography.titleMedium.fontStyle
                    )
                    ItemDescription(
                        description = link.description
                    )
                }
                OptionsBar(
                    options = {
                        if(!hideOptions) {
                            ShareButton(
                                link = link,
                                snackbarHostState = snackbarHostState
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Row {
                                ViewLinkReferenceButton(
                                    snackbarHostState = snackbarHostState,
                                    link = link
                                )
                                if(!hideOptions) {
                                    IconButton(
                                        onClick = removeAction
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    /**
     * Function to create the top bar section with the details of the [RefyLink.owner] if that item
     * is shared with teams, so [hasTeams] is *true*
     *
     * @param item: the item from get details
     * @param overlineColor: the color to use in the overline section content
     */
    @Composable
    @NonRestartableComposable
    protected fun TopBarDetails(
        item: RefyItem,
        overlineColor: Color = activityColorTheme
    ) {
        UserPlaque(
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                overlineColor = overlineColor
            ),
            profilePicSize = 45.dp,
            user = item.owner
        )
        LineDivider()
    }

}