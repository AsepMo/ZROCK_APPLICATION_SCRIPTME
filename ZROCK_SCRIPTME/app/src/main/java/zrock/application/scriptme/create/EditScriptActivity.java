package zrock.application.scriptme.create;

import zrock.application.scriptme.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import zrock.application.scriptme.create.utils.Root;
import zrock.application.scriptme.create.utils.Shell;

import android.graphics.Color;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Used to edit the chosen Script
 */
public class EditScriptActivity extends Activity {
   private Script m_script;

   private CheckBox m_runAsRoot;

   private CheckBox m_hideOutput;

   private EditText m_scriptArgs;

   private EditText m_scriptContents;

   private String m_originalContents;

   /**
    * Initialize everything.
    */
   private void init() {
      // Set content to see
      setContentView(R.layout.activity_application_scriptme_edit_script);
	  getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.scriptme_background));
	   
      m_script = ScriptSingleton.getInstance();

      m_runAsRoot = (CheckBox) findViewById(R.id.run_as_root);
      if (Root.isRoot()) {
         m_runAsRoot.setChecked(ScriptSingleton.getInstance().runAsRoot());
      } else {
         m_runAsRoot.setVisibility(View.GONE);
      }

      m_hideOutput = (CheckBox) findViewById(R.id.hide_output);
      m_hideOutput.setChecked(m_script.hideOutput());

      m_scriptArgs = (EditText) findViewById(R.id.script_args);
      m_scriptArgs.setText(m_script.getArgs());
	  m_scriptArgs.setTextColor(Color.GREEN);
	  m_scriptArgs.setHintTextColor(Color.GRAY);
	  
      m_scriptContents = (EditText) findViewById(R.id.script_text);
	  m_scriptContents.setTextColor(Color.GREEN);
	   
	  try {
         m_originalContents = Shell.execute("cat " + m_script.getFullPath());
         m_scriptContents.setText(m_originalContents);
      } catch (Exception e) {
         toastError("Failed to get contents for " + m_script.getFullPath()
               + " b/c " + e.getMessage());
      }

      Button save = (Button) findViewById(R.id.save_button);
      save.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            m_script.runAsRoot(m_runAsRoot.isChecked())
                  .setArgs(m_scriptArgs.getText().toString().trim())
                  .hideOutput(m_hideOutput.isChecked());
            String contents = m_scriptContents.getText().toString().trim();
            if (!contents.equals(m_originalContents)) {
               try {
                  BufferedWriter bw = new BufferedWriter(new FileWriter(
                        new File(m_script.getFullPath())));
                  bw.write(contents);
                  bw.close();
                  setResult(RESULT_OK, new Intent());
                  finish();
               } catch (IOException e) {
                  toastError("Failed to save contents of file "
                        + m_script.getFullPath() + " b/c " + e.getMessage());
               }
            } else {
               setResult(RESULT_OK, new Intent());
               finish();
            }
         }
      });

      Button back = (Button) findViewById(R.id.back_button);
      back.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            finish();
         }
      });
   }

   /**
    * Called when the activity is first created.
    */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      init();
   }

   private void toastError(String err) {
      Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
   }

   // private void toastMessage(String msg) {
   // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
   // }
}
