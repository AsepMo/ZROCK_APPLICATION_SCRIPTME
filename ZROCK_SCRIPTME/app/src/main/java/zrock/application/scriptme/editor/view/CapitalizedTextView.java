package zrock.application.scriptme.editor.view;

import zrock.application.scriptme.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CapitalizedTextView extends TextView {

    public CapitalizedTextView(final Context context) {
        super(context);
    }

    public CapitalizedTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public CapitalizedTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(final CharSequence text, final BufferType type) {
        super.setText(text.toString().toUpperCase(), type);
    }
}
