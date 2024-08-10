package com.tecknobit.refy.ui.utilities

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GroupRemove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bodyFontFamily
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material.RichText
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.user
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.viewmodels.teams.TeamActivityViewModel
import com.tecknobit.refycore.records.RefyItem
import com.tecknobit.refycore.records.RefyUser
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.Team.RefyTeamMember
import com.tecknobit.refycore.records.Team.RefyTeamMember.TeamRole
import com.tecknobit.refycore.records.Team.RefyTeamMember.TeamRole.ADMIN
import displayFontFamily
import imageLoader
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.add

@Composable
@NonRestartableComposable
fun OptionsBar(
    options: @Composable RowScope.() -> Unit
) {
    LineDivider()
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        content = options
    )
}

@Composable
@NonRestartableComposable
fun LineDivider() {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    HorizontalDivider(
        color = if(isSystemInDarkTheme)
            MaterialTheme.colorScheme.tertiary
        else
            MaterialTheme.colorScheme.outlineVariant,
        thickness = if(isSystemInDarkTheme)
            0.5.dp
        else
            DividerDefaults.Thickness
    )
}

@Composable
@NonRestartableComposable
fun ItemDescription(
    modifier: Modifier = Modifier
        .heightIn(
            max = 120.dp
        ),
    description: String?,
    fontSize: TextUnit = 16.sp
) {
    description?.let {
        val state = rememberRichTextState()
        state.config.linkColor = MaterialTheme.colorScheme.primary
        state.setMarkdown(description)
        RichText(
            modifier = modifier,
            textAlign = TextAlign.Justify,
            color = LocalContentColor.current,
            fontFamily = bodyFontFamily,
            fontSize = fontSize,
            fontStyle = AppTypography.bodyMedium.fontStyle,
            state = state
        )
    }
}

fun <T: RefyItem> getItemRelations(
    userList: List<T>,
    linkList: List<T>
): List<T> {
    val containers = mutableListOf<T>()
    containers.addAll(userList)
    containers.removeAll(linkList)
    return containers
}

fun Modifier.drawOneSideBorder(
    width: Dp,
    color: Color,
    shape: Shape = RectangleShape
) = this
    .clip(shape)
    .drawWithContent {
        val widthPx = width.toPx()
        drawContent()
        drawLine(
            color = color,
            start = Offset(widthPx / 2, 0f),
            end = Offset(widthPx / 2, size.height),
            strokeWidth = widthPx
        )
    }

@Composable
@NonRestartableComposable
fun AddItemToContainer(
    show: MutableState<Boolean>,
    viewModel: EquinoxViewModel,
    icon: ImageVector,
    availableItems: List<RefyItem>,
    title: StringResource,
    confirmAction: (List<String>) -> Unit
) {
    viewModel.SuspendUntilElementOnScreen(
        elementVisible = show
    )
    val ids = mutableListOf<String>()
    EquinoxAlertDialog(
        show = show,
        icon = icon,
        title = title,
        text = {
            LazyColumn (
                modifier = Modifier
                    .width(400.dp)
                    .heightIn(
                        max = 150.dp
                    )
            ) {
                items(
                    items = availableItems,
                    key = { item -> item.id }
                ) { item ->
                    var selected by remember { mutableStateOf(ids.contains(item.id)) }
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selected,
                            onCheckedChange = {
                                selected = it
                                if(selected)
                                    ids.add(item.id)
                                else
                                    ids.remove(item.id)
                            }
                        )
                        Text(
                            text = item.title
                        )
                    }
                    HorizontalDivider()
                }
            }
        },
        confirmAction = { confirmAction.invoke(ids) },
        confirmText = Res.string.add
    )
}

@Composable
@NonRestartableComposable
fun DeleteItemButton(
    show: MutableState<Boolean>,
    deleteAction: @Composable () -> Unit,
    tint: Color = MaterialTheme.colorScheme.error
) {
    OptionButton(
        icon = Icons.Default.Delete,
        show = show,
        optionAction = deleteAction,
        tint = tint
    )
}

@Composable
@NonRestartableComposable
fun OptionButton(
    icon: ImageVector,
    visible: (() -> Boolean) = { true },
    show: MutableState<Boolean>,
    optionAction: @Composable () -> Unit,
    tint: Color = LocalContentColor.current,
) {
    AnimatedVisibility(
        visible = visible.invoke(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        IconButton(
            onClick = { show.value = true }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint
            )
        }
        optionAction.invoke()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@NonRestartableComposable
fun ExpandTeamMembers(
    viewModel: EquinoxViewModel,
    show: MutableState<Boolean>,
    teams: List<Team>
) {
    viewModel.SuspendUntilElementOnScreen(
        elementVisible = show
    )
    if(show.value) {
        ModalBottomSheet(
            onDismissRequest = { show.value = false }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                teams.forEach { team ->
                    item {
                        Text(
                            modifier = Modifier
                                .padding(
                                    top = 16.dp,
                                    start = 16.dp
                                ),
                            text = team.title,
                            fontFamily = displayFontFamily
                        )
                    }
                    items(
                        items = team.members,
                        key = { member -> member.id + team.id }
                    ) { member ->
                        DefaultPlaque(
                            profilePic = member.profilePic,
                            completeName = member.completeName,
                            tagName = member.completeName,
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
@NonRestartableComposable
fun TeamMemberPlaque(
    team: Team,
    member: RefyTeamMember,
    viewModel: TeamActivityViewModel
) {
    val isAuthorizedUser = team.isAdmin(user.id) && member.id != user.id
    val enableOption = isAuthorizedUser && !team.isTheAuthor(member.id)
    DefaultPlaque(
        profilePic = member.profilePic,
        completeName = member.completeName,
        tagName = member.completeName,
        supportingContent = {
            RolesMenu(
                enableOption = enableOption,
                viewModel = viewModel,
                member = member
            )
        },
        trailingContent = if(enableOption) {
            {
                IconButton(
                    onClick = {
                        viewModel.removeMember(
                            member = member
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.GroupRemove,
                        contentDescription = null
                    )
                }
            }
        } else
            null
    )
    HorizontalDivider()
}

@Composable
@NonRestartableComposable
fun UserPlaque(
    colors: ListItemColors = ListItemDefaults.colors(),
    profilePicSize: Dp = 50.dp,
    user: RefyUser,
    supportingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    DefaultPlaque(
        colors = colors,
        profilePicSize = profilePicSize,
        profilePic = user.profilePic,
        completeName = user.completeName,
        tagName = user.completeName,
        supportingContent = supportingContent,
        trailingContent = trailingContent
    )
}

@Composable
@NonRestartableComposable
private fun DefaultPlaque(
    colors: ListItemColors = ListItemDefaults.colors(),
    profilePicSize: Dp = 50.dp,
    profilePic: String,
    completeName: String,
    tagName: String,
    supportingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    ListItem(
        colors = colors,
        leadingContent = {
            Logo(
                picSize = profilePicSize,
                picUrl = profilePic
            )
        },
        headlineContent = {
            Text(
                text = completeName
            )
        },
        supportingContent = supportingContent,
        overlineContent = {
            Text(
                text = tagName
            )
        },
        trailingContent = trailingContent
    )
}

@Composable
@NonRestartableComposable
private fun RolesMenu(
    enableOption: Boolean,
    viewModel: TeamActivityViewModel,
    member: RefyTeamMember
) {
    val role = (member).role
    val changeRole = remember { mutableStateOf(false) }
    Column {
        Text(
            modifier = Modifier
                .clickable(
                    enabled = enableOption
                ) {
                    changeRole.value = true
                },
            text = role.name,
            color = if(role == ADMIN)
                MaterialTheme.colorScheme.error
            else
                Color.Unspecified
        )
        viewModel.SuspendUntilElementOnScreen(
            elementVisible = changeRole
        )
        DropdownMenu(
            expanded = changeRole.value,
            onDismissRequest = { changeRole.value = false }
        ) {
            TeamRole.entries.forEach { role ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = role.name
                        )
                    },
                    onClick = {
                        viewModel.changeMemberRole(
                            member = member,
                            role = role,
                            onSuccess = { changeRole.value = false }
                        )
                    }
                )
            }
        }
    }
}

@Composable
@NonRestartableComposable
fun Logo(
    modifier: Modifier = Modifier,
    picSize: Dp = 50.dp,
    addShadow: Boolean = false,
    onClick: (() -> Unit)? = null,
    shape: Shape = CircleShape,
    picUrl: String
) {
    AsyncImage(
        modifier = modifier
            .clip(shape)
            .size(picSize)
            .clickable(
                enabled = onClick != null,
            ) {
                onClick?.invoke()
            }
            .then(
                if (addShadow) {
                    Modifier.shadow(
                        elevation = 5.dp,
                        shape = shape
                    )
                } else
                    Modifier
            ),
        imageLoader = imageLoader,
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(picUrl)
            .crossfade(enable = true)
            .crossfade(500)
            //.error() //TODO: TO SET THE ERROR IMAGE CORRECTLY
            .build(),
        contentDescription = null,
        contentScale = ContentScale.FillBounds
    )
}