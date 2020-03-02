package zrock.application.scriptme.create.activities;

import zrock.application.scriptme.R;
import zrock.application.scriptme.create.utils.CheckBoxList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * This is an activity which just displays a list
 */
public abstract class ListActivity<T> extends Activity {

   protected CheckBoxList<T> m_list;

   /**
    * Something from the CheckBoxList was clicked
    * 
    * @param position
    */
   public abstract void listItemClick(int position);

   /**
    * Something from the CheckBoxList was long clicked
    * 
    * @param position
    */
   public abstract void listItemLongClick(int position);

   /**
    * @see android.app.Activity#onCreate(android.os.Bundle)
    */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      //setTheme(R.style.Theme_Application_ScriptMe);
      super.onCreate(savedInstanceState);

      // Set content to see
      setContentView(R.layout.list);
   }

   /**
    * Display loading screen
    * 
    * @param loading
    */
   protected void setLoading(boolean loading) {
      setLoading(loading, "Empty", 101);
   }

   /**
    * Display loading screen with message
    * 
    * @param loading
    * @param percent
    */
   protected void setLoading(boolean loading, int percent) {
      setLoading(loading, "Empty", percent);
   }

   /**
    * Display loading screen with message
    * 
    * @param loading
    * @param msg
    */
   protected void setLoading(boolean loading, String msg) {
      setLoading(loading, msg, 101);
   }

   /**
    * Display loading screen with message and progress bar
    * 
    * @param loading
    * @param msg
    * @param percent
    */
   protected void setLoading(boolean loading, String msg, int percent) {
      if (loading) {
         m_list.hide();

         ProgressBar pb = (ProgressBar) findViewById(R.id.progress_bar);
         if (percent <= 100) {
            pb.setMax(100);
            pb.setProgress(percent);
            pb.setVisibility(View.VISIBLE);
         }

         TextView tv = (TextView) findViewById(R.id.empty_list);
         tv.setText(msg);
         tv.setVisibility(View.VISIBLE);
      } else {
         ProgressBar pb = (ProgressBar) findViewById(R.id.progress_bar);
         pb.setVisibility(View.INVISIBLE);
         pb.setMax(100);
         pb.setProgress(0);

         if (!m_list.isEmpty()) {
            m_list.show();

            TextView tv = (TextView) findViewById(R.id.empty_list);
            tv.setVisibility(View.INVISIBLE);
            tv.setText(msg);
         } else {
            m_list.hide();

            TextView tv = (TextView) findViewById(R.id.empty_list);
            tv.setVisibility(View.VISIBLE);
            tv.setText(msg);
         }
      }
   }

   /**
    * Display error message in long toast.
    * 
    * @param err
    */
   public void toastError(String err) {
      Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
   }

   /**
    * Display message in short toast.
    * 
    * @param msg
    */
   public void toastMessage(String msg) {
      Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
   }
}
