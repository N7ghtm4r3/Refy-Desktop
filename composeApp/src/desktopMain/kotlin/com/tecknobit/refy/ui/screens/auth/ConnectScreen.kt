package com.tecknobit.refy.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinox.inputs.InputValidator.*
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.utilities.RefyLinkUtilities
import com.tecknobit.refy.viewmodels.ConnectViewModel
import com.tecknobit.refycore.helpers.RefyInputValidator.isTagNameValid
import com.tecknobit.refycore.records.links.RefyLink
import displayFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.*

/**
 * The **ConnectScreen** class is useful to manage the authentication requests of the user
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 * @see RefyLinkUtilities
 */
class ConnectScreen : Screen(), RefyLinkUtilities<RefyLink> {

    /**
     * *snackbarHostState* -> the host to launch the snackbar messages
     */
    private val snackbarHostState = SnackbarHostState()

    /**
     * *viewModel* -> the support view model to manage the requests to the backend
     */
    private val viewModel = ConnectViewModel(
        snackbarHostState = snackbarHostState
    )

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
    @Composable
    override fun ShowContent() {
        viewModel.isSignUp = remember { mutableStateOf(true) }
        viewModel.host = remember { mutableStateOf("") }
        viewModel.hostError = remember { mutableStateOf(false) }
        viewModel.serverSecret = remember { mutableStateOf("") }
        viewModel.serverSecretError = remember { mutableStateOf(false) }
        viewModel.tagName = remember { mutableStateOf("") }
        viewModel.tagNameError = remember { mutableStateOf(false) }
        viewModel.name = remember { mutableStateOf("") }
        viewModel.nameError = remember { mutableStateOf(false) }
        viewModel.surname = remember { mutableStateOf("") }
        viewModel.surnameError = remember { mutableStateOf(false) }
        viewModel.email = remember { mutableStateOf("") }
        viewModel.emailError = remember { mutableStateOf(false) }
        viewModel.password = remember { mutableStateOf("") }
        viewModel.passwordError = remember { mutableStateOf(false) }
        Scaffold (
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                HeaderSection()
                FormSection()
            }
        }
    }

    /**
     * Function to create the header section of the activity
     *
     * No-any params required
     */
    @Composable
    private fun HeaderSection() {
        Column (
            modifier = Modifier
                .height(110.dp)
                .background(MaterialTheme.colorScheme.primary),
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        all = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(
                            if (viewModel.isSignUp.value)
                                Res.string.hello
                            else
                                Res.string.welcome_back
                        ),
                        fontFamily = displayFontFamily,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(
                            if (viewModel.isSignUp.value)
                                Res.string.sign_up
                            else
                                Res.string.sign_in
                        ),
                        fontFamily = displayFontFamily,
                        color = Color.White,
                        fontSize = 35.sp
                    )
                }
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            modifier = Modifier,
                            onClick = {
                                openLink(
                                    link = "https://github.com/N7ghtm4r3/Refy-Desktop"
                                )
                            }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.github),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "v. ${stringResource(Res.string.app_version)}",
                            fontFamily = displayFontFamily,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }

    /**
     * Function to create the form where the user can fill it with his credentials
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun FormSection() {
        Column (
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 16.dp
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
                EquinoxOutlinedTextField(
                    value = viewModel.host,
                    label = stringResource(Res.string.host_address),
                    keyboardOptions = keyboardOptions,
                    errorText = stringResource(Res.string.host_address_not_valid),
                    isError = viewModel.hostError,
                    validator = { isHostValid(it) }
                )
                AnimatedVisibility(
                    visible = viewModel.isSignUp.value
                ) {
                    EquinoxOutlinedTextField(
                        value = viewModel.serverSecret,
                        label = stringResource(Res.string.server_secret),
                        keyboardOptions = keyboardOptions,
                        errorText = stringResource(Res.string.server_secret_not_valid),
                        isError = viewModel.serverSecretError,
                        validator = { isServerSecretValid(it) }
                    )
                }
                AnimatedVisibility(
                    visible = viewModel.isSignUp.value
                ) {
                    Column (
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        EquinoxOutlinedTextField(
                            value = viewModel.tagName,
                            label = stringResource(Res.string.tag_name),
                            keyboardOptions = keyboardOptions,
                            errorText = stringResource(Res.string.tag_name_not_valid),
                            isError = viewModel.tagNameError,
                            validator = { isTagNameValid(it) }
                        )
                        EquinoxOutlinedTextField(
                            value = viewModel.name,
                            label = stringResource(Res.string.name),
                            keyboardOptions = keyboardOptions,
                            errorText = stringResource(Res.string.name_not_valid),
                            isError = viewModel.nameError,
                            validator = { isNameValid(it) }
                        )
                        EquinoxOutlinedTextField(
                            value = viewModel.surname,
                            label = stringResource(Res.string.surname),
                            keyboardOptions = keyboardOptions,
                            errorText = stringResource(Res.string.surname_not_valid),
                            isError = viewModel.surnameError,
                            validator = { isSurnameValid(it) }
                        )
                    }
                }
                EquinoxOutlinedTextField(
                    value = viewModel.email,
                    label = stringResource(Res.string.email),
                    mustBeInLowerCase = true,
                    keyboardOptions = keyboardOptions,
                    errorText = stringResource(Res.string.email_not_valid),
                    isError = viewModel.emailError,
                    validator = { isEmailValid(it) }
                )
                var hiddenPassword by remember { mutableStateOf(true) }
                EquinoxOutlinedTextField(
                    value = viewModel.password,
                    label = stringResource(Res.string.password),
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
                    isError = viewModel.passwordError,
                    validator = { isPasswordValid(it) }
                )
                Button(
                    modifier = Modifier
                        .padding(
                            top = 10.dp
                        )
                        .height(
                            60.dp
                        )
                        .width(300.dp),
                    shape = RoundedCornerShape(
                        size = 10.dp
                    ),
                    onClick = { viewModel.auth() }
                ) {
                    Text(
                        text = stringResource(
                        if (viewModel.isSignUp.value)
                            Res.string.sign_up_btn
                        else
                            Res.string.sign_in_btn
                        )
                    )
                }
                Row (
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = stringResource(
                            if (viewModel.isSignUp.value)
                                Res.string.have_an_account
                            else
                                Res.string.are_you_new_to_neutron
                        ),
                        fontSize = 14.sp
                    )
                    Text(
                        modifier = Modifier
                            .clickable { viewModel.isSignUp.value = !viewModel.isSignUp.value },
                        text = stringResource(
                            if (viewModel.isSignUp.value)
                                Res.string.sign_in_btn
                            else
                                Res.string.sign_up_btn
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

}