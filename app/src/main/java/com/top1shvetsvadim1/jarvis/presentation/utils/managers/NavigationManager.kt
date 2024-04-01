package com.top1shvetsvadim1.jarvis.presentation.utils.managers

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.presentation.utils.views.HubBottomNavigation
import java.lang.ref.WeakReference

object NavigationManager {
    fun setupWithNavController(
        navigationBarView: HubBottomNavigation,
        navController: NavController,
        onTabChanged: (Int) -> Unit = {}
    ) {
        navigationBarView.onTabChanged = { id ->
            onTabChanged(id)
            onNavDestinationSelected(
                id,
                navController
            ).also {
                Log.d("Deb", "NavRes = $it")
            }
        }
        val weakReference = WeakReference(navigationBarView)
        navController.addOnDestinationChangedListener(
            object : NavController.OnDestinationChangedListener {
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?
                ) {
                    val view = weakReference.get()
                    if (view == null) {
                        navController.removeOnDestinationChangedListener(this)
                        return
                    }
                    view.queryTabs { tab ->
                        destination.matchDestination(tab.id)
                    }
                }
            })
    }

    fun NavDestination.matchDestination(@IdRes destId: Int): Boolean = hierarchy.any { it.id == destId }

    fun onNavDestinationSelected(id: Int, navController: NavController): Boolean {
        val builder = NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(true)
        Log.d("Deb","NavController: ${navController.currentDestination?.parent}")
        if (navController.currentDestination!!.parent!!.findNode(id) is ActivityNavigator.Destination) {
            builder.setEnterAnim(R.anim.nav_default_enter_anim)
                .setExitAnim(R.anim.nav_default_exit_anim)
                .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
        } else {
            builder.setEnterAnim(R.animator.nav_default_enter_anim)
                .setExitAnim(R.animator.nav_default_exit_anim)
                .setPopEnterAnim(R.animator.nav_default_pop_enter_anim)
                .setPopExitAnim(R.animator.nav_default_pop_exit_anim)
        }
        builder.setPopUpTo(
            navController.graph.findStartDestination().id,
            inclusive = false,
            saveState = true
        )
        val options = builder.build()
        return try {
            navController.navigate(id, null, options)
            navController.currentDestination?.matchDestination(id) == true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}