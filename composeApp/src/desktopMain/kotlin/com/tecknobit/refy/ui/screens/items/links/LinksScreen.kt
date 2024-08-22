package com.tecknobit.refy.ui.screens.items.links

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.FolderCopy
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.ui.screens.items.ItemScreen
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import com.tecknobit.refy.utilities.*
import com.tecknobit.refy.viewmodels.links.LinksViewModel
import com.tecknobit.refycore.records.RefyItem
import com.tecknobit.refycore.records.links.RefyLink
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.add_link_to_collection
import refy.composeapp.generated.resources.add_link_to_team
import refy.composeapp.generated.resources.no_links_yet

/**
 * The **LinksScreen** class is useful to give the basic structure for a screen to display
 * the a [RefyLink]'s list
 *
 * @param viewModel: the view model used to execute this operation
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see Screen
 * @see SessionManager
 *
 * @param T: the type of the [RefyLink] between [RefyLink] and [CustomRefyLink]
 */
@Structure
abstract class LinksScreen <T : RefyLink> (
    val viewModel: LinksViewModel<T>
) : ItemScreen(), RefyLinkUtilities<T> {

    /**
     * *links* -> the list of the links to display
     */
    protected lateinit var links: List<T>

    protected fun fetchLinksList() {
        screenViewModel = viewModel
        viewModel.getLinks()
    }

    /**
     * Function to display the [links] list
     *
     * No-any params required
     */
    @Composable
    protected fun LinksList() {
        if(links.isEmpty()) {
            EmptyListUI(
                icon = Icons.Default.LinkOff,
                subText = stringResource(Res.string.no_links_yet)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(350.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = links,
                    key = { link -> link.id }
                ) { link ->
                    LinkCard(
                        link = link
                    )
                }
            }
        }
    }

    /**
     * Function to create a properly [Card] to display the link
     *
     * @param link: the link to display
     */
    @Composable
    @NonRestartableComposable
    abstract fun LinkCard(
        link: T
    )

    /**
     * Function to create a [Card] to display the [RefyLink]'s details
     *
     * @param link: the link to display
     * @param onClick: the action to execute when the card has been clicked
     * @param onLongClick: the action to execute when the card has been clicked for a long period
     * @param showCompleteOptionsBar: whether show the complete options bar if the [localUser] is
     * authorized
     */
    @Composable
    @NonRestartableComposable
    fun RefyLinkCard(
        link: T,
        onClick: () -> Unit,
        onLongClick: () -> Unit,
        showCompleteOptionsBar: Boolean = true
    ) {
        ItemCard(
            item = link,
            onClick = onClick,
            onDoubleClick = {
                showLinkReference(
                    snackbarHostState = snackbarHostState,
                    link = link
                )
            },
            onLongClick = onLongClick,
            title = link.title,
            description = link.description,
            teams = link.teams,
            optionsBar = {
                if(showCompleteOptionsBar) {
                    OptionsBar(
                        link = link
                    )
                } else {
                    OptionsBar {
                        ShareButton(
                            link = link,
                            snackbarHostState = snackbarHostState
                        )
                        Actions(
                            link = link,
                            userCanUpdate = true
                        )
                    }
                }
            }
        )
    }

    /**
     * Function to create an options bar for the card of the [RefyLink]
     *
     * @param link: the link to display
     */
    @Composable
    @NonRestartableComposable
    private fun OptionsBar(
        link: T
    ) {
        val addToTeam = remember { mutableStateOf(false) }
        val addToCollection = remember { mutableStateOf(false) }
        OptionsBar(
            options = {
                val userCanUpdate = link.canBeUpdatedByUser(localUser.userId)
                AnimatedVisibility(
                    visible = userCanUpdate,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row {
                        val collections = getItemRelations(
                            userList = localUser.getCollections(true),
                            currentAttachments = link.collections
                        )
                        OptionButton(
                            icon = Icons.Default.CreateNewFolder,
                            show = addToCollection,
                            visible = { collections.isNotEmpty() },
                            optionAction = {
                                AddLinkToCollections(
                                    show = addToCollection,
                                    availableCollection = collections,
                                    link = link
                                )
                            }
                        )
                        val teams = getItemRelations(
                            userList = localUser.getTeams(true),
                            currentAttachments = link.teams
                        )
                        OptionButton(
                            icon = Icons.Default.GroupAdd,
                            show = addToTeam,
                            visible = { teams.isNotEmpty() },
                            optionAction = {
                                AddLinkToTeams(
                                    show = addToTeam,
                                    availableTeams = teams,
                                    link = link
                                )
                            }
                        )
                        ShareButton(
                            link = link,
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
                Actions(
                    link = link,
                    userCanUpdate = userCanUpdate
                )
            }
        )
    }

    /**
     * Function to create the actions [Row] to operate with the link of the card
     *
     * @param link: the link to display
     * @param userCanUpdate: whether the user can update the link
     */
    @Composable
    @NonRestartableComposable
    private fun Actions(
        link: T,
        userCanUpdate: Boolean
    ) {
        val deleteLink = remember { mutableStateOf(false) }
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
                if (userCanUpdate) {
                    DeleteLinkButton(
                        viewModel = viewModel,
                        deleteLink = deleteLink,
                        link = link,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    /**
     * Function to add the link to collections
     *
     * @param show: whether show the [EquinoxAlertDialog] where is possible chose the collections
     * @param availableCollection: the available collections where add the link
     * @param link: the link to add
     */
    @Composable
    @NonRestartableComposable
    private fun AddLinkToCollections(
        show: MutableState<Boolean>,
        availableCollection: List<RefyItem>,
        link: T
    ) {
        AddItemToContainer(
            show = show,
            viewModel = viewModel,
            icon = Icons.Default.FolderCopy,
            availableItems = availableCollection,
            title = Res.string.add_link_to_collection,
            confirmAction = { ids ->
                viewModel.addLinkToCollections(
                    link = link,
                    collections = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    /**
     * Function to share the link to teams
     *
     * @param show: whether show the [EquinoxAlertDialog] where is possible chose the teams
     * @param availableTeams: the available teams where share the link
     * @param link: the link to share
     */
    @Composable
    @NonRestartableComposable
    private fun AddLinkToTeams(
        show: MutableState<Boolean>,
        availableTeams: List<RefyItem>,
        link: T
    ) {
        AddItemToContainer(
            show = show,
            viewModel = viewModel,
            icon = Icons.Default.GroupAdd,
            availableItems = availableTeams,
            title = Res.string.add_link_to_team,
            confirmAction = { ids ->
                viewModel.addLinkToTeams(
                    link = link,
                    teams = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

}