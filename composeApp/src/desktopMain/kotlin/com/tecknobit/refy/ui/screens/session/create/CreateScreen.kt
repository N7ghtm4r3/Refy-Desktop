@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.session.create

import androidx.annotation.CallSuper
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
import com.tecknobit.refy.viewmodels.create.CreateItemViewModel
import com.tecknobit.refycore.helpers.RefyInputValidator.MAX_TITLE_LENGTH
import com.tecknobit.refycore.helpers.RefyInputValidator.isDescriptionValid
import com.tecknobit.refycore.records.RefyItem
import navigator
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.description
import refy.composeapp.generated.resources.description_not_valid

/**
 * The **CreateScreen** class is useful to give the base behavior to create or edit a [RefyItem]'s
 * item
 *
 * @param items: the items list
 * @param invalidMessage: the resource identifier of the invalid message to display when the item is
 * not valid or not found in [items] list
 * @param itemId: the identifier of the item
 *
 * @param T: the [RefyItem] of the current activity displayed
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 * @see RefyItemBaseScreen
 */
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

    /**
     * *editItemName* -> whether the name of the item is currently in edit mode
     */
    protected lateinit var editItemName: MutableState<Boolean>

    /**
     * *viewModel* -> the support view model to manage the requests to the backend
     */
    protected lateinit var viewModel: V

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
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

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
    @CallSuper
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

    /**
     * Function to create the [Scaffold] with the details to display
     *
     * @param modifier: the modifier of the scaffold
     * @param colors: the scheme colors to use for the [LargeTopAppBar]
     * @param placeholder: the resource identifier for the placeholder text
     * @param saveButtonColor: the color of the save button
     * @param leftContent: the left content to display
     * @param rightContent: the right content to display
     */
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

    /**
     * Wrapper function to create a back navigation button to nav at the previous caller activity
     *
     * No-any params required
     */
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

    /**
     * Function to create the section that allows the user to edit or show the name of the item
     *
     * @param modifier: the modifier of the [TextField]
     * @param placeholder: the resource identifier for the placeholder text
     */
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

    /**
     * Wrapper function to create a save button to save the current item
     *
     * @param color: the color of the save button
     */
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

    /**
     * Function to create the section that allows the user to edit the description of the item
     *
     * @param modifier: the modifier of the [TextField]
     */
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

    /**
     * Function to display a custom section to display an items list
     *
     * @param header: the resource identifier of the header text
     * @param content: the content of the [LazyColumn]
     */
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

    /**
     * Wrapper function to create a custom check box that when clicked manage the current focus and
     * set to *false* the [editItemName]
     *
     * @param checked: the state used to control whether the checkbox has been checked
     * @param keyboardController: the current keyboard controller
     * @param itemId: the identifier of the item attached to that [Checkbox]
     */
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

    /**
     * Function to check whether the current item can be saved because all the details has been
     * correctly filled
     *
     * No-any params required
     * @return whether the item can be saved as boolean
     */
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