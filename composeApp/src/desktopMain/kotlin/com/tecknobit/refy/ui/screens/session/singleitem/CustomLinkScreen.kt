@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.session.singleitem

import androidx.annotation.CallSuper
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import com.tecknobit.refy.ui.utilities.DeleteItemButton
import com.tecknobit.refy.ui.utilities.ItemDescription
import com.tecknobit.refy.ui.viewmodels.links.CustomLinkScreenViewModel
import com.tecknobit.refycore.records.links.CustomRefyLink
import navigator
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.json.JSONObject
import refy.composeapp.generated.resources.*

/**
 * The **CustomLinkScreen** class is useful to display a [CustomLinkActivity]'s details and manage
 * that team
 *
 * @param customLinkId: the identifier of the custom link
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 * @see RefyItemBaseScreen
 * @see SingleItemScreen
 * @see SessionManager
 */
class CustomLinkScreen(
    customLinkId: String
) : SingleItemScreen<CustomRefyLink>(
    items = localUser.getCustomLinks(true),
    invalidMessage = Res.string.invalid_custom_link,
    itemId = customLinkId
) {

    /**
     * *viewModel* -> the support view model to manage the requests to the backend
     */
    private lateinit var viewModel: CustomLinkScreenViewModel

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
            item = viewModel.customLink.collectAsState().value
            activityColorTheme = MaterialTheme.colorScheme.primaryContainer
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                topBar = {
                    LargeTopAppBar(
                        navigationIcon = { NavButton() },
                        title = {
                            Text(
                                text = item!!.title
                            )
                        },
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = activityColorTheme
                        ),
                        actions = {
                            ShareButton(
                                link = item!!,
                                tint = iconsColor,
                                snackbarHostState = snackbarHostState
                            )
                            val deleteLink = remember { mutableStateOf(false) }
                            DeleteItemButton(
                                show = deleteLink,
                                deleteAction = {
                                    DeleteItemButton(
                                        show = deleteLink,
                                        deleteAction = {
                                            DeleteLink(
                                                show = deleteLink
                                            )
                                        },
                                        tint = iconsColor
                                    )
                                },
                                tint = iconsColor
                            )
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            openLink(
                                link = item!!.getPreviewModeUrl(localUser.hostAddress)
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Preview,
                            contentDescription = null
                        )
                    }
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = 16.dp
                        )
                        .verticalScroll(rememberScrollState())
                ) {
                    ItemDescription(
                        modifier = Modifier
                            .padding(
                                all = 16.dp
                            ),
                        description = item!!.description
                    )
                    HorizontalDivider()
                    DetailsSection()
                    PayloadSection(
                        header = Res.string.resources,
                        map = item!!.resources
                    )
                    if (item!!.fields.isNotEmpty()) {
                        HorizontalDivider()
                        PayloadSection(
                            header = Res.string.fields,
                            map = item!!.fields
                        )
                    }
                }
            }
        }
    }

    /**
     * Function to display the details of the [CustomRefyLink] displayed such if it has the
     * [CustomRefyLink.hasUniqueAccess] or [CustomRefyLink.expires]
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun DetailsSection() {
        val hasUniqueAccess = item!!.hasUniqueAccess()
        val expires = item!!.expires()
        if(hasUniqueAccess || expires) {
            HeaderText(
                header = Res.string.details
            )
            Column (
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if(hasUniqueAccess) {
                    DetailInfo(
                        info = stringResource(Res.string.unique_access_text)
                    )
                }
                if(expires) {
                    DetailInfo(
                        info = stringResource(Res.string.the_link_will_expire_on, item!!.expirationDate)
                    )
                }
            }
            HorizontalDivider()
        }
    }

    /**
     * Function to display the info details of the [CustomRefyLink]
     *
     * @param info: the info details
     */
    @Composable
    @NonRestartableComposable
    private fun DetailInfo(
        info: String
    ) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        all = 10.dp
                    ),
                text = info,
                textAlign = TextAlign.Justify
            )
        }
    }

    /**
     * Function to display the payload attached to the [CustomRefyLink] such the resources or the
     * validation fields
     *
     * @param header: the resource identifier of the header text
     * @param map: the map with the details to display
     */
    @Composable
    @NonRestartableComposable
    private fun PayloadSection(
        header: StringResource,
        map: Map<String, String>
    ) {
        HeaderText(
            header = header
        )
        Column (
            modifier = Modifier
                .padding(
                    top = 10.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val payload = JSONObject(map)
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(
                        max = 300.dp
                    )
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            all = 10.dp
                        )
                        .verticalScroll(rememberScrollState())
                        .horizontalScroll(rememberScrollState()),
                    text = payload.toString(4),
                    textAlign = TextAlign.Justify,
                    fontSize = 16.sp
                )
            }
        }
    }

    /**
     * Function to display the info details of the [CustomRefyLink]
     *
     * @param show: whether show the warn [EquinoxAlertDialog] to warn the user about the custom link
     * deletion
     */
    @Composable
    @NonRestartableComposable
    private fun DeleteLink(
        show: MutableState<Boolean>
    ) {
        if (show.value)
            viewModel.suspendRefresher()
        val resetLayout = {
            show.value = false
            viewModel.restartRefresher()
        }
        EquinoxAlertDialog(
            show = show,
            onDismissAction = resetLayout,
            icon = Icons.Default.Delete,
            title = Res.string.delete_link,
            text = Res.string.delete_link_message,
            confirmAction = {
                viewModel.deleteLink(
                    link = item!!,
                    onSuccess = {
                        show.value = false
                        navigator.goBack()
                    }
                )
            }
        )
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
            viewModel = CustomLinkScreenViewModel(
                snackbarHostState = snackbarHostState,
                initialCustomLink = item!!
            )
            viewModel.setActiveContext(this::class.java)
            viewModel.refreshLink()
        }
    }

}