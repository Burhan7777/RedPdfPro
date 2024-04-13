package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MainScreen(
    navHostController: NavHostController,
    viewModel: MyViewModel,
    activity: MainActivity
) {
    Scaffold(bottomBar = {
        BottomAppBar {
            MyBottomBar(navHostController = navHostController)
        }
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            MyNavHost(
                navHostController = navHostController,
                activity = activity,
                viewModel = viewModel
            )
        }
    }

}

@Composable
fun MyBottomBar(navHostController: NavHostController) {
    val screens = listOf(
        ScreensBottomNavigation.ScannerScreen,
        ScreensBottomNavigation.Documents,
        ScreensBottomNavigation.HomePageTools
    )
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar {
        screens.forEach {
            NavigationBarItem(selected = currentDestination?.route == it.route, onClick = {
                navHostController.popBackStack()
                navHostController.navigate(it.route)
            }, icon = {
                if (it == ScreensBottomNavigation.Documents) {
                    Icon(
                        painter = painterResource(id = it.painterId),
                        contentDescription = "bottom_navigation_image"
                    )
                } else {
                    Icon(
                        imageVector = it.imageVector,
                        contentDescription = "bottom_navigation_image"
                    )
                }
            },
                label = {
                    Text(text = it.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.tertiary,
                    selectedIconColor = MaterialTheme.colorScheme.onSecondary
                )
            )
        }
    }

}