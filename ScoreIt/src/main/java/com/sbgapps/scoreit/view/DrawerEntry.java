/*
 * Copyright (c) 2014 SBG Apps
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sbgapps.scoreit.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sbgapps.scoreit.R;
import com.sbgapps.scoreit.adapter.DrawerArrayAdapter;

public class DrawerEntry implements DrawerItem {

    private final int mResId;
    private final int mGame;
    private View mView;

    public DrawerEntry(int resId, int game) {
        mResId = resId;
        mGame = game;
    }

    @Override
    public int getGame() {
        return mGame;
    }

    @Override
    public int getViewType() {
        return DrawerArrayAdapter.RowType.ENTRY_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (null == convertView) {
            mView = inflater.inflate(R.layout.list_item_drawer_entry, null);
            TextView tv = (TextView) mView.findViewById(R.id.drawer_entry);
            tv.setText(mResId);
        } else {
            mView = convertView;
        }
        return mView;
    }
}
