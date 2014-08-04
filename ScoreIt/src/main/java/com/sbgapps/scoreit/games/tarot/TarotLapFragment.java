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

package com.sbgapps.scoreit.games.tarot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.sbgapps.scoreit.R;
import com.sbgapps.scoreit.games.LapFragment;
import com.sbgapps.scoreit.games.Player;
import com.sbgapps.scoreit.widget.SeekbarPoints;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sbaiget on 07/12/13.
 */
public class TarotLapFragment extends LapFragment
        implements SeekbarPoints.OnPointsChangedListener {

    @InjectView(R.id.spinner_taker)
    Spinner mTaker;
    @InjectView(R.id.spinner_bid)
    Spinner mBid;
    @InjectView(R.id.checkbox_petit)
    CheckBox mPetit;
    @InjectView(R.id.checkbox_twenty_one)
    CheckBox mTwentyOne;
    @InjectView(R.id.checkbox_excuse)
    CheckBox mExcuse;
    @InjectView(R.id.input_points)
    SeekbarPoints mPoints;
    @InjectView(R.id.ll_bonuses)
    LinearLayout mBonuses;
    @InjectView(R.id.btn_add_bonus)
    Button mButtonBonus;
    Spinner mPartner;

    @Override
    public TarotLap getLap() {
        return (TarotLap) super.getLap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lap_tarot, null);
        ButterKnife.inject(this, view);

        mTaker.setAdapter(getPlayerArrayAdapter());
        mTaker.setSelection(getLap().getTaker());
        mTaker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getLap().setTaker(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (getGameHelper().getPlayerCount() == 5) {
            ViewStub stub = (ViewStub) view.findViewById(R.id.viewstub_partner);
            View v = stub.inflate();
            final ArrayAdapter<PlayerItem> partnerItemArrayAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item);
            partnerItemArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            partnerItemArrayAdapter.add(new PlayerItem(Player.PLAYER_1));
            partnerItemArrayAdapter.add(new PlayerItem(Player.PLAYER_2));
            partnerItemArrayAdapter.add(new PlayerItem(Player.PLAYER_3));
            partnerItemArrayAdapter.add(new PlayerItem(Player.PLAYER_4));
            partnerItemArrayAdapter.add(new PlayerItem(Player.PLAYER_5));
            mPartner = (Spinner) v.findViewById(R.id.spinner_partner);
            mPartner.setAdapter(partnerItemArrayAdapter);
            mPartner.setSelection(((TarotFiveLap) getLap()).getPartner());
            mPartner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TarotFiveLap) getLap()).setPartner(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        final ArrayAdapter<BidItem> bidItemArrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item);
        bidItemArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bidItemArrayAdapter.add(new BidItem(TarotBid.BID_PRISE));
        bidItemArrayAdapter.add(new BidItem(TarotBid.BID_GARDE));
        bidItemArrayAdapter.add(new BidItem(TarotBid.BID_GARDE_SANS));
        bidItemArrayAdapter.add(new BidItem(TarotBid.BID_GARDE_CONTRE));
        mBid.setAdapter(bidItemArrayAdapter);
        mBid.setSelection(getLap().getBid().get());
        mBid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getLap().setBid(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPoints.init(getLap().getPoints(), 91, getLap().getPoints());
        mPoints.setOnPointsChangedListener(this, "points");

        mPetit.setChecked((getLap().getOudlers() & TarotLap.OUDLER_PETIT_MSK)
                == TarotLap.OUDLER_PETIT_MSK);
        mPetit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int oudlers = getLap().getOudlers();
                if (isChecked) {
                    getLap().setOudlers(oudlers | TarotLap.OUDLER_PETIT_MSK);
                } else {
                    getLap().setOudlers(oudlers & ~TarotLap.OUDLER_PETIT_MSK);
                }
            }
        });
        mExcuse.setChecked((getLap().getOudlers() & TarotLap.OUDLER_EXCUSE_MSK)
                == TarotLap.OUDLER_EXCUSE_MSK);
        mExcuse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int oudlers = getLap().getOudlers();
                if (isChecked) {
                    getLap().setOudlers(oudlers | TarotLap.OUDLER_EXCUSE_MSK);
                } else {
                    getLap().setOudlers(oudlers & ~TarotLap.OUDLER_EXCUSE_MSK);
                }
            }
        });
        mTwentyOne.setChecked((getLap().getOudlers() & TarotLap.OUDLER_21_MSK)
                == TarotLap.OUDLER_21_MSK);
        mTwentyOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int oudlers = getLap().getOudlers();
                if (isChecked) {
                    getLap().setOudlers(oudlers | TarotLap.OUDLER_21_MSK);
                } else {
                    getLap().setOudlers(oudlers & ~TarotLap.OUDLER_21_MSK);
                }
            }
        });

        mButtonBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBonus(null);
            }
        });
        for (TarotBonus bonus : getLap().getBonuses()) {
            addBonus(bonus);
        }
        return view;
    }

    private void addBonus(TarotBonus tarotBonus) {
        List<TarotBonus> bonuses = getLap().getBonuses();
        if (null == tarotBonus) {
            boolean petit = false;
            boolean poignee = false;
            boolean chelem = false;
            for (TarotBonus bonus : bonuses) {
                if (bonus.get() == TarotBonus.BONUS_PETIT_AU_BOUT) {
                    petit = true;
                } else if (bonus.get() == TarotBonus.BONUS_POIGNEE_SIMPLE ||
                        bonus.get() == TarotBonus.BONUS_POIGNEE_DOUBLE ||
                        bonus.get() == TarotBonus.BONUS_POIGNEE_TRIPLE) {
                    poignee = true;
                } else if (bonus.get() == TarotBonus.BONUS_CHELEM_NON_ANNONCE ||
                        bonus.get() == TarotBonus.BONUS_CHELEM_ANNONCE_REALISE ||
                        bonus.get() == TarotBonus.BONUS_CHELEM_ANNONCE_NON_REALISE) {
                    chelem = true;
                }
            }
            tarotBonus = !petit ? new TarotBonus(TarotBonus.BONUS_PETIT_AU_BOUT) :
                    !poignee ? new TarotBonus(TarotBonus.BONUS_POIGNEE_SIMPLE) :
                            !chelem ? new TarotBonus(TarotBonus.BONUS_CHELEM_NON_ANNONCE) : null;
            bonuses.add(tarotBonus);
            mButtonBonus.setEnabled(bonuses.size() < 3);
        }
        final TarotBonus bonus = tarotBonus;

        final View view = getActivity().getLayoutInflater()
                .inflate(R.layout.list_item_bonus, mBonuses, false);
        ImageButton btn = (ImageButton) view.findViewById(R.id.btn_remove_announce);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLap().getBonuses().remove(bonus);
                mButtonBonus.setEnabled(getLap().getBonuses().size() < 3);
                mBonuses.removeView(view);
            }
        });

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_announce);
        spinner.setAdapter(getBonusArrayAdapter());
        spinner.setSelection(bonus.get());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bonus.set(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner = (Spinner) view.findViewById(R.id.spinner_player);
        ArrayAdapter<PlayerItem> aa = getPlayerArrayAdapter();
        spinner.setAdapter(aa);
        spinner.setSelection(bonus.getPlayer());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bonus.setPlayer(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int pos = mBonuses.getChildCount() - 1;
        mBonuses.addView(view, pos);
    }

    private ArrayAdapter<PlayerItem> getPlayerArrayAdapter() {
        ArrayAdapter<PlayerItem> playerItemArrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item);
        playerItemArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerItemArrayAdapter.add(new PlayerItem(Player.PLAYER_1));
        playerItemArrayAdapter.add(new PlayerItem(Player.PLAYER_2));
        playerItemArrayAdapter.add(new PlayerItem(Player.PLAYER_3));
        switch (getGameHelper().getPlayerCount()) {
            case 4:
                playerItemArrayAdapter.add(new PlayerItem(Player.PLAYER_4));
                break;
            case 5:
                playerItemArrayAdapter.add(new PlayerItem(Player.PLAYER_4));
                playerItemArrayAdapter.add(new PlayerItem(Player.PLAYER_5));
                break;
        }
        return playerItemArrayAdapter;
    }

    private ArrayAdapter<BonusItem> getBonusArrayAdapter() {
        final ArrayAdapter<BonusItem> announceItemArrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item);
        announceItemArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        announceItemArrayAdapter.add(new BonusItem(TarotBonus.BONUS_PETIT_AU_BOUT));
        announceItemArrayAdapter.add(new BonusItem(TarotBonus.BONUS_POIGNEE_SIMPLE));
        announceItemArrayAdapter.add(new BonusItem(TarotBonus.BONUS_POIGNEE_DOUBLE));
        announceItemArrayAdapter.add(new BonusItem(TarotBonus.BONUS_POIGNEE_TRIPLE));
        announceItemArrayAdapter.add(new BonusItem(TarotBonus.BONUS_CHELEM_NON_ANNONCE));
        announceItemArrayAdapter.add(new BonusItem(TarotBonus.BONUS_CHELEM_ANNONCE_REALISE));
        announceItemArrayAdapter.add(new BonusItem(TarotBonus.BONUS_CHELEM_ANNONCE_NON_REALISE));
        return announceItemArrayAdapter;
    }

    @Override
    public int onPointsChanged(int progress, String tag) {
        getLap().setPoints(progress);
        return progress;
    }

    class BidItem {

        final int mBid;

        BidItem(int bid) {
            mBid = bid;
        }

        @Override
        public String toString() {
            return TarotBid.getLitteralBid(getActivity(), mBid);
        }
    }

    class PlayerItem {

        final int mPlayer;

        public PlayerItem(int player) {
            mPlayer = player;
        }

        @Override
        public String toString() {
            return getGameHelper().getPlayer(mPlayer).getName();
        }
    }

    class BonusItem {

        final int mBonus;

        BonusItem(int bonus) {
            mBonus = bonus;
        }

        @Override
        public String toString() {
            return TarotBonus.getLitteralBonus(getActivity(), mBonus);
        }
    }
}