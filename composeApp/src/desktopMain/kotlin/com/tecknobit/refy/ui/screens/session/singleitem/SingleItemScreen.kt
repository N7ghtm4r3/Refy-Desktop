@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.session.singleitem

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
import com.tecknobit.refycore.records.RefyItem
import com.tecknobit.refycore.records.links.RefyLink
import displayFontFamily
import navigator
import org.jetbrains.compose.resources.StringResource

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

    protected var iconsColor: Color = Color.Red

    protected var hasTeams: Boolean = true

    protected var activityColorTheme: Color = Color.Red

    protected open fun prepareView() {
        initItemFromScreen()
    }

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