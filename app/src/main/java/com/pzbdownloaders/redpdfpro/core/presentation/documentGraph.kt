package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pzbdownloaders.redpdfpro.documentfeature.screens.DocumentFeature

fun NavGraphBuilder.documentGraph(activity: MainActivity,viewModel: MyViewModel,navHostController: NavHostController){
    navigation(startDestination = Screens.Documents.route,route = DOCUMENTS_GRAPH){
        composable(Screens.Documents.route){
            DocumentFeature(activity,viewModel,navHostController)
        }
    }
}