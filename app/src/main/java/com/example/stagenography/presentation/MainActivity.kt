package com.example.stagenography.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stagenography.presentation.components.DecodeScreen
import com.example.stagenography.presentation.components.EncodeScreen
import com.example.stagenography.presentation.components.MainScreen
import com.example.stagenography.ui.theme.StagenographyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StagenographyTheme {
                val navController = rememberNavController()
                SteganographyNavHost(navController)
            }
        }
    }
}

@Composable
fun SteganographyNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                onEncodeClick = { navController.navigate("encode") },
                onDecodeClick = { navController.navigate("decode") }
            )
        }
        composable("encode") {
            EncodeScreen()
        }
        composable("decode") {
            DecodeScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StagenographyTheme {
        SteganographyNavHost(rememberNavController())
    }
}