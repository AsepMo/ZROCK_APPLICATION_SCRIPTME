package zrock.application.scriptme.create.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

/**
 * This class reads in an InputStream when Command creates a process
 */
public class StreamGobbler extends Thread {
   @SuppressWarnings("unused")
   private final String m_name;

   private final InputStream m_stream;

   private String m_output = "";

   private boolean m_isFinished = false;

   /**
    * Constructor
    * 
    * @param stream
    */
   public StreamGobbler(String name, InputStream stream) {
      m_name = name;
      m_stream = stream;
   }

   /**
    * @return True if done, false otherwise
    */
   public boolean done() {
      return m_isFinished;
   }

   /**
    * @return The output of the InputStream
    */
   public String getOutput() {
      synchronized (this) {
         while (!m_isFinished) {
            try {
               wait();
            } catch (InterruptedException e) {/* do nothing */}
         }
         return m_output;
      }
   }

   /**
    * @see java.lang.Thread#run()
    */
   @Override
   public void run() {
      InputStreamReader isr = new InputStreamReader(m_stream);
      BufferedReader br = new BufferedReader(isr);
      String line = null;
      try {
         if ((line = br.readLine()) != null) {
            m_output = line;
         }
         while ((line = br.readLine()) != null) {
            m_output += "\n" + line;
         }
      } catch (IOException e) {
         Log.d("StreamGobbler", "run", e);
      } catch (OutOfMemoryError e) {
         Log.d("StreamGobbler", "run", e);
      }

      m_isFinished = true;
      synchronized (this) {
         notifyAll();
      }
   }
}
