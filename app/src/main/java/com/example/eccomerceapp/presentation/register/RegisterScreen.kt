package com.example.eccomerceapp.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eccomerceapp.presentation.components.FeedbackBanner
import com.example.eccomerceapp.presentation.components.FeedbackType
import com.example.eccomerceapp.ui.theme.BackgroundGray
import com.example.eccomerceapp.ui.theme.EccomerceAppTheme
import com.example.eccomerceapp.ui.theme.KabumOrange

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isRegisterSuccessful) {
        if (uiState.isRegisterSuccessful) {
            onRegisterSuccess()
            viewModel.resetRegisterSuccess()
        }
    }

    RegisterContent(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onRegisterClick = viewModel::register,
        onClearError = viewModel::clearError,
        onNavigateToLogin = onNavigateToLogin
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterContent(
    uiState: RegisterUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onClearError: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                IconButton(onClick = onNavigateToLogin) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(28.dp),
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Cadastro",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2021)
                    )

                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Já tem uma conta? ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        TextButton(
                            onClick = onNavigateToLogin,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Entrar",
                                color = KabumOrange,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    FeedbackBanner(
                        message = uiState.errorMessage ?: "",
                        type = FeedbackType.ERROR,
                        isVisible = uiState.errorMessage != null
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    RegisterField(
                        label = "Nome completo",
                        value = uiState.name,
                        placeholder = "Ex: Lois Becket",
                        onValueChange = onNameChange,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    RegisterField(
                        label = "E-mail",
                        value = uiState.email,
                        placeholder = "email@exemplo.com",
                        onValueChange = onEmailChange,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    RegisterField(
                        label = "Data de nascimento",
                        value = "", 
                        placeholder = "DD/MM/AAAA",
                        onValueChange = { },
                        trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.Gray) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    RegisterField(
                        label = "Número de telefone",
                        value = "",
                        placeholder = "(00) 00000-0000",
                        onValueChange = { },
                        leadingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 12.dp, end = 8.dp)) {
                                Text("🇧🇷", fontSize = 20.sp)
                                Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(12.dp).padding(start = 4.dp), tint = Color.Gray)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    RegisterField(
                        label = "Senha",
                        value = uiState.password,
                        placeholder = "********",
                        onValueChange = onPasswordChange,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onVisibilityToggle = { passwordVisible = !passwordVisible },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { onRegisterClick() })
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = onRegisterClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = KabumOrange),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Cadastrar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun RegisterField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onVisibilityToggle: (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = leadingIcon,
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = onVisibilityToggle ?: {}) {
                        Text(if (passwordVisible) "👁️" else "👁️‍🗨️", fontSize = 16.sp)
                    }
                }
            } else trailingIcon,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFF0F0F0),
                focusedBorderColor = KabumOrange,
                unfocusedContainerColor = Color(0xFFF9F9F9),
                focusedContainerColor = Color.White
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    EccomerceAppTheme {
        RegisterContent(
            uiState = RegisterUiState(),
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onRegisterClick = {},
            onClearError = {},
            onNavigateToLogin = {}
        )
    }
}
