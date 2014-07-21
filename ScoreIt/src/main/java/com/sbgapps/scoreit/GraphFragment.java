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

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.sbgapps.scoreit.games.GameHelper;
import com.sbgapps.scoreit.games.Lap;
import com.sbgapps.scoreit.games.Player;

public class GraphFragment extends Fragment {

    public static final String TAG = GraphFragment.class.getName();
    private final int[] mColors = new int[Player.PLAYER_COUNT_MAX];
    private final Line[] mLines = new Line[Player.PLAYER_COUNT_MAX];
    private GameHelper mGameHelper;
    private LineGraph mGraph;
    private int[] mScores;
    private int mX;
    private int mPointColor;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mGameHelper = ((ScoreItActivity) activity).getGameHelper();
        Resources r = activity.getResources();
        mColors[Player.PLAYER_1] = r.getColor(R.color.color_player1);
        mColors[Player.PLAYER_2] = r.getColor(R.color.color_player2);
        mColors[Player.PLAYER_3] = r.getColor(R.color.color_player3);
        mColors[Player.PLAYER_4] = r.getColor(R.color.color_player4);
        mColors[Player.PLAYER_5] = r.getColor(R.color.color_player5);
        mPointColor = r.getColor(R.color.darker_gray);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, null);
        mGraph = (LineGraph) view.findViewById(R.id.line_graph);
        return view;
    }

    public void traceGraph() {
        mGraph.removeAllLines();
        final int lapCnt = mGameHelper.getLaps().size();
        mGraph.setVisibility((0 == lapCnt) ? View.INVISIBLE : View.VISIBLE);
        if (0 == lapCnt) return;

        final int playerCnt = mGameHelper.getPlayerCount();
        for (int i = 0; i < playerCnt; i++) {
            mLines[i] = new Line();
            mLines[i].setColor(mColors[i]);
            mGraph.addLine(mLines[i]);
        }

        LinePoint p = new LinePoint(mX = 0, 0);
        p.setColor(mPointColor);
        for (int player = 0; player < playerCnt; player++) {
            mGraph.addPointToLine(player, p);
        }

        mScores = new int[Player.PLAYER_COUNT_MAX];
        for (int i = 0; i < lapCnt; i++) {
            Lap lap = mGameHelper.getLaps().get(i);
            addLap(lap);
        }
    }

    public void addLap(Lap lap) {
        mX++;
        final int playerCnt = mGameHelper.getPlayerCount();
        for (int player = 0; player < playerCnt; player++) {
            mScores[player] += lap.getScore(player);
            LinePoint p = new LinePoint(mX, mScores[player]);
            p.setColor(mPointColor);
            mGraph.addPointToLine(player, p);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        traceGraph();
    }
}
