package com.iscoding.videocallstream.di

import com.iscoding.videocallstream.MyApplication
import com.iscoding.videocallstream.connection.ConnectionViewModel
import com.iscoding.videocallstream.videocall.VideoCallViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    factory {
        val app = androidContext().applicationContext as MyApplication
        app.client
    }

    viewModelOf(::ConnectionViewModel)
    viewModelOf(::VideoCallViewModel)
}