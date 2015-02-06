package com.sbgapps.scoreit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbgapps.scoreit.R;
import com.sbgapps.scoreit.fragments.GenericBeloteLapFragment;
import com.sbgapps.scoreit.games.GameHelper;
import com.sbgapps.scoreit.games.Player;
import com.sbgapps.scoreit.games.belote.GenericBeloteLap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sbaiget on 28/10/2014.
 */
public class BelotePoints extends LinearLayout
        implements SeekPoints.OnProgressChangedListener {

    @InjectView(R.id.player1_name)
    TextView mPlayer1Name;
    @InjectView(R.id.player2_name)
    TextView mPlayer2Name;
    @InjectView(R.id.player1_points)
    TextView mPlayer1Points;
    @InjectView(R.id.player2_points)
    TextView mPlayer2Points;
    @InjectView(R.id.btn_switch)
    CircleImageView mSwitchBtn;
    @InjectView(R.id.group_score)
    ToggleGroup mScoreGroup;
    @InjectView(R.id.seekbar_points)
    SeekPoints mSeekPoints;

    GenericBeloteLap mLap;

    public BelotePoints(Context context) {
        this(context, null);
    }

    public BelotePoints(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BelotePoints(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public void init(final GenericBeloteLapFragment beloteFrag) {
        final GameHelper gameHelper = beloteFrag.getGameHelper();
        mLap = beloteFrag.getLap();

        mPlayer1Name.setText(gameHelper.getPlayer(Player.PLAYER_1).getName());
        mPlayer2Name.setText(gameHelper.getPlayer(Player.PLAYER_2).getName());


        mSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Player.PLAYER_1 == mLap.getScorer()) {
                    mLap.setScorer(Player.PLAYER_2);
                } else {
                    mLap.setScorer(Player.PLAYER_1);
                }
                displayScores();
            }
        });

        mScoreGroup.setOnCheckedChangeListener(new ToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ToggleGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_score:
                        mSeekPoints.setVisibility(View.VISIBLE);
                        mLap.setPoints(110);
                        mSeekPoints.setPoints(110, "110");
                        break;
                    case R.id.btn_inside:
                        mSeekPoints.setVisibility(View.GONE);
                        mLap.setPoints(160);
                        break;
                    case R.id.btn_capot:
                        mSeekPoints.setVisibility(View.GONE);
                        mLap.setPoints(250);
                        break;
                }
                displayScores();
            }
        });

        int points = mLap.getPoints();
        if (160 == points) {
            mScoreGroup.check(R.id.btn_inside);
            mSeekPoints.setVisibility(View.GONE);
        } else if (250 == points) {
            mScoreGroup.check(R.id.btn_capot);
            mSeekPoints.setVisibility(View.GONE);
        } else {
            mScoreGroup.check(R.id.btn_score);
        }
        mSeekPoints.init(
                points,
                162,
                Integer.toString(points));
        mSeekPoints.setOnProgressChangedListener(this);
        displayScores();
    }

    @Override
    public String onProgressChanged(SeekPoints seekPoints, int progress) {
        mLap.setPoints(progress);
        displayScores();
        return Integer.toString(progress);
    }

    private void displayScores() {
        mLap.computePoints();
        mPlayer1Points.setText(Integer.toString(mLap.getScore(Player.PLAYER_1)));
        mPlayer2Points.setText(Integer.toString(mLap.getScore(Player.PLAYER_2)));
    }
}