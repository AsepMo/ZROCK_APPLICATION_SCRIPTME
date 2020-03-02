package zrock.application.scriptme.editor.dialog;

import zrock.application.scriptme.R;


import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// ...
public class EditTextDialog extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mEditText;

    public static EditTextDialog newInstance(final String hint) {
        final EditTextDialog f = new EditTextDialog();

        // Supply num input as an argument.
        final Bundle args = new Bundle();
        args.putString("hint", hint);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        final Dialog dialog = getDialog();
        final String title = getString(R.string.codifica);
        dialog.setTitle(title);

        final View view = inflater.inflate(R.layout.dialog_fragment_edittext, container);
        this.mEditText = (EditText) view.findViewById(android.R.id.edit);

        // Show soft keyboard automatically
        this.mEditText.setText(getArguments().getString("hint"));
        this.mEditText.requestFocus();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        this.mEditText.setOnEditorActionListener(this);

        final Button button = (Button) view.findViewById(android.R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                returnData();
            }
        });

        return view;
    }

    void returnData() {
        EditDialogListener target = (EditDialogListener) getTargetFragment();
        if (target == null) {
            target = (EditDialogListener) getActivity();
        }
        target.onFinishEditDialog(this.mEditText.getText().toString(),
                (Actions) getArguments().getSerializable("action"), getArguments().getString("hint"));
        this.dismiss();
    }

    @Override
    public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            returnData();
            return true;
        }
        return false;
    }

    public enum Actions {
        NewRemoteFolder, NewRemoteFile, NewLocalFolder, Rename, Move, EditEncoding
    }

    public interface EditDialogListener {
        void onFinishEditDialog(String inputText, Actions action, String hint);
    }
}


