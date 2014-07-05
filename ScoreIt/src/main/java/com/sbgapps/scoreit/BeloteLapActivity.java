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

import android.os.Bundle;
import android.widget.RadioButton;

import com.sbgapps.scoreit.games.Player;
import com.sbgapps.scoreit.games.belote.BeloteLap;
import com.sbgapps.scoreit.widget.SeekbarInputPoints;

/**
 * Created by sbaiget on 01/11/13.
 */
public class BeloteLapActivity extends LapActivity {

    private static final LapHolder HOLDER = new LapHolder();
    private static final int[] PROGRESS2POINTS = {80, 90, 100, 110, 120, 130, 140, 150, 160, 250};

    @Override
    public BeloteLap getLap() {
        return (BeloteLap) super.getLap();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lap_belote);

        HOLDER.rb_player1 = (RadioButton) findViewById(R.id.rb_player1);
        HOLDER.rb_player2 = (RadioButton) findViewById(R.id.rb_player2);
        HOLDER.rb_belote_none = (RadioButton) findViewById(R.id.rb_belote_none);
        HOLDER.rb_belote_player1 = (RadioButton) findViewById(R.id.rb_belote_player1);
        HOLDER.rb_belote_player2 = (RadioButton) findViewById(R.id.rb_belote_player2);
        HOLDER.input_points = (SeekbarInputPoints) findViewById(R.id.input_points);

        if (isDialog()) {
            findViewById(R.id.btn_cancel).setOnClickListener(this);
            findViewById(R.id.btn_confirm).setOnClickListener(this);
        }

        switch (getLap().getTaker()) {
            default:
            case Player.PLAYER_1:
                HOLDER.rb_player1.setChecked(true);
                break;
            case Player.PLAYER_2:
                HOLDER.rb_player2.setChecked(true);
                break;
        }

        HOLDER.input_points.setMax(9);
        HOLDER.input_points.setPoints(getLap().getPoints());

        switch (getLap().getBelote()) {
            case Player.PLAYER_1:
                HOLDER.rb_belote_player1.setChecked(true);
                break;
            case Player.PLAYER_2:
                HOLDER.rb_belote_player2.setChecked(true);
                break;
            default:
            case Player.PLAYER_NONE:
                HOLDER.rb_belote_none.setChecked(true);
                break;
        }
    }

    @Override
    public void updateLap() {
        BeloteLap lap = getLap();
        lap.setTaker(HOLDER.rb_player1.isChecked() ? Player.PLAYER_1 : Player.PLAYER_2);
        lap.setPoints(HOLDER.input_points.getPoints());
        lap.setBelote(HOLDER.rb_belote_none.isChecked() ? Player.PLAYER_NONE :
                HOLDER.rb_belote_player1.isChecked() ? Player.PLAYER_1 : Player.PLAYER_2);
        lap.setScores();
    }

    @Override
    public int progressToPoints(int progress) {
        return PROGRESS2POINTS[progress];
    }

    @Override
    public int pointsToProgress(int points) {
        int progress = points / 10 - 8;
        progress = (17 == progress) ? 9 : progress;
        return progress;
    }

    static class LapHolder {
        RadioButton rb_player1;
        RadioButton rb_player2;
        RadioButton rb_belote_none;
        RadioButton rb_belote_player1;
        RadioButton rb_belote_player2;
        SeekbarInputPoints input_points;
    }
}
