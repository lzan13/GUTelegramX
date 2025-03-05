/*
 * This file is a part of Telegram X
 * Copyright © 2014 (tgx-android@pm.me)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * File created on 02/03/2016 at 13:32
 */
package org.thunderdog.challegram.component.chat;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import org.thunderdog.challegram.theme.ColorId;
import org.thunderdog.challegram.theme.Theme;
import org.thunderdog.challegram.tool.Screen;


/**
 * author: lzan13
 * date: 2025/03/03
 * description: 自定义扩展设置界面
 */
public class VoiceToTextView {

  private int size, radius;

  private Paint paint;

  private float expandFactor = 1f;

  public VoiceToTextView () {
    paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
    paint.setStyle(Paint.Style.FILL);

    size = Screen.dp(32f);
    radius = Screen.dp(8f);
  }

  public int getWidth () {
    return size;
  }

  public float getExpand () {
    return expandFactor;
  }

  public void setExpand (float expand) {
    this.expandFactor = expand;
  }

  public void layout () {
    paint.setColor(Theme.getColor(ColorId.waveformInactive));
  }

  public void draw (Canvas c, int startX, int startY) {
    RectF rectF = new RectF(startX, startY, startX + size, startY + size);
    c.drawRoundRect(rectF, radius, radius, paint);
  }

}
