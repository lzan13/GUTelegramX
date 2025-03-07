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

import org.drinkless.tdlib.TdApi;
import org.thunderdog.challegram.R;
import org.thunderdog.challegram.data.TGMessage;
import org.thunderdog.challegram.theme.ColorId;
import org.thunderdog.challegram.theme.Theme;
import org.thunderdog.challegram.tool.Screen;
import org.thunderdog.challegram.tool.UI;

import me.vkryl.core.StringUtils;


/**
 * author: lzan13
 * date: 2025/03/03
 * description: 自定义扩展翻译图标按钮
 */
public class TranslationIcon {

  private int size, radius, padding;

  private Drawable translationIcon;

  private float expandFactor = 1f;

  private TGMessage context;
  private TdApi.Message message;

  public TranslationIcon (TGMessage context, TdApi.Message message) {
    this.context = context;
    this.message = message;

    // 翻译图标
    translationIcon = UI.getResources().getDrawable(R.drawable.baseline_translate_14);

    size = Screen.dp(36f);
    radius = Screen.dp(8f);
    padding = Screen.dp(8f);
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

  public void draw (Canvas c, Paint paint, int startX, int startY) {
    RectF rectF = new RectF(startX, startY, startX + size, startY + size);
    c.drawRoundRect(rectF, radius, radius, paint);

    int color = Theme.getColor(ColorId.waveformActive);
    translationIcon.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
    translationIcon.setBounds(left + padding, top + padding, right - padding, bottom - padding);
    translationIcon.draw(c);
  }


  private int left, top, right, bottom;
  private int startX, startY;
  private boolean isTouchCaught;

  public void setBounds (int startX, int startY) {
      this.left = startX;
      this.top = startY;
      this.right = startX + size;
      this.bottom = top + size;

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

  /**
   * 处理点击事件
   */
  public void performClick (View view, boolean ignoreListener) {
    TdApi.MessageText content = (TdApi.MessageText) message.content;
    TdApi.MessageText newContent = new TdApi.MessageText();
    newContent.linkPreview = content.linkPreview;
    newContent.linkPreviewOptions = content.linkPreviewOptions;

    newContent.text = new TdApi.FormattedText();
    if (StringUtils.isEmpty(content.text.orgText)) {
      newContent.text.orgText= content.text.text;
      newContent.text.text="Hello tgx! This is test translation content!";
    }else{
      newContent.text.orgText= "";
      newContent.text.text = content.text.orgText;
    }
    context.manager().updateMessageTranslationContent(context.getChatId(), context.getSmallestId(), newContent);
    // 手动触发视图的重新绘制
//    context.invalidate();
  }
}
