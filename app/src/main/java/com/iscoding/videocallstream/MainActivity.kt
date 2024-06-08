package com.iscoding.videocallstream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.iscoding.videocallstream.connection.ConnectionViewModel
import com.iscoding.videocallstream.ui.theme.VideoCallStreamTheme
import com.iscoding.videocallstream.videocall.CallState
import com.iscoding.videocallstream.videocall.VideoCallScreen
import com.iscoding.videocallstream.videocall.VideoCallViewModel
import androidx.navigation.compose.composable
import com.iscoding.videocallstream.connection.ConnectionScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import io.getstream.video.android.compose.theme.VideoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoCallStreamTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = ConnectRoute,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<ConnectRoute> {
                            val viewModel = koinViewModel<ConnectionViewModel>()
                            val state = viewModel.state

                            LaunchedEffect(key1 = state.isConnected) {
                                if (state.isConnected) {
                                    navController.navigate(VideoCallRoute) {
                                        popUpTo(ConnectRoute) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }

                            ConnectionScreen(state = state, onAction = viewModel::onAction)
                        }
                        composable<VideoCallRoute> {
                            val viewModel = koinViewModel<VideoCallViewModel>()
                            val state = viewModel.state

                            LaunchedEffect(key1 = state.callState) {
                                if (state.callState == CallState.ENDED) {
                                    navController.navigate(ConnectRoute) {
                                        popUpTo(VideoCallRoute) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }

                            VideoTheme {
                                VideoCallScreen(state = state, onAction = viewModel::onAction)
                            }
                        }
                    }
                }
            }
        }
    }
}
@Serializable
data object ConnectRoute

@Serializable
data object VideoCallRoute

