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

@Structure
abstract class RefyItemBaseScreen <T : RefyItem> (
    val items: List<T>,
    val invalidMessage: StringResource,
    private val itemId: String?
) : Screen() {

    protected val snackbarHostState = SnackbarHostState()

    protected var item: T? = null

    protected var itemExists = false

    protected var invalidItem = false

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

    @Composable
    @NonRestartableComposable
    fun InvalidItemUi() {
        ErrorUI(
            errorMessage = stringResource(invalidMessage),
            retryText = ""
        )
    }

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