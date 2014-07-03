/*
 * Copyright 2013 SBG Apps
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.sbgapps.scoreit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sbgapps.scoreit.util.TypefaceSpan;
import com.sbgapps.scoreit.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AboutActivity extends BaseActivity {

    private InfoFragment mInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupFauxDialog();
        setContentView(R.layout.activity_about);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(sectionsPagerAdapter);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);

        TypefaceSpan typefaceSpan = new TypefaceSpan(this, "Lobster.otf");
        SpannableString title = new SpannableString(getResources().getString(R.string.about));
        title.setSpan(typefaceSpan, 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mInfoFragment.getBillingProcessor().handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    public static class TranslationsFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_translations, container, false);
            ListView listView = (ListView) view.findViewById(R.id.list_view);

            String[] from = new String[]{"language", "translator"};
            int[] to = new int[]{R.id.language, R.id.translator};

            List<HashMap<String, String>> data = new ArrayList<>();
            HashMap<String, String> map;
            String[] l = getResources().getStringArray(R.array.languages);
            String[] t = getResources().getStringArray(R.array.translators);
            for (int i = 0; i < l.length; i++) {
                map = new HashMap<>();
                map.put("language", l[i]);
                map.put("translator", t[i]);
                data.add(map);
            }

            listView.setAdapter(new SimpleAdapter(getActivity(),
                    data, R.layout.list_item_translation, from, to));

            Button btn = (Button) view.findViewById(R.id.btn_translate);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.getlocalization.com/ScoreIt/"));
                    startActivity(intent);
                }
            });

            return view;
        }
    }

    public static class LicensesFragment extends ListFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            String[] from = new String[]{"library", "license"};
            int[] to = new int[]{R.id.library, R.id.license_text};

            List<HashMap<String, String>> data = new ArrayList<>();
            HashMap<String, String> map;
            String[] lib = getResources().getStringArray(R.array.libraries);
            String[] lic = getResources().getStringArray(R.array.lib_licenses);
            for (int i = 0; i < lib.length; i++) {
                map = new HashMap<>();
                map.put("library", lib[i]);
                map.put("license", lic[i]);
                data.add(map);
            }

            setListAdapter(new SimpleAdapter(getActivity(),
                    data, R.layout.list_item_license, from, to));
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    mInfoFragment = (InfoFragment) Fragment.instantiate(AboutActivity.this,
                            InfoFragment.class.getName());
                    fragment = mInfoFragment;
                    break;
                case 1:
                    fragment = Fragment.instantiate(AboutActivity.this,
                            TranslationsFragment.class.getName());
                    break;
                case 2:
                    fragment = Fragment.instantiate(AboutActivity.this,
                            LicensesFragment.class.getName());
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.application).toUpperCase(l);
                case 1:
                    return getString(R.string.translations).toUpperCase(l);
                case 2:
                    return getString(R.string.licenses).toUpperCase(l);
            }
            return null;
        }
    }
}