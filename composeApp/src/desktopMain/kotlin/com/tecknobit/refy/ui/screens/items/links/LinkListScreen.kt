package com.tecknobit.refy.ui.screens.items.links

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.refy.ui.viewmodels.links.LinkListViewModel
import com.tecknobit.refycore.helpers.RefyInputValidator.isDescriptionValid
import com.tecknobit.refycore.helpers.RefyInputValidator.isLinkResourceValid
import com.tecknobit.refycore.records.links.RefyLink
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.*

class LinkListScreen : LinksScreen<RefyLink>(
    viewModel = LinkListViewModel()
) {

    private lateinit var addLink: MutableState<Boolean>

    init {
        viewModel.setActiveContext(this::class.java)
    }

    @Composable
    override fun ShowContent() {
        LinksList()
    }

    @Composable
    @NonRestartableComposable
    override fun LinkCard(
        link: RefyLink,
    ) {
        RefyLinkCard(
            link = link,
            onClick = {
                openLink(
                    link = link
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    override fun SetFabAction() {
        addLink = remember { mutableStateOf(false) }
        AddLink()
    }

    override fun executeFabAction() {
        addLink.value = true
    }

    @Composable
    @NonRestartableComposable
    private fun AddLink() {
        LinkDialog(
            show = addLink,
            icon = Icons.Default.Edit,
            title = Res.string.add_new_link,
            confirmText = Res.string.add
        )
    }

    @Composable
    override fun EditLink(
        editLink: MutableState<Boolean>,
        link: RefyLink
    ) {
        LinkDialog(
            show = editLink,
            link = link,
            icon = Icons.Default.Edit,
            title = Res.string.edit_link,
            confirmText = Res.string.edit
        )
    }

    @Composable
    @NonRestartableComposable
    private fun LinkDialog(
        show: MutableState<Boolean>,
        icon: ImageVector,
        title: StringResource,
        confirmText: StringResource,
        link: RefyLink? = null
    ) {
        viewModel.linkReference = remember {
            mutableStateOf(
                if(link != null)
                    link.referenceLink
                else
                    ""
            )
        }
        viewModel.linkReferenceError = remember { mutableStateOf(false) }
        viewModel.linkDescription = remember {
            mutableStateOf(
                if(link != null && link.description != null)
                    link.description
                else
                    ""
            )
        }
        viewModel.linkDescriptionError = remember { mutableStateOf(false) }
        viewModel.SuspendUntilElementOnScreen(
            elementVisible = show
        )
        val resetLayout = {
            show.value = false
            viewModel.linkReference.value = ""
            viewModel.linkReferenceError.value = false
            viewModel.linkDescription.value = ""
            viewModel.linkDescriptionError.value = false
        }
        EquinoxAlertDialog(
            show = show,
            icon = icon,
            onDismissAction = resetLayout,
            title = stringResource(title),
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    EquinoxOutlinedTextField(
                        value = viewModel.linkReference,
                        label = stringResource(Res.string.link_reference),
                        validator = { isLinkResourceValid(it) },
                        isError = viewModel.linkReferenceError,
                        errorText = stringResource(Res.string.link_reference_not_valid)
                    )
                    EquinoxOutlinedTextField(
                        value = viewModel.linkDescription,
                        isTextArea = true,
                        label = stringResource(Res.string.description),
                        validator = { isDescriptionValid(it) },
                        isError = viewModel.linkDescriptionError,
                        errorText = stringResource(Res.string.description_not_valid)
                    )
                }
            },
            dismissText = stringResource(Res.string.dismiss),
            confirmText = stringResource(confirmText),
            confirmAction = {
                viewModel.manageLink(
                    link = link,
                    onSuccess = {
                        resetLayout.invoke()
                    }
                )
            }
        )
    }

}