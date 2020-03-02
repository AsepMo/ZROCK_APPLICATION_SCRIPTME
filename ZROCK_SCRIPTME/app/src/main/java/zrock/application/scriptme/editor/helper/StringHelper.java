package zrock.application.scriptme.editor.helper;

public final class StringHelper {

    private StringHelper() {
    }

    public static String join(final String... strings) {
        final StringBuffer buffer = new StringBuffer();
        for (String string : strings) {
            if (!string.endsWith("/")) {
                string += "/";
            }
            buffer.append(string);
        }
        String result = buffer.toString();
        if (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
