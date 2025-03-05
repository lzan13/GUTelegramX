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
 * File created on 06/01/2023
 */
package org.thunderdog.challegram.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import org.thunderdog.challegram.R;
import org.thunderdog.challegram.component.base.SettingView;
import org.thunderdog.challegram.core.Lang;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.telegram.TdlibSettingsManager;
import org.thunderdog.challegram.v.CustomRecyclerView;

import java.util.ArrayList;

/**
 * author: lzan13
 * date: 2025/03/03
 * description: 自定义扩展设置界面
 */
public class SettingsGroupUltraController extends RecyclerViewController<Void> implements View.OnClickListener, View.OnLongClickListener, TdlibSettingsManager.ChatListPositionListener {

  private SettingsAdapter adapter;

  public SettingsGroupUltraController (Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override
  public int getId () {
    return R.id.controller_groupUltraSettings;
  }

  @Override
  public long getAsynchronousAnimationTimeout (boolean fastAnimation) {
    return 500l;
  }

  @Override
  public CharSequence getName () {
    return Lang.getString(R.string.GroupUltraSettings);
  }

  @Override
  protected void onCreateView (Context context, CustomRecyclerView recyclerView) {
    RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
    if (itemAnimator instanceof SimpleItemAnimator) {
      ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
    }

    ArrayList<ListItem> items = new ArrayList<>();
    items.add(new ListItem(ListItem.TYPE_HEADER_PADDED, 0, 0, R.string.Settings));
    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_groupUltraAiTranslation, 0, R.string.GroupUltraAiTranslation));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_groupUltraVoiceToText, 0, R.string.GroupUltraVoiceToText));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_groupUltraScreenshotRestriction, 0, R.string.GroupUltraScreenshotRestriction));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, R.string.GroupUltraScreenshotRestrictionDescription));


    adapter = new SettingsAdapter(this) {
      @Override
      protected void setValuedSetting (ListItem item, SettingView v, boolean isUpdate) {
        v.setDrawModifier(item.getDrawModifier());

        final int itemId = item.getId();
        if (itemId == R.id.btn_groupUltraAiTranslation) {
          v.getToggler().setRadioEnabled(tdlib.settings().isOpenGroupUltraAiTranslation(), isUpdate);
        } else if (itemId == R.id.btn_groupUltraVoiceToText) {
          v.getToggler().setRadioEnabled(tdlib.settings().isOpenGroupUltraVoiceToText(), isUpdate);
        } else if (itemId == R.id.btn_groupUltraScreenshotRestriction) {
          v.getToggler().setRadioEnabled(tdlib.settings().isOpenGroupUltraScreenshotRestriction(), isUpdate);
        }
      }
    };
    adapter.setItems(items, false);
    recyclerView.setAdapter(adapter);
    addThemeInvalidateListener(recyclerView);

    tdlib.settings().addChatListPositionListener(this);
  }

  @Override
  public void destroy () {
    super.destroy();
  }

  @Override
  public boolean saveInstanceState (Bundle outState, String keyPrefix) {
    super.saveInstanceState(outState, keyPrefix);
    return true;
  }

  @Override
  public boolean restoreInstanceState (Bundle in, String keyPrefix) {
    super.restoreInstanceState(in, keyPrefix);
    return true;
  }

  private boolean shouldUpdateRecommendedChatFolders = false;

  @Override
  protected void onFocusStateChanged () {
    if (isFocused()) {
      if (shouldUpdateRecommendedChatFolders) {
        shouldUpdateRecommendedChatFolders = false;
      }
    } else {
      shouldUpdateRecommendedChatFolders = true;
    }
  }

  @Override
  public void onClick (View v) {
    if (v.getId() == R.id.btn_groupUltraAiTranslation) {
      tdlib.settings().setGroupUltraAiTranslation(adapter.toggleView(v));
    } else if (v.getId() == R.id.btn_groupUltraVoiceToText) {
      tdlib.settings().setGroupUltraVoiceToText(adapter.toggleView(v));
    } else if (v.getId() == R.id.btn_groupUltraScreenshotRestriction) {
      tdlib.settings().setGroupUltraScreenshotRestriction(adapter.toggleView(v));
    }
  }

  @Override
  public boolean onLongClick (View v) {
    if (v.getId() == R.id.chatFolder) {
      return true;
    }
    return false;
  }

}