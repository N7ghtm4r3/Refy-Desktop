@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.session.create

import androidx.annotation.CallSuper
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.tecknobit.equinox.environment.records.EquinoxUser.DEFAULT_PROFILE_PIC
import com.tecknobit.refy.ui.getCompleteMediaItemUrl
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import com.tecknobit.refy.utilities.DefaultPlaque
import com.tecknobit.refy.viewmodels.create.CreateTeamViewModel
import com.tecknobit.refycore.records.Team
import imageLoader
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.invalid_team
import refy.composeapp.generated.resources.members
import refy.composeapp.generated.resources.team_name

/**
 * The **CreateTeamActivity** class is useful to create or edit a [Team]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 * @see RefyItemBaseScreen
 * @see CreateScreen
 */
class CreateTeamScreen(
    teamId: String?
): CreateScreen<Team, CreateTeamViewModel>(
    items = localUser.getTeams(true),
    invalidMessage = Res.string.invalid_team,
    itemId = teamId
) {

    /**
     * **fileType** -> list of allowed image types
     */
    private val fileType = listOf("jpg", "png", "jpeg")

    /**
     * *pickProfilePic* -> the flag to choose the logo picture for the team
     */
    private lateinit var pickProfilePic: MutableState<Boolean>

    init {
        viewModel = CreateTeamViewModel(
            snackbarHostState = snackbarHostState
        )
        viewModel.fetchCurrentUsers()
    }

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
    @CallSuper
    @Composable
    override fun ScreenContent() {
        super.ScreenContent()
        PickLogo()
        viewModel.logoPic = remember {
            mutableStateOf(
                if(itemExists)
                    item!!.logoPic
                else
                    DEFAULT_PROFILE_PIC
            )
        }
        ScaffoldContent(
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            placeholder = Res.string.team_name,
            leftContent = {
                if(viewModel.logoPic.value.isNotEmpty())
                    LogoSet()
                else
                    LogoNotSet()
            },
            rightContent = {
                MembersSection()
            }
        )
    }

    /**
     * Function to display the logo picture of the team
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun LogoSet() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    all = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val shape = CircleShape
            AsyncImage(
                modifier = Modifier
                    .size(225.dp)
                    .clip(
                        shape = shape
                    )
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = shape
                    )
                    .clickable { pickProfilePic.value = true },
                imageLoader = imageLoader,
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(
                        if (itemExists && viewModel.logoPic.value.contains(item!!.id)) {
                            getCompleteMediaItemUrl(
                                relativeMediaUrl = viewModel.logoPic.value
                            )
                        } else
                            viewModel.logoPic.value
                    )
                    .crossfade(enable = true)
                    .crossfade(500)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource("logo.png")
            )
        }
    }

    /**
     * Function to display the section to choose the logo picture for the team
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun LogoNotSet() {
        val stroke = Stroke(
            width = 4f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
        val color = MaterialTheme.colorScheme.primary
        Column (
            modifier = Modifier
                .size(225.dp)
                .padding(
                    all = 16.dp
                )
                .drawBehind {
                    drawCircle(
                        color = color,
                        style = stroke
                    )
                }
                .clip(CircleShape)
                .clickable { pickProfilePic.value = true },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(100.dp),
                imageVector = Icons.Default.ImageSearch,
                contentDescription = null,
                tint = color
            )
        }
    }

    /**
     * Function to pick the logo picture from the team
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun PickLogo() {
        val profilePic = remember { mutableStateOf(localUser.profilePic) }
        pickProfilePic = remember { mutableStateOf(false) }
        FilePicker(
            show = pickProfilePic.value,
            fileExtensions = fileType
        ) { profilePicPath ->
            if(profilePicPath != null) {
                profilePic.value = profilePicPath.path
                viewModel.logoPic.value = profilePic.value
            }
        }
    }

    /**
     * Function to display the section to choose the members of the team
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun MembersSection() {
        val keyboardController = LocalSoftwareKeyboardController.current
        val currentUsers = viewModel.potentialMembers.collectAsState().value
        CustomSection(
            modifier = Modifier
                .padding(
                    end = 16.dp
                ),
            header = Res.string.members
        ) {
            items(
                items = currentUsers,
                key = { member -> member.id }
            ) { member ->
                val checked = remember { mutableStateOf(viewModel.itemDedicatedList.contains(member.id)) }
                DefaultPlaque(
                    profilePic = member.profilePic,
                    completeName = member.completeName,
                    tagName = member.tagName,
                    trailingContent = {
                        ItemCheckbox(
                            checked = checked,
                            keyboardController = keyboardController,
                            itemId = member.id
                        )
                    }
                )
                HorizontalDivider()
            }
        }
    }

    /**
     * Function to check whether the current item can be saved because all the details has been
     * correctly filled
     *
     * No-any params required
     * @return whether the item can be saved as boolean
     */
    override fun canBeSaved(): Boolean {
        return super.canBeSaved() && viewModel.logoPic.value.isNotEmpty()
    }

}
