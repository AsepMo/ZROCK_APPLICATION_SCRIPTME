package zrock.application.scriptme.create.dialogs;

import zrock.application.scriptme.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * The dialog created when the user clicks a script.
 */
public abstract class OutputDialog extends Dialog {
   public static final int ID = R.id.output_ok_button;

   /**
    * Constructor
    * 
    * @param activity
    */
   public OutputDialog(final Activity activity, String title) {
      super(activity);

      setContentView(R.layout.output_dialog);
      setTitle(title);

      final TextView output = (TextView) findViewById(R.id.output_text);
      output.setText(getOutput() + "\n\n");

      final Button okButton = (Button) findViewById(ID);
      okButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            onCloseButton();
         }
      });
   }

   /**
    * Get the output to display
    * 
    * @return The output
    */
   public abstract String getOutput();

   /**
    * Actions to take when the Close button is clicked
    */
   public abstract void onCloseButton();
}
