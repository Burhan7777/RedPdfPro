package com.pzbdownloaders.redpdfpro.core.presentation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.pzbdownloaders.redpdfpro.aifeature.main.screens.AIMainScreen
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.core.presentation.Screens

fun NavGraphBuilder.aiGraph(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController
) {
    composable(Screens.AIScreen.route){
        AIMainScreen(activity,viewModel,navHostController)

    }
}