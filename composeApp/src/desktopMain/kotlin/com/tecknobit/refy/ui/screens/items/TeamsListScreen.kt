@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.tecknobit.refy.ui.screens.items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.refy.ui.getCompleteMediaItemUrl
import com.tecknobit.refy.ui.screens.Screen.Routes.CREATE_TEAM_SCREEN
import com.tecknobit.refy.ui.screens.Screen.Routes.TEAM_SCREEN
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.utilities.*
import com.tecknobit.refy.ui.viewmodels.teams.TeamsListViewModel
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.links.RefyLink
import displayFontFamily
import navigator
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.you_re_not_on_any_team

class TeamsListScreen: ItemScreen(), TeamsUtilities, RefyLinkUtilities<RefyLink> {

    private val viewModel = TeamsListViewModel()

    private lateinit var teams: List<Team>

    @Composable
    override fun ShowContent() {
        val context = this::class.java
        currentScreenContext = context
        viewModel.setActiveContext(context)
        screenViewModel = viewModel
        viewModel.getTeams()
        teams = viewModel.teams.collectAsState().value
        if(teams.isEmpty()) {
            EmptyListUI(
                icon = Icons.Default.GroupOff,
                subText = stringResource(Res.string.you_re_not_on_any_team)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(350.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = teams,
                    key = { team -> team.id }
                ) { team ->
                    TeamCard(
                        team = team
                    )
                }
            }
        }
    }

    override fun executeFabAction() {
        navigator.navigate(CREATE_TEAM_SCREEN.name)
    }

    @Composable
    @NonRestartableComposable
    private fun TeamCard(
        team: Team
    ) {
        val isAdmin = team.isAdmin(localUser.userId)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .combinedClickable(
                    onClick = {
                        navToDedicatedItemScreen(
                            itemId = team.id,
                            destination = TEAM_SCREEN
                        )
                    },
                    onLongClick = if (isAdmin) {
                        {
                            navToDedicatedItemScreen(
                                itemId = team.id,
                                destination = CREATE_TEAM_SCREEN
                            )
                        }
                    } else
                        null
                ),
            shape = RoundedCornerShape(
                size = 8.dp
            )
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 5.dp,
                            bottom = 5.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    TeamDetails(
                        team = team
                    )
                }
                OptionsBar(
                    isMaintainer = isAdmin,
                    team = team
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun TeamDetails(
        team: Team
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            leadingContent = {
                Logo(
                    picSize = 115.dp,
                    shape = RoundedCornerShape(
                        size = 10.dp
                    ),
                    addShadow = true,
                    picUrl = getCompleteMediaItemUrl(
                        relativeMediaUrl = team.logoPic
                    )
                )
            },
            overlineContent = {
                PicturesRow(
                    pictures = {
                        val profiles = mutableListOf<String>()
                        team.members.forEach { member ->
                            profiles.add(member.profilePic)
                        }
                        profiles
                    },
                    pictureSize = 20.dp
                )
            },
            headlineContent = {
                Text(
                    modifier = Modifier
                        .padding(
                            bottom = 5.dp
                        ),
                    text = team.title,
                    fontSize = 20.sp,
                    fontFamily = displayFontFamily,
                    fontStyle = AppTypography.titleMedium.fontStyle
                )
            },
            supportingContent = {
                ItemDescription(
                    description = team.description
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun OptionsBar(
        isMaintainer: Boolean,
        team: Team
    ) {
        val addLinks = remember { mutableStateOf(false) }
        val addCollections = remember { mutableStateOf(false) }
        val leaveTeam = remember { mutableStateOf(false) }
        val deleteTeam = remember { mutableStateOf(false) }
        OptionsBar(
            options = {
                AnimatedVisibility(
                    visible = isMaintainer,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row {
                        val iconsColor = LocalContentColor.current
                        val links = getItemRelations(
                            userList = localUser.getLinks(true),
                            currentAttachments = team.links
                        )
                        AddLinksButton(
                            viewModel = viewModel,
                            show = addLinks,
                            links = links,
                            team = team,
                            tint = iconsColor
                        )
                        val collections = getItemRelations(
                            userList = localUser.getCollections(true),
                            currentAttachments = team.collections
                        )
                        AddCollectionsButton(
                            viewModel = viewModel,
                            show = addCollections,
                            collections = collections,
                            team = team,
                            tint = iconsColor
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Row {
                        if (team.isTheAuthor(localUser.userId)) {
                            DeleteTeamButton(
                                viewModel = viewModel,
                                deleteTeam = deleteTeam,
                                team = team,
                                tint = MaterialTheme.colorScheme.error
                            )
                        } else {
                            LeaveTeamButton(
                                viewModel = viewModel,
                                leaveTeam = leaveTeam,
                                team = team,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        )
    }

}