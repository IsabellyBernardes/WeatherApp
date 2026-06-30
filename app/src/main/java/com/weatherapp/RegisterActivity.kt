package com.weatherapp

import android.app.Activity
import android.content.Intent
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
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.db.fb.toFBUser
import com.weatherapp.model.User
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
                    Firebase.auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity) { task ->
                            if (task.isSuccessful) {
                                val uid = Firebase.auth.currentUser?.uid
                                Log.d("REGISTER", "Auth OK - UID: $uid")
                                FBDatabase().register(
                                    user = User(nome, email).toFBUser(),
                                    onSuccess = {
                                        Log.d("REGISTER", "Firestore write SUCCESS - UID: $uid")
                                        Toast.makeText(activity,
                                            "Registro OK!", Toast.LENGTH_LONG).show()
                                        activity.startActivity(
                                            Intent(activity, MainActivity::class.java).setFlags(
                                                Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            )
                                        )
                                        activity.finish()
                                    },
                                    onFailure = { e ->
                                        Log.e("REGISTER", "Firestore write FAILED", e)
                                        Toast.makeText(activity,
                                            "Erro ao salvar dados: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                )
                            } else {
                                Toast.makeText(activity,
                                    "Registro FALHOU: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
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