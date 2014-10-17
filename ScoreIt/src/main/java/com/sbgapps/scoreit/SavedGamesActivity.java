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

package com.sbgapps.scoreit;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sbgapps.scoreit.games.GameHelper;

import java.util.List;

/**
 * Created by Stéphane on 04/08/2014.
 */
public class SavedGamesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_games);
        setupFauxDialog();

        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            SpannableString title = new SpannableString(getResources().getString(R.string.saved_games));
            title.setSpan(getTypefaceSpan(), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        final GameHelper gameHelper = new GameHelper(this);
        final List<String> games = gameHelper.getFilesUtil().getSavedFiles();

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, games));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String game = games.get(position);
                gameHelper.getFilesUtil().setPlayedFile(game);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}