@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.session.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.refy.ui.screens.session.RefyItemBaseScreen
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.viewmodels.create.CreateItemViewModel
import com.tecknobit.refycore.helpers.RefyInputValidator.MAX_TITLE_LENGTH
import com.tecknobit.refycore.helpers.RefyInputValidator.isDescriptionValid
import com.tecknobit.refycore.records.RefyItem
import navigator
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.description
import refy.composeapp.generated.resources.description_not_valid

@Structure
abstract class CreateScreen<T : RefyItem, V : CreateItemViewModel<T>>(
    items : List<T>,
    invalidMessage: StringResource,
    itemId: String?
) : RefyItemBaseScreen<T>(
    items = items,
    invalidMessage = invalidMessage,
    itemId = itemId
) {

    protected lateinit var editItemName: MutableState<Boolean>

    protected lateinit var viewModel: V

    @Composable
    override fun ShowContent() {
        viewModel.setActiveContext(this::class.java)
        initItemFromScreen()
        RefyTheme {
            if(invalidItem)
                InvalidItemUi()
            else
                ScreenContent()
        }
    }

    @Composable
    protected open fun ScreenContent() {
        LifecycleManager(
            onDispose = {
                viewModel.suspendRefresher()
            }
        )
        viewModel.initExistingItem(
            item = item
        )
        editItemName = remember { mutableStateOf(false) }
        viewModel.itemName = remember {
            mutableStateOf(
                if(itemExists)
                    item!!.title
                else
                    ""
            )
        }
        viewModel.itemDescription = remember {
            mutableStateOf(
                if(itemExists)
                    item!!.description
                else
                    ""
            )
        }
        viewModel.itemDescriptionError = remember { mutableStateOf(false) }
    }

    @Composable
    @NonRestartableComposable
    protected fun ScaffoldContent(
        modifier: Modifier = Modifier,
        colors: TopAppBarColors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        placeholder: StringResource,
        saveButtonColor: Color = MaterialTheme.colorScheme.primaryContainer,
        leftContent: @Composable ColumnScope.() -> Unit,
        rightContent: @Composable ColumnScope.() -> Unit
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                LargeTopAppBar(
                    modifier = modifier,
                    navigationIcon = { NavButton() },
                    title = {
                        ItemNameSection(
                            placeholder = placeholder
                        )
                    },
                    colors = colors
                )
            },
            floatingActionButton = {
                SaveButton(
                    color = saveButtonColor
                )
            }
        ) { paddingValues ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        bottom = paddingValues.calculateBottomPadding() + 16.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        leftContent.invoke(this)
                        DescriptionSection(
                            modifier = Modifier
                                .padding(
                                    top = 16.dp
                                )
                                .width(350.dp)
                        )
                    }
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    content = rightContent
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun NavButton() {
        IconButton(
            onClick = { navigator.goBack() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
    }

    @Composable
    @NonRestartableComposable
    protected fun ItemNameSection(
        modifier: Modifier = Modifier,
        placeholder: StringResource
    ) {
        if(editItemName.value) {
            val localContentColor = LocalContentColor.current
            TextField(
                modifier = modifier,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = localContentColor,
                    focusedIndicatorColor = localContentColor
                ),
                textStyle = TextStyle(
                    fontSize = 20.sp
                ),
                value = viewModel.itemName.value,
                singleLine = true,
                onValueChange = {
                    if(it.length <= MAX_TITLE_LENGTH)
                        viewModel.itemName.value = it
                    else
                        editItemName.value = false
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { editItemName.value = false }
                )
            )
        } else {
            Text(
                modifier = modifier
                    .clickable { editItemName.value = true },
                text = viewModel.itemName.value.ifEmpty {
                    stringResource(placeholder)
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 22.sp
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun SaveButton(
        color: Color
    ) {
        AnimatedVisibility(
            visible = canBeSaved(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FloatingActionButton(
                onClick = {
                    viewModel.manageItem {
                        navigator.goBack()
                    }
                },
                containerColor = color
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    protected fun DescriptionSection(
        modifier: Modifier = Modifier
    ) {
        EquinoxTextField(
            modifier = modifier
                .onFocusChanged {
                    if (it.isFocused)
                        editItemName.value = false
                },
            isTextArea = true,
            value = viewModel.itemDescription,
            label = stringResource(Res.string.description),
            isError = viewModel.itemDescriptionError,
            validator = { isDescriptionValid(it) },
            errorText = stringResource(Res.string.description_not_valid),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
    }

    @Composable
    @NonRestartableComposable
    protected fun CustomSection(
        modifier: Modifier = Modifier,
        header: StringResource,
        content: LazyListScope.() -> Unit
    ) {
        HeaderText(
            header = header
        )
        LazyColumn (
            modifier = modifier,
            contentPadding = PaddingValues(
                top = 5.dp,
                bottom = 5.dp
            ),
            content = content
        )
    }

    @Composable
    @NonRestartableComposable
    protected fun ItemCheckbox(
        checked: MutableState<Boolean>,
        keyboardController: SoftwareKeyboardController?,
        itemId: String
    ) {
        Checkbox(
            checked = checked.value,
            onCheckedChange = {
                checked.value = it
                keyboardController?.hide()
                editItemName.value = false
                if(checked.value)
                    viewModel.itemDedicatedList.add(itemId)
                else
                    viewModel.itemDedicatedList.remove(itemId)
            }
        )
    }

    protected open fun canBeSaved(): Boolean {
        if(editItemName.value)
            return false
        if(viewModel.itemDescriptionError.value)
            return false
        return viewModel.itemName.value.isNotEmpty() &&
                viewModel.itemDescription.value.isNotEmpty() &&
                viewModel.itemDedicatedList.isNotEmpty()
    }

}