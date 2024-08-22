@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.session.create

import androidx.annotation.CallSuper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
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
import com.tecknobit.refy.ui.generateRandomColor
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.utilities.ItemDescription
import com.tecknobit.refy.viewmodels.create.CreateCollectionViewModel
import com.tecknobit.refycore.records.LinksCollection
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.collection_name
import refy.composeapp.generated.resources.invalid_collection
import refy.composeapp.generated.resources.links

/**
 * The **CreateCollectionScreen** class is useful to create or edit a [LinksCollection]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 * @see RefyItemBaseScreen
 * @see CreateScreen
 */
class CreateCollectionScreen(
    collectionId: String?
): CreateScreen<LinksCollection, CreateCollectionViewModel>(
    items = localUser.getCollections(true),
    invalidMessage = Res.string.invalid_collection,
    itemId = collectionId
) {

    init {
        viewModel = CreateCollectionViewModel(
            snackbarHostState = snackbarHostState
        )
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
        viewModel.collectionColor = remember {
            mutableStateOf(
                if(itemExists)
                    item!!.color.toColor()
                else
                    generateRandomColor()
            )
        }
        ScaffoldContent(
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = viewModel.collectionColor.value
            ),
            saveButtonColor = viewModel.collectionColor.value,
            placeholder = Res.string.collection_name,
            leftContent = {
                val controller = rememberColorPickerController()
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
                    initialColor = viewModel.collectionColor.value.copy()
                )
            },
            rightContent = {
                LinksSection()
            }
        )
    }

    /**
     * Function to create the section where choose the links to attach to the current collection
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun LinksSection() {
        val keyboardController = LocalSoftwareKeyboardController.current
        CustomSection(
            modifier = Modifier
                .padding(
                    end = 16.dp
                ),
            header = Res.string.links
        ) {
            items(
                items = localUser.getLinks(true),
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