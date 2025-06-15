package com.example.pc02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pc02.ui.screen.LoginScreen
import com.example.pc02.ui.screen.ConvertScreen
import com.example.pc02.ui.screen.HistoryScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.example.pc02.ui.theme.PC02Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val nav = rememberNavController()
                NavHost(nav, startDestination = "login") {
                    composable("login") {
                        LoginScreen(onSuccess = {
                            nav.navigate("convert") {
                                popUpTo("login") { inclusive = true }
                            }
                        })
                    }
                    composable("convert") {
                        ConvertScreen(
                            onLogout = {
                                Firebase.auth.signOut()
                                nav.navigate("login") {
                                    popUpTo("convert") { inclusive = true }
                                }
                            },
                            onShowHistory = {
                                nav.navigate("history")
                            }
                        )
                    }
                    composable("history") {
                        HistoryScreen(onBack = { nav.popBackStack() })
                    }
                }
            }
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PC02Theme {
        Greeting("Android")
    }
}