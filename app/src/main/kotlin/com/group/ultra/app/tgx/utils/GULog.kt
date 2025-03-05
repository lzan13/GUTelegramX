package com.group.ultra.app.tgx.utils

import android.util.Log
import com.group.ultra.app.tgx.common.Tags

/**
 * author: lzan13
 * date: 2025/03/03
 * description: 日志工具类
 */
object GULog {

  fun d(msg: String) {
    Log.d(Tags.groupUltra, msg)
  }

  fun i(msg: String) {
    Log.i(Tags.groupUltra, msg)
  }

  fun e(msg: String) {
    Log.e(Tags.groupUltra, msg)
  }
}