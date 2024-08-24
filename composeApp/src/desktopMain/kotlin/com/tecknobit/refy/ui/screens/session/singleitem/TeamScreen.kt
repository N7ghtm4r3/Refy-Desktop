@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.session.singleitem

import androidx.annotation.CallSuper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.refy.ui.getCompleteMediaItemUrl
import com.tecknobit.refy.ui.screens.Screen.Routes.CREATE_COLLECTION_SCREEN
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import com.tecknobit.refy.ui.screens.session.RefyItemBaseScreen
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.utilities.*
import com.tecknobit.refy.viewmodels.teams.TeamScreenViewModel
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.links.RefyLink
import displayFontFamily
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.collections
import refy.composeapp.generated.resources.invalid_team
import refy.composeapp.generated.resources.links

/**
 * The **TeamScreen** class is useful to display a [Team]'s details and manage
 * that team
 *
 * @param teamId: the identifier of the team
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 * @see RefyItemBaseScreen
 * @see SingleItemScreen
 * @see RefyLinkUtilities
 * @see TeamsUtilities
 */
class TeamScreen(
    teamId: String
) : SingleItemScreen<Team>(
    items = localUser.getTeams(false),
    invalidMessage = Res.string.invalid_team,
    itemId = teamId
), RefyLinkUtilities<RefyLink>, TeamsUtilities {

    /**
     * *viewModel* -> the support view model to manage the requests to the backend
     */
    private lateinit var viewModel: TeamScreenViewModel

    /**
     * *linksExpanded* -> whether the links section is expanded
     */
    private lateinit var linksExpanded: MutableState<Boolean>

    /**
     * *membersExpanded* -> whether the members section is expanded
     */
    private lateinit var membersExpanded: MutableState<Boolean>

    /**
     * *isUserAdmin* -> whether the current [localUser] is an admin of the current team ([item])
     */
    private var isUserAdmin: Boolean = false

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
            item = viewModel.team.collectAsState().value
            activityColorTheme = MaterialTheme.colorScheme.primaryContainer
            isUserAdmin = item!!.isAdmin(localUser.userId)
            linksExpanded = remember { mutableStateOf(item!!.links.isNotEmpty()) }
            membersExpanded = remember { mutableStateOf(false) }
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                topBar = {
                    LargeTopAppBar(
                        navigationIcon = { NavButton() },
                        title = {
                            Box {
                                Logo(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart),
                                    picSize = 65.dp,
                                    picUrl = getCompleteMediaItemUrl(
                                        relativeMediaUrl = item!!.logoPic
                                    )
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.Bottom
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .align(Alignment.End)
                                            .padding(
                                                start = 35.dp
                                            ),
                                        text = item!!.title,
                                        fontSize = 24.sp
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = activityColorTheme
                        ),
                        actions = {
                            AnimatedVisibility(
                                visible = isUserAdmin,
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
                                        team = item!!,
                                        tint = iconsColor
                                    )
                                    val addCollections = remember { mutableStateOf(false) }
                                    val collections = getItemRelations(
                                        userList = localUser.getCollections(true),
                                        currentAttachments = item!!.collections
                                    )
                                    AddCollectionsButton(
                                        viewModel = viewModel,
                                        show = addCollections,
                                        collections = collections,
                                        team = item!!,
                                        tint = iconsColor
                                    )
                                }
                            }
                            if (item!!.isTheAuthor(localUser.userId)) {
                                val deleteTeam = remember { mutableStateOf(false) }
                                DeleteTeamButton(
                                    goBack = true,
                                    viewModel = viewModel,
                                    deleteTeam = deleteTeam,
                                    team = item!!,
                                    tint = iconsColor
                                )
                            } else {
                                val leaveTeam = remember { mutableStateOf(false) }
                                LeaveTeamButton(
                                    goBack = true,
                                    viewModel = viewModel,
                                    leaveTeam = leaveTeam,
                                    team = item!!,
                                    tint = iconsColor
                                )
                            }
                        }
                    )
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = item!!.members.size > 1,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        TeamMembers()
                        FloatingActionButton(
                            onClick = { membersExpanded.value = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = null
                            )
                        }
                    }
                }
            ) { paddingValues ->
                TeamContent(
                    paddingValues = paddingValues
                )
            }
        }
    }

    /**
     * Function to display the content of the team
     *
     * @param paddingValues: the padding to use to correctly display the content
     */
    @Composable
    @NonRestartableComposable
    private fun TeamContent(
        paddingValues: PaddingValues
    ) {
        Column {
            ItemDescription(
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                description = item!!.description
            )
            ItemsSection(
                isSectionVisible = item!!.links.isNotEmpty(),
                header = Res.string.links,
                visible = linksExpanded.value
            ) {
                items(
                    items = item!!.links,
                    key = { link -> link.id }
                ) { link ->
                    RefyLinkContainerCard(
                        link = link,
                        hideOptions = !isUserAdmin,
                        removeAction = {
                            viewModel.removeLinkFromTeam(
                                link = link
                            )
                        }
                    )
                }
            }
            ItemsSection(
                isSectionVisible = item!!.collections.isNotEmpty(),
                header = Res.string.collections,
                visible = !linksExpanded.value
            ) {
                items(
                    items = item!!.collections,
                    key = { collection -> collection.id }
                ) { collection ->
                    LinksCollectionTeamCard(
                        collection = collection
                    )
                }
            }
        }
    }

    /**
     * Function to display an items section such links or collections section shared with the team
     *
     * @param isSectionVisible: whether the section can be visible, so the [items] is not empty
     * @param header: the resource identifier of the header text
     * @param visible: whether the section is currently visible
     * @param items: the items list to display
     */
    @Composable
    @NonRestartableComposable
    private fun ItemsSection(
        isSectionVisible: Boolean,
        header: StringResource,
        visible: Boolean,
        items: LazyGridScope.() -> Unit
    ) {
        if(isSectionVisible) {
            HorizontalDivider()
            ControlSectionHeader(
                header = header,
                iconCheck = visible
            )
            AnimatedVisibility(
                visible = visible
            ) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        bottom = 16.dp
                    ),
                    columns = GridCells.Adaptive(350.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items.invoke(this)
                }
            }
        }
    }

    /**
     * Function to create the control header to hide or unhidden an [ItemsSection]
     *
     * @param header: the resource identifier of the header text
     * @param iconCheck: the icon for the button to hide or unhidden the section
     */
    @Composable
    @NonRestartableComposable
    private fun ControlSectionHeader(
        header: StringResource,
        iconCheck: Boolean
    ) {
        Row (
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(header),
                fontFamily = displayFontFamily,
                style = AppTypography.titleLarge,
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(
                onClick = { linksExpanded.value = !linksExpanded.value }
            ) {
                Icon(
                    imageVector = if(iconCheck)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }
    }

    /**
     * Function to create an [LinksCollection] card to display the details of that collection and
     * to give the rapid actions such removing from the team
     *
     * @param collection: the collection to display
     */
    @Composable
    @NonRestartableComposable
    private fun LinksCollectionTeamCard(
        collection: LinksCollection
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .drawOneSideBorder(
                    width = 10.dp,
                    color = collection.color.toColor(),
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp
                    )
                ),
            shape = RoundedCornerShape(
                size = 8.dp
            ),
            onClick = {
                localUser.setCollections(item!!.collections)
                navToDedicatedItemScreen(
                    itemId = collection.id,
                    destination = CREATE_COLLECTION_SCREEN
                )
            }
        ) {
            Column {
                TopBarDetails(
                    item = collection,
                    overlineColor = collection.color.toColor()
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 5.dp,
                            start = 21.dp,
                            end = 16.dp,
                            bottom = 5.dp
                        )
                ) {
                    Text(
                        text = collection.title,
                        fontFamily = displayFontFamily,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontStyle = AppTypography.titleMedium.fontStyle
                    )
                    ItemDescription(
                        description = collection.description
                    )
                }
                AnimatedVisibility(
                    visible = isUserAdmin,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    OptionsBar(
                        options = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                IconButton(
                                    onClick = {
                                        viewModel.removeCollectionFromTeam(
                                            collection = collection
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    /**
     * Function to display the members of the team displayed and, if authorized, removing or change
     * the role members
     *
     * No-any params required
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    @NonRestartableComposable
    private fun TeamMembers() {
        if(membersExpanded.value) {
            ModalBottomSheet(
                onDismissRequest = {
                    membersExpanded.value = false
                }
            ) {
                LazyColumn {
                    items(
                        items = item!!.members,
                        key = { member -> member.id }
                    ) { member ->
                        TeamMemberPlaque(
                            team = item!!,
                            member = member,
                            viewModel = viewModel
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
            viewModel = TeamScreenViewModel(
                snackbarHostState = snackbarHostState,
                initialTeam = item!!
            )
            viewModel.setActiveContext(this::class.java)
            viewModel.refreshTeam()
        }
    }

}