@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.session.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.ui.generateRandomColor
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.user
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.ui.utilities.ItemDescription
import com.tecknobit.refy.ui.viewmodels.create.CreateCollectionViewModel
import com.tecknobit.refycore.records.LinksCollection
import refy.composeapp.generated.resources.*

class CreateCollectionScreen(
    collectionId: String?
): CreateScreen<LinksCollection, CreateCollectionViewModel>(
    items = user.collections,
    invalidMessage = Res.string.invalid_collection,
    itemId = collectionId
) {

    private lateinit var choseColor: MutableState<Boolean>

    init {
        viewModel = CreateCollectionViewModel(
            snackbarHostState = snackbarHostState
        )
    }

    @Composable
    override fun ScreenContent() {
        super.ScreenContent()
        viewModel.collectionColor = remember {
            mutableStateOf(
                if(itemExists)
                    item!!.color.toColor()
                else
                    generateRandomColor()
            )
        }
        choseColor = remember { mutableStateOf(false) }
        ScaffoldContent(
            modifier = Modifier
                .clickable { choseColor.value = true },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = viewModel.collectionColor.value
            ),
            saveButtonColor = viewModel.collectionColor.value,
            placeholder = Res.string.collection_name,
            customContent = {
                LinksSection()
                ChoseCollectionColor()
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun ChoseCollectionColor() {
        val controller = rememberColorPickerController()
        var currentColor = remember { viewModel.collectionColor.value.copy() }
        EquinoxAlertDialog(
            show = choseColor,
            title = Res.string.collection_color,
            text = {
                HsvColorPicker(
                    modifier = Modifier
                        .size(
                            width = 300.dp,
                            height = 250.dp
                        ),
                    controller = controller,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        viewModel.collectionColor.value = colorEnvelope.color
                    },
                    initialColor = currentColor
                )
            },
            onDismissAction = {
                viewModel.collectionColor.value = currentColor
                choseColor.value = false
            },
            confirmAction = {
                currentColor = viewModel.collectionColor.value
                choseColor.value = false
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun LinksSection() {
        val keyboardController = LocalSoftwareKeyboardController.current
        CustomSection(
            header = Res.string.links
        ) {
            items(
                items = user.links,
                key = { link -> link.id }
            ) { link ->
                val checked = remember { mutableStateOf(viewModel.itemDedicatedList.contains(link.id)) }
                var expanded by remember { mutableStateOf(false) }
                ListItem(
                    leadingContent = {
                        ItemCheckbox(
                            checked = checked,
                            keyboardController = keyboardController,
                            itemId = link.id
                        )
                    },
                    overlineContent = {
                        Text(
                            text = link.referenceLink,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    headlineContent = {
                        Text(
                            text = link.title
                        )
                    },
                    supportingContent = {
                        link.description?.let { description ->
                            AnimatedVisibility(
                                visible = expanded
                            ) {
                                ItemDescription(
                                    modifier = Modifier,
                                    description = description,
                                    fontSize = TextUnit.Unspecified
                                )
                            }
                        }
                    },
                    trailingContent = {
                        link.description?.let {
                            IconButton(
                                onClick = { expanded = !expanded }
                            ) {
                                Icon(
                                    imageVector = if(expanded)
                                        Icons.Default.KeyboardArrowUp
                                    else
                                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }

}