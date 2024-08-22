package com.tecknobit.refy.ui.screens.session

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.tecknobit.equinox.environment.records.EquinoxUser.ApplicationTheme
import com.tecknobit.equinox.environment.records.EquinoxUser.ApplicationTheme.*
import com.tecknobit.equinox.inputs.InputValidator.*
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.refy.helpers.NavigationHelper.Companion.resetFirstTab
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.ui.screens.Screen.Routes.SPLASHSCREEN
import com.tecknobit.refy.ui.screens.navigation.Splashscreen.Companion.localUser
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.utilities.Logo
import com.tecknobit.refy.viewmodels.ProfileScreenViewModel
import displayFontFamily
import navigator
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.*

/**
 * The **ProfileScreen** class is the screen where the user manage his/her profile account
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 */
class ProfileScreen: Screen() {

    /**
     * *viewModel* -> the support view model to manage the requests to the backend
     */
    private val viewModel = ProfileScreenViewModel(
        snackbarHostState = snackbarHostState
    )

    /**
     * **fileType** -> list of allowed image types
     */
    private val fileType = listOf("jpg", "png", "jpeg")

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
    @Composable
    override fun ShowContent() {
        viewModel.setActiveContext(this::class.java)
        RefyTheme {
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) { paddingValues ->
                Column {
                    CustomTopBar()
                    Column (
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        EmailSection()
                        PasswordSection()
                        LanguageSection()
                        ThemeSection()
                        LogoutSection()
                        DeleteSection()
                    }
                }
            }
        }
    }

    /**
     * Function to create a custom top bar to display in the [ProfileActivity] allowing also the user
     * to change his/her profile picture
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun CustomTopBar() {
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.inversePrimary)
                .height(225.dp),
        ) {
            val profilePic = remember { mutableStateOf(localUser.profilePic) }
            var pickProfilePic by remember { mutableStateOf(false) }
            FilePicker(
                show = pickProfilePic,
                fileExtensions = fileType
            ) { profilePicPath ->
                if(profilePicPath != null) {
                    viewModel.changeProfilePic(
                        imagePath = profilePicPath.path,
                        profilePic = profilePic
                    )
                }
            }
            Logo(
                modifier = Modifier
                    .align(Alignment.Center),
                picUrl = profilePic.value,
                picSize = 150.dp,
                addShadow = true,
                shape = CircleShape,
                onClick = { pickProfilePic = true }
            )
            IconButton(
                onClick = { navigator.goBack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
            Column (
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        bottom = 10.dp
                    )
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = localUser.tagName,
                    fontSize = 14.sp
                )
                Text(
                    text = localUser.completeName,
                    fontSize = 20.sp
                )
            }
        }
    }

    /**
     * Function to display the section of the user's email and allowing the user to change it
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun EmailSection() {
        val showChangeEmailAlert = remember { mutableStateOf(false) }
        var userEmail by remember { mutableStateOf(localUser.email) }
        viewModel.newEmail = remember { mutableStateOf("") }
        viewModel.newEmailError = remember { mutableStateOf(false) }
        val resetEmailLayout = {
            viewModel.newEmail.value = ""
            viewModel.newEmailError.value = false
            showChangeEmailAlert.value = false
        }
        UserInfo(
            header = Res.string.email,
            info = userEmail,
            onClick = { showChangeEmailAlert.value = true }
        )
        EquinoxAlertDialog(
            onDismissAction = resetEmailLayout,
            icon = Icons.Default.Email,
            show = showChangeEmailAlert,
            title = Res.string.change_email,
            text = {
                EquinoxOutlinedTextField(
                    value = viewModel.newEmail,
                    label = stringResource(Res.string.new_email),
                    mustBeInLowerCase = true,
                    errorText = stringResource(Res.string.email_not_valid),
                    isError = viewModel.newEmailError,
                    validator = { isEmailValid(it) }
                )
            },
            confirmAction = {
                viewModel.changeEmail(
                    onSuccess = {
                        userEmail = viewModel.newEmail.value
                        resetEmailLayout.invoke()
                    }
                )
            }
        )
    }

    /**
     * Function to display the section of the user's password and allowing the user to change it
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun PasswordSection() {
        val showChangePasswordAlert = remember { mutableStateOf(false) }
        viewModel.newPassword = remember { mutableStateOf("") }
        viewModel.newPasswordError = remember { mutableStateOf(false) }
        val resetPasswordLayout = {
            viewModel.newPassword.value = ""
            viewModel.newPasswordError.value = false
            showChangePasswordAlert.value = false
        }
        var hiddenPassword by remember { mutableStateOf(true) }
        UserInfo(
            header = Res.string.password,
            info = "****",
            onClick = { showChangePasswordAlert.value = true }
        )
        EquinoxAlertDialog(
            onDismissAction = resetPasswordLayout,
            icon = Icons.Default.Password,
            show = showChangePasswordAlert,
            title = stringResource(Res.string.change_password),
            text = {
                EquinoxOutlinedTextField(
                    value = viewModel.newPassword,
                    label = stringResource(Res.string.new_password),
                    trailingIcon = {
                        IconButton(
                            onClick = { hiddenPassword = !hiddenPassword }
                        ) {
                            Icon(
                                imageVector = if(hiddenPassword)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if(hiddenPassword)
                        PasswordVisualTransformation()
                    else
                        VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    errorText = stringResource(Res.string.password_not_valid),
                    isError = viewModel.newPasswordError,
                    validator = { isPasswordValid(it) }
                )
            },
            confirmAction = {
                viewModel.changePassword(
                    onSuccess = resetPasswordLayout
                )
            },
            confirmText = stringResource(Res.string.confirm),
            dismissText = stringResource(Res.string.dismiss)
        )
    }

    /**
     * Function to display the section of the user's language and allowing the user to change it
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun LanguageSection() {
        val changeLanguage = remember { mutableStateOf(false) }
        UserInfo(
            header = Res.string.language,
            info = LANGUAGES_SUPPORTED[localUser.language]!!,
            onClick = { changeLanguage.value = true }
        )
        ChangeLanguage(
            changeLanguage = changeLanguage
        )
    }

    /**
     * Function to display the section of the user's theme and allowing the user to change it
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun ThemeSection() {
        val changeTheme = remember { mutableStateOf(false) }
        UserInfo(
            header = Res.string.theme,
            info = localUser.theme.name,
            buttonText = Res.string.change,
            onClick = { changeTheme.value = true }
        )
        ChangeTheme(
            changeTheme = changeTheme
        )
    }

    /**
     * Function to display the section where the user can disconnect from the current session
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun LogoutSection() {
        val showLogoutAlert = remember { mutableStateOf(false) }
        UserInfo(
            header = Res.string.disconnect,
            info = stringResource(Res.string.logout),
            buttonText = Res.string.execute,
            onClick = { showLogoutAlert.value = true }
        )
        EquinoxAlertDialog(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            show = showLogoutAlert,
            title = stringResource(Res.string.logout),
            text = stringResource(Res.string.logout_message),
            confirmAction = {
                viewModel.clearSession {
                    navToSplash()
                }
            },
            confirmText = stringResource(Res.string.confirm),
            dismissText = stringResource(Res.string.dismiss),
        )
    }

    /**
     * Function to display the section where the user can delete the current account and disconnect
     * from the current session
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun DeleteSection() {
        val showDeleteAlert = remember { mutableStateOf(false) }
        UserInfo(
            header = Res.string.account_deletion,
            info = stringResource(Res.string.delete),
            buttonText = Res.string.execute,
            buttonColors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            onClick = { showDeleteAlert.value = true }
        )
        EquinoxAlertDialog(
            icon = Icons.Default.Cancel,
            show = showDeleteAlert,
            title = Res.string.delete,
            text = Res.string.delete_message,
            confirmAction = {
                viewModel.deleteAccount {
                    navToSplash()
                }
            }
        )
    }

    /**
     * Function to display a specific info details of the user
     *
     * @param header: the header of the info to display
     * @param info: the info details value to display
     * @param buttonText: the text of the setting button
     * @param onClick: the action to execute when the [buttonText] has been clicked
     */
    @Composable
    private fun UserInfo(
        header: StringResource,
        info: String,
        buttonText: StringResource = Res.string.edit,
        buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
        onClick: () -> Unit
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    all = 12.dp
                )
        ) {
            Text(
                text = stringResource(header),
                fontSize = 18.sp
            )
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 5.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = info,
                    fontSize = 20.sp,
                    fontFamily = displayFontFamily
                )
                Button(
                    modifier = Modifier
                        .height(25.dp),
                    colors = buttonColors,
                    onClick = onClick,
                    shape = RoundedCornerShape(5.dp),
                    contentPadding = PaddingValues(
                        start = 10.dp,
                        end = 10.dp,
                        top = 0.dp,
                        bottom = 0.dp
                    ),
                    elevation = ButtonDefaults.buttonElevation(2.dp)
                ) {
                    Text(
                        text = stringResource(buttonText),
                        fontSize = 12.sp
                    )
                }
            }
        }
        HorizontalDivider()
    }

    /**
     * Function to allow the user to change the current language setting
     *
     * @param changeLanguage: the state whether display this section
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ChangeLanguage(
        changeLanguage: MutableState<Boolean>
    ) {
        ChangeInfo(
            showModal = changeLanguage
        ) {
            LANGUAGES_SUPPORTED.keys.forEach { language ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.changeLanguage(
                                newLanguage = language,
                                onSuccess = {
                                    changeLanguage.value = false
                                    navToSplash()
                                }
                            )
                        }
                        .padding(
                            all = 16.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = if (localUser.language == language)
                            MaterialTheme.colorScheme.primary
                        else
                            LocalContentColor.current
                    )
                    Text(
                        text = LANGUAGES_SUPPORTED[language]!!,
                        fontFamily = displayFontFamily
                    )
                }
                HorizontalDivider()
            }
        }
    }

    /**
     * Function to allow the user to change the current theme setting
     *
     * @param changeTheme: the state whether display this section
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ChangeTheme(
        changeTheme: MutableState<Boolean>
    ) {
        ChangeInfo(
            showModal = changeTheme
        ) {
            ApplicationTheme.entries.forEach { theme ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.changeTheme(
                                newTheme = theme,
                                onChange = {
                                    changeTheme.value = false
                                    navToSplash()
                                }
                            )
                        }
                        .padding(
                            all = 16.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = when(theme) {
                            Light -> Icons.Default.LightMode
                            Dark -> Icons.Default.DarkMode
                            else -> Icons.Default.AutoMode
                        },
                        contentDescription = null,
                        tint = if (localUser.theme == theme)
                            MaterialTheme.colorScheme.primary
                        else
                            LocalContentColor.current
                    )
                    Text(
                        text = theme.toString(),
                        fontFamily = displayFontFamily
                    )
                }
                HorizontalDivider()
            }
        }
    }

    /**
     * Function to allow the user to change a current setting
     *
     * @param showModal: the state whether display the [ModalBottomSheet]
     * @param sheetState: the state to apply to the [ModalBottomSheet]
     * @param onDismissRequest: the action to execute when the the [ModalBottomSheet] has been dismissed
     * @param content: the content to display
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ChangeInfo(
        showModal: MutableState<Boolean>,
        sheetState: SheetState = rememberModalBottomSheetState(),
        onDismissRequest: () -> Unit = { showModal.value = false },
        content: @Composable ColumnScope.() -> Unit
    ) {
        if(showModal.value) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = onDismissRequest
            ) {
                Column (
                    content = content
                )
            }
        }
    }

    /**
     * Function to execute the back navigation from the [Splashscreen] activity after user changed any
     * setting which required the refresh of the [localUser]
     *
     * No-any params required
     */
    private fun navToSplash() {
        resetFirstTab()
        navigator.navigate(SPLASHSCREEN.name)
    }
    
}