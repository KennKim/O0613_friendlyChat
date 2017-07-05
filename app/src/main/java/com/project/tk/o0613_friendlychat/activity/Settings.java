package com.project.tk.o0613_friendlychat.activity;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.text.TextUtils;
import android.view.Menu;

import com.project.tk.o0613_friendlychat.R;
import com.project.tk.o0613_friendlychat.util.SharedPre;

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);

        addPreferencesFromResource(R.xml.appsetting);

        setOnPreferenceChange(findPreference(getString(R.string.pref_key_user_name)));
        setOnPreferenceChange(findPreference(getString(R.string.pref_key_user_name_open)));
        setOnPreferenceChange(findPreference(getString(R.string.pref_key_autoUpdate_ringtone)));
        setOnPreferenceChange(findPreference(getString(R.string.pref_key_set_ringtone)));
        setOnPreferenceChange(findPreference("myPrefTest"));
        setOnPreferenceChange(findPreference("myPrefTest2"));

        Preference myPre = findPreference(getString(R.string.pref_key_user_name));
        myPre.setSummary(SharedPre.getInstance().getString(SharedPre.DISPLAY_NAME,null));

/*
        Toolbar bar;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
            bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
            root.addView(bar, 0); // insert at top
            bar.setTitleTextColor(android.graphics.Color.WHITE);

        } else {
            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);

            root.removeAllViews();

            bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);


            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }else{
                height = bar.getHeight();
            }

            content.setPadding(0, height, 0, 0);

            root.addView(content);
            root.addView(bar);
        }

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setOnPreferenceChange(Preference mPreference) {
        mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
        onPreferenceChangeListener.onPreferenceChange(mPreference, PreferenceManager.getDefaultSharedPreferences(mPreference.getContext()).getString(mPreference.getKey(), ""));
    }

    private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if (preference instanceof EditTextPreference) {
                preference.setSummary(stringValue);

                SharedPre.getInstance().putString(SharedPre.DISPLAY_NAME,stringValue);

            } else if (preference instanceof ListPreference) {
                /**
                 * ListPreference의 경우 stringValue가 entryValues이기 때문에 바로 Summary를
                 * 적용하지 못한다 따라서 설정한 entries에서 String을 로딩하여 적용한다
                 */

                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

            } else if (preference instanceof SwitchPreference) {


            } else if (preference instanceof RingtonePreference) {
                /**
                 * RingtonePreference의 경우 stringValue가
                 * content://media/internal/audio/media의 형식이기 때문에
                 * RingtoneManager을 사용하여 Summary를 적용한다
                 *
                 * 무음일경우 ""이다
                 */

                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(getString(R.string.basic_ringtone));

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);

                    } else {
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                        SharedPre.getInstance().putString(SharedPre.URI_RINGTONE,stringValue);
                    }
                }
            }
            return true;
        }
    };
}
