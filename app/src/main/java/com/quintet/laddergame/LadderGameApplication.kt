package com.quintet.laddergame

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Ladder Game Application
 *
 * 앱 실행 후 딱 한번만 Init 하면 되는 객체들을 여기에 선언하고 사용한다. (Memory Leak 방지)
 */
@HiltAndroidApp
class LadderGameApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}