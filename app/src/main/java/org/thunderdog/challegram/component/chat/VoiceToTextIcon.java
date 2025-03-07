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
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import org.drinkless.tdlib.TdApi;
import org.thunderdog.challegram.R;
import org.thunderdog.challegram.data.TGMessage;
import org.thunderdog.challegram.theme.ColorId;
import org.thunderdog.challegram.theme.Theme;
import org.thunderdog.challegram.tool.Screen;
import org.thunderdog.challegram.tool.UI;
import org.thunderdog.challegram.widget.FileProgressComponent;


/**
 * author: lzan13
 * date: 2025/03/03
 * description: 自定义扩展语音转文字图标按钮
 */
public class VoiceToTextIcon {

  private int size, radius, padding;

  private Drawable transcribeIcon;
  private Paint paint;

  private float expandFactor = 1f;
  private TGMessage context;
  private TdApi.Message message;

  private @Nullable FileProgressComponent.SimpleListener listener;

  public VoiceToTextIcon (TGMessage context, TdApi.Message message) {
    this.context = context;
    this.message = message;

    // 语音转文字图标
    transcribeIcon = UI.getResources().getDrawable(R.drawable.baseline_format_text_24);

    paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
    paint.setStyle(Paint.Style.FILL);

    size = Screen.dp(32f);
    radius = Screen.dp(8f);
    padding = Screen.dp(4f);
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

  public void setSimpleListener (@Nullable FileProgressComponent.SimpleListener listener) {
    this.listener = listener;
  }

  public void layout () {
    paint.setColor(Theme.getColor(ColorId.waveformInactive));
  }

  public void draw (Canvas c, int startX, int startY) {
    RectF rectF = new RectF(startX, startY, startX + size, startY + size);
    c.drawRoundRect(rectF, radius, radius, paint);

    int color = Theme.getColor(ColorId.waveformActive);
    transcribeIcon.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
    transcribeIcon.setBounds(left + padding, top + padding, right - padding, bottom - padding);
    transcribeIcon.draw(c);
  }

  private int left, top, right, bottom;
  private int startX, startY;
  private boolean isTouchCaught;

  public void setBounds (int startX, int startY) {
      this.left = startX;
      this.top = startY;
      this.right = startX + size;
      this.bottom = top + size;

      layout();
  }

  public boolean onTouchEvent (View view, MotionEvent e) {
    float x = e.getX();
    float y = e.getY();

    switch (e.getAction()) {
      case MotionEvent.ACTION_DOWN: {
        this.startX = (int) x;
        this.startY = (int) y;
        return isTouchCaught = x >= left && x <= right && y >= top && y <= bottom ;
      }
      case MotionEvent.ACTION_MOVE: {
        if (isTouchCaught && Math.max(Math.abs(startX - x), Math.abs(startY - y)) > Screen.getTouchSlop()) {
          isTouchCaught = false;
          return true;
        }
        break;
      }
      case MotionEvent.ACTION_CANCEL: {
        if (isTouchCaught) {
          isTouchCaught = false;
          return true;
        }
        break;
      }
      case MotionEvent.ACTION_UP: {
        if (isTouchCaught) {
          performClick(view, false);
          return true;
        }
        break;
      }
    }
    return isTouchCaught /*|| (x >= left && x <= right && y >= top && y <= bottom)*/;
  }

  private boolean isExpand = false;
  /**
   * 处理点击事件
   */
  public void performClick (View view, boolean ignoreListener) {
    TdApi.MessageVoiceNote content = (TdApi.MessageVoiceNote) message.content;
    TdApi.MessageVoiceNote newContent = new TdApi.MessageVoiceNote();
    newContent.voiceNote = content.voiceNote;
    newContent.isListened = content.isListened;
    newContent.isTranscribe = content.isTranscribe;

    newContent.caption = new TdApi.FormattedText();
    if (newContent.isTranscribe) {
      newContent.isTranscribe = false;
      newContent.caption.text = "";
    } else {
      newContent.isTranscribe = true;
      newContent.caption.text = "测试语音转文字内容展示效果！isTranscribe: " + newContent.isTranscribe;
    }
    context.manager().updateMessageTranscribeContent(context.getChatId(), context.getSmallestId(), newContent);
  }
}
