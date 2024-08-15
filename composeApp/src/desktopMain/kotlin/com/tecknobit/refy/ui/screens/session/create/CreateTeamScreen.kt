@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.session.create

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.tecknobit.equinox.environment.records.EquinoxUser.DEFAULT_PROFILE_PIC
import com.tecknobit.refy.ui.getCompleteMediaItemUrl
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import com.tecknobit.refy.ui.utilities.DefaultPlaque
import com.tecknobit.refy.ui.viewmodels.create.CreateTeamViewModel
import com.tecknobit.refycore.records.Team
import imageLoader
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.invalid_team
import refy.composeapp.generated.resources.members
import refy.composeapp.generated.resources.team_name

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

    private lateinit var pickProfilePic: MutableState<Boolean>

    init {
        viewModel = CreateTeamViewModel(
            snackbarHostState = snackbarHostState
        )
        viewModel.fetchCurrentUsers()
    }

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
            extraContent = {
                if(viewModel.logoPic.value.isNotEmpty())
                    LogoSet()
                else
                    LogoNotSet()
            },
            customContent = {
                MembersSection()
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun LogoSet() {
        val shape = RoundedCornerShape(
            size = 5.dp
        )
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
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
                //.error() //TODO: TO SET THE ERROR IMAGE CORRECTLY
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }

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
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .height(150.dp)
                .drawBehind {
                    drawRoundRect(
                        color = color,
                        style = stroke,
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )
                }
                .clip(
                    RoundedCornerShape(
                        size = 5.dp
                    )
                )
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

    @Composable
    @NonRestartableComposable
    private fun MembersSection() {
        val keyboardController = LocalSoftwareKeyboardController.current
        val currentUsers = viewModel.potentialMembers.collectAsState().value
        CustomSection(
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

    override fun canBeSaved(): Boolean {
        return super.canBeSaved() && viewModel.logoPic.value.isNotEmpty()
    }

}
