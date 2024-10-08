@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.session.singleitem

import androidx.annotation.CallSuper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import com.tecknobit.refy.ui.screens.session.RefyItemBaseScreen
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.utilities.ExpandTeamMembers
import com.tecknobit.refy.utilities.LinksCollectionUtilities
import com.tecknobit.refy.utilities.RefyLinkUtilities
import com.tecknobit.refy.utilities.getItemRelations
import com.tecknobit.refy.viewmodels.collections.CollectionScreenViewModel
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.links.RefyLink
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.invalid_collection

/**
 * The **CollectionScreen** class is useful to display a [LinksCollection]'s details and manage
 * that collection
 *
 * @param collectionId: the identifier of the collection
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 * @see RefyItemBaseScreen
 * @see SingleItemScreen
 * @see RefyLinkUtilities
 * @see LinksCollectionUtilities
 */
class CollectionScreen(
    collectionId: String
): SingleItemScreen<LinksCollection>(
    items = localUser.getCollections(false),
    invalidMessage = Res.string.invalid_collection,
    itemId = collectionId
), RefyLinkUtilities<RefyLink>, LinksCollectionUtilities {

    /**
     * *viewModel* -> the support view model to manage the requests to the backend
     */
    private lateinit var viewModel: CollectionScreenViewModel

    init {
        prepareView()
    }

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
    @Composable
    override fun ShowContent() {
        LifecycleManager(
            onDispose = {
                viewModel.reset()
                viewModel.suspendRefresher()
            }
        )
        ContentView {
            item = viewModel.collection.collectAsState().value
            activityColorTheme = item!!.color.toColor()
            hasTeams = item!!.hasTeams()
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                topBar = {
                    LargeTopAppBar(
                        navigationIcon = { NavButton() },
                        title = {
                            Text(
                                text = item!!.title
                            )
                        },
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = activityColorTheme
                        ),
                        actions = {
                            AnimatedVisibility(
                                visible = item!!.canBeUpdatedByUser(localUser.userId),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Row {
                                    val links = getItemRelations(
                                        userList = localUser.getLinks(true),
                                        currentAttachments = item!!.links
                                    )
                                    val addLinks = remember { mutableStateOf(false) }
                                    AddLinksButton(
                                        viewModel = viewModel,
                                        show = addLinks,
                                        links = links,
                                        collection = item!!,
                                        tint = iconsColor
                                    )
                                    val teams = getItemRelations(
                                        userList = localUser.getTeams(true),
                                        currentAttachments = item!!.teams
                                    )
                                    val addTeams = remember { mutableStateOf(false) }
                                    AddTeamsButton(
                                        viewModel = viewModel,
                                        show = addTeams,
                                        teams = teams,
                                        collection = item!!,
                                        tint = iconsColor
                                    )
                                    val deleteCollection = remember { mutableStateOf(false) }
                                    DeleteCollectionButton(
                                        goBack = true,
                                        viewModel = viewModel,
                                        deleteCollection = deleteCollection,
                                        collection = item!!,
                                        tint = iconsColor
                                    )
                                }
                            }
                        }
                    )
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = hasTeams,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        val expandTeams = remember { mutableStateOf(false) }
                        FloatingActionButton(
                            onClick = { expandTeams.value = true },
                            containerColor = activityColorTheme
                        ) {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = null
                            )
                        }
                        ExpandTeamMembers(
                            viewModel = viewModel,
                            show = expandTeams,
                            teams = item!!.teams
                        )
                    }
                }
            ) { paddingValues ->
                val userCanUpdate = item!!.canBeUpdatedByUser(localUser.userId)
                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(
                            top = paddingValues.calculateTopPadding() + 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                    columns = GridCells.Adaptive(350.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = item!!.links,
                        key = { link -> link.id }
                    ) { link ->
                        RefyLinkContainerCard(
                            link = link,
                            hideOptions = !userCanUpdate,
                            removeAction = {
                                viewModel.removeLinkFromCollection(
                                    link = link
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    /**
     * Function to prepare the view initializing the [item] by invoking the [initItemFromIntent]
     * method, will be initialized the [viewModel] and started its refreshing routine to refresh the
     * [item]
     *
     * No-any params required
     */
    @CallSuper
    override fun prepareView() {
        super.prepareView()
        if (itemExists) {
            viewModel = CollectionScreenViewModel(
                snackbarHostState = snackbarHostState,
                initialCollection = item!!
            )
            viewModel.setActiveContext(this::class.java)
            viewModel.refreshCollection()
        }
    }

}