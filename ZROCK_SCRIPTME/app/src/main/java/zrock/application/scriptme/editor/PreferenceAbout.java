package zrock.application.scriptme.editor;

import zrock.application.scriptme.R;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class PreferenceAbout extends PreferenceActivity {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about);
        setupClickablePreferences();
    }

    public void setupClickablePreferences() {
        final Preference email = findPreference("aboutactivity_authoremail"),
                changelog = findPreference("aboutactivity_changelog"),
                open_source_licenses = findPreference("aboutactivity_open_source_licenses"),
                market = findPreference("aboutactivity_authormarket");
        if (email != null) {
            email.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(final Preference preference) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"app.feedback.mail@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, getApplicationName(getBaseContext()) + " " + getCurrentVersion(getBaseContext()));
                    i.putExtra(Intent.EXTRA_TEXT, "");
                    try {
                        startActivity(Intent.createChooser(i, getString(R.string.aboutactivity_authoremail_summary)));
                    } catch (android.content.ActivityNotFoundException ex) {
                    }
                    return false;
                }
            });
        }
        if (changelog != null) {
            changelog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(final Preference preference) {
                    openDialogFragment(new DialogStandardFragment());
                    return false;
                }
            });
        }
        if (open_source_licenses != null) {
            open_source_licenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(final Preference preference) {
                    startActivity(new Intent(PreferenceAbout.this, LicensesActivity.class));
                    return false;
                }
            });
        }
        if (market != null) {
            market.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(final Preference preference) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Vlad+Mihalachi"))
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    } catch (Exception e) {
                    }
                    return false;
                }
            });
        }
    }

    private void openDialogFragment(DialogStandardFragment dialogStandardFragment) {
        if (dialogStandardFragment != null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("changelogdemo_dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            dialogStandardFragment.show(ft, "changelogdemo_dialog");
        }
    }

    public static String getApplicationName(final Context context) {
        final ApplicationInfo applicationInfo = context.getApplicationInfo();
        return context.getString(applicationInfo.labelRes);
    }

    public static String getCurrentVersion(final Context context) {
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }
}
