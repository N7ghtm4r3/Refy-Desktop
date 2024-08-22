package com.tecknobit.refy.ui.screens.session

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinoxcompose.components.ErrorUI
import com.tecknobit.refy.ui.getRefyItem
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refycore.records.RefyItem
import displayFontFamily
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * The **RefyItemBaseScreen** class is useful to give the base behavior of a [RefyItem]'s screens
 * to manage that item and other utilities such find it in the corresponding items list
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
 */
@Structure
abstract class RefyItemBaseScreen <T : RefyItem> (
    val items: List<T>,
    val invalidMessage: StringResource,
    private val itemId: String?
) : Screen() {

    /**
     * *snackbarHostState* -> the host to launch the snackbar messages
     */
    protected val snackbarHostState = SnackbarHostState()

    /**
     * *item* -> the item corresponding that identifier if exist or null otherwise
     */
    protected var item: T? = null

    /**
     * *itemExists* -> whether that item has been found in the list
     */
    protected var itemExists = false

    /**
     * *invalidItem* -> whether the item is valid and so [itemExists] will be *true*
     */
    protected var invalidItem = false

    /**
     * Function to init the [item] searching it in the [items] list by its [itemId]
     *
     * No-any params required
     */
    fun initItemFromScreen() {
        if(itemId != null) {
            item = items.getRefyItem(
                itemId = itemId
            )
            if(item == null)
                invalidItem = true
            else
                itemExists = true
        }
    }

    /**
     * Function to display the error view when the item is not valid or has not been found
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    fun InvalidItemUi() {
        ErrorUI(
            errorMessage = stringResource(invalidMessage),
            retryText = ""
        )
    }

    /**
     * Function to create a header for an activity section
     *
     * @param header: the resource identifier of the header text
     */
    @Composable
    @NonRestartableComposable
    protected fun HeaderText(
        header: StringResource
    ) {
        Text(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp
                ),
            text = stringResource(header),
            fontFamily = displayFontFamily,
            style = AppTypography.titleLarge,
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }

}