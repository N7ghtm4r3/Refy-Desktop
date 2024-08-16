@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.tecknobit.refy.ui.screens.session.create

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import com.tecknobit.refy.ui.viewmodels.create.CreateCustomLinkViewModel
import com.tecknobit.refycore.records.links.CustomRefyLink
import com.tecknobit.refycore.records.links.CustomRefyLink.*
import com.tecknobit.refycore.records.links.CustomRefyLink.ExpiredTime.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.*

class CreateCustomLinkScreen(
    customLinkId: String?
): CreateScreen<CustomRefyLink, CreateCustomLinkViewModel>(
    items = localUser.getCustomLinks(true),
    invalidMessage = Res.string.invalid_custom_link,
    itemId = customLinkId
) {

    init {
        viewModel = CreateCustomLinkViewModel(
            snackbarHostState = snackbarHostState
        )
    }

    @Composable
    override fun ScreenContent() {
        super.ScreenContent()
        ScaffoldContent(
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            placeholder = Res.string.custom_link_title,
            leftContent = {
                Options()
            },
            rightContent = {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Payload(
                        header = Res.string.resources,
                        supportList = viewModel.resourcesSupportList,
                        itemName = Res.string.key
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Payload(
                        header = Res.string.fields,
                        supportList = viewModel.fieldsSupportList,
                        itemName = Res.string.field
                    )
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun Options() {
        Column(
            modifier = Modifier
                .width(350.dp)
        ) {
            HeaderText(
                header = Res.string.options
            )
            OptionsSection(
                modifier = Modifier
                    .padding(
                        top = 10.dp
                    ),
                optionKey = UNIQUE_ACCESS_KEY,
                optionText = Res.string.unique_access
            )
            OptionsSection(
                optionKey = EXPIRED_TIME_KEY,
                optionText = Res.string.expires,
                extraContent = { selected ->
                    ExpireSection(
                        selected = selected
                    )
                }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ExpireSection(
        selected: Boolean
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .padding(
                    start = 3.dp
                ),
            visible = selected,
            enter = slideInHorizontally() + fadeIn(),
            exit = slideOutHorizontally() + fadeOut()
        ) {
            Row {
                viewModel.expiredTime = remember {
                    mutableStateOf(
                        if(itemExists && item!!.expires())
                            item!!.expiredTime
                        else
                            ONE_MINUTE
                    )
                }
                Text(
                    text = stringResource(Res.string.expires_in)
                )
                Text(
                    modifier = Modifier
                        .padding(
                            start = 3.dp
                        ),
                    text = getExpirationText(
                        expiredTime = viewModel.expiredTime.value
                    )
                )
                var showExpirations by remember { mutableStateOf(false) }
                Icon(
                    modifier = Modifier
                        .padding(
                            start = 2.dp
                        )
                        .clip(CircleShape)
                        .clickable { showExpirations = true },
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null
                )
                DropdownMenu(
                    expanded = showExpirations,
                    onDismissRequest = { showExpirations = false }
                ) {
                    entries.forEach { expiration ->
                        if(expiration != NO_EXPIRATION) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = getExpirationText(
                                            expiredTime = expiration
                                        )
                                    )
                                },
                                onClick = {
                                    viewModel.expiredTime.value = expiration
                                    showExpirations = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun getExpirationText(
        expiredTime: ExpiredTime
    ) : String {
        val timeValue = expiredTime.timeValue
        return pluralStringResource(
            when(expiredTime) {
                ONE_MINUTE, FIFTEEN_MINUTES, THIRTY_MINUTES -> {
                    Res.plurals.expirations_minute
                }
                ONE_HOUR -> Res.plurals.expirations_hour
                ONE_DAY -> Res.plurals.expirations_day
                else -> Res.plurals.expirations_week
            },
            quantity = timeValue,
            timeValue
        )
    }

    @Composable
    @NonRestartableComposable
    private fun OptionsSection(
        modifier: Modifier = Modifier,
        optionKey: String,
        optionText: StringResource,
        extraContent: @Composable ((Boolean) -> Unit)? = null
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        Row (
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val selected = remember {
                mutableStateOf(viewModel.itemDedicatedList.contains(optionKey))
            }
            ItemCheckbox(
                checked = selected,
                keyboardController = keyboardController,
                itemId = optionKey
            )
            Text(
                text = stringResource(optionText)
            )
            extraContent?.invoke(selected.value)
        }
    }

    @Composable
    @NonRestartableComposable
    private fun Payload(
        header: StringResource,
        supportList: SnapshotStateList<Pair<String, String>>,
        itemName: StringResource
    ) {
        val keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        )
        HeaderText(
            header = header
        )
        LazyColumn(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp
                )
                .heightIn(
                    max = 300.dp
                )
        ) {
            stickyHeader {
                FloatingActionButton(
                    modifier = Modifier
                        .size(35.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    onClick = {
                        viewModel.addNewItem(
                            supportList = supportList
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
            }
            itemsIndexed(
                items = supportList
            ) { index , item ->
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val nameError = remember { mutableStateOf(false) }
                    val valueError = remember { mutableStateOf(false) }
                    EquinoxOutlinedTextField(
                        modifier = Modifier
                            .weight(1f),
                        value = mutableStateOf(item.first),
                        onValueChange = {
                            nameError.value = it.isEmpty()
                            viewModel.addItem(
                                supportList = supportList,
                                index = index,
                                key = it,
                                value = item.second
                            )
                        },
                        isError = nameError,
                        errorText = stringResource(Res.string.not_valid),
                        label = stringResource(itemName),
                        keyboardOptions = keyboardOptions
                    )
                    EquinoxOutlinedTextField(
                        modifier = Modifier
                            .weight(1f),
                        value = mutableStateOf(item.second),
                        onValueChange = {
                            valueError.value = it.isEmpty()
                            viewModel.addItem(
                                supportList = supportList,
                                index = index,
                                key = item.first,
                                value = it
                            )
                        },
                        isError = valueError,
                        errorText = stringResource(Res.string.value_not_valid),
                        label = stringResource(Res.string.value),
                        keyboardOptions = if(index == supportList.lastIndex) {
                            KeyboardOptions(
                                imeAction = ImeAction.Done
                            )
                        } else
                            keyboardOptions
                    )
                    IconButton(
                        onClick = {
                            viewModel.removeItem(
                                supportList = supportList,
                                index = index
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

    override fun canBeSaved(): Boolean {
        if(editItemName.value)
            return false
        if(viewModel.itemDescriptionError.value)
            return false
        if(viewModel.itemName.value.isEmpty() || viewModel.itemDescription.value.isEmpty() ||
            viewModel.resourcesSupportList.isEmpty())
            return false
        viewModel.resourcesSupportList.forEach { resource ->
            if(resource.first.isEmpty() || resource.second.isEmpty())
                return false
        }
        return true
    }

}