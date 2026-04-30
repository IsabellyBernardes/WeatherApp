package com.weatherapp

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weatherapp.ui.theme.WeatherAppTheme

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterPage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPage(modifier: Modifier = Modifier) {
    var nome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    val activity = LocalActivity.current as Activity

    Column(
        modifier = modifier.padding(24.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val modifier = modifier.fillMaxWidth(fraction = 0.9f)

        Text(
            text = "Cadastre-se",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.size(12.dp))

        OutlinedTextField(
            value = nome,
            label = { Text(text = "Digite seu nome") },
            modifier = modifier,
            onValueChange = { nome = it }
        )

        OutlinedTextField(
            value = email,
            label = { Text(text = "Digite seu e-mail") },
            modifier = modifier,
            onValueChange = { email = it }
        )

        Spacer(modifier = Modifier.size(12.dp))

        OutlinedTextField(
            value = password,
            label = { Text(text = "Digite sua senha") },
            modifier = modifier,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.size(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            label = { Text(text = "Repita sua senha") },
            modifier = modifier,
            onValueChange = { confirmPassword = it },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.size(12.dp))

        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    Toast.makeText(activity, "Registro realizado com sucesso!", Toast.LENGTH_LONG).show()
                    activity.finish()
                },
                enabled = password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword
            ) {
                Text("Cadastrar")
            }
            Button(
                onClick = { email = ""; password = "" }
            ) {
                Text("Limpar")
            }
        }
    }
}