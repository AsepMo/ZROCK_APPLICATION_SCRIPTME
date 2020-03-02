package zrock.application.scriptme.editor.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class PreferenceHelper {

    private PreferenceHelper() {
    }

    /**
     * Getter Methods
     */
    public static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPrefs(context).edit();
    }

    public static boolean getWrapText(Context context) {
        return getPrefs(context).getBoolean("editor_wrap_text", true);
    }

    public static boolean getSyntaxHiglight(Context context) {
        return getPrefs(context).getBoolean("editor_syntax_highlight", true);
    }

    public static String getEncoding(Context context) {
        return getPrefs(context).getString("editor_encoding", "UTF-8");
    }
    /**
     * Setter Methods
     */

    public static void setWrapText(Context context, boolean value) {
        getEditor(context).putBoolean("editor_wrap_text", value).commit();
    }

    public static void setSyntaxHiglight(Context context, boolean value) {
        getEditor(context).putBoolean("editor_syntax_highlight", value).commit();
    }

    public static void setEncoding(Context context, String value) {
        getEditor(context).putString("editor_encoding", value).commit();
    }
}
