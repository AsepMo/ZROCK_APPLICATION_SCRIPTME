package zrock.application.scriptme.create.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * This class executes shell commands
 */
public class Shell {
   /**
    * Convenience method
    * 
    * @param command
    * @return stdout and stderr output from executed command
    * @throws Exception
    */
   public static String execute(List<String> commands) throws Exception {
      return execute(commands, false);
   }

   /**
    * Create a Shell to run a command
    * 
    * @param commands
    * @param root
    * @return stdout and stderr output from executed command
    * @throws Exception
    */
   public static String execute(List<String> commands, boolean root)
         throws Exception {
      Process p = null;
      try {
         if (root) {
            p = new ProcessBuilder("su").redirectErrorStream(true).start();
         } else {
            p = new ProcessBuilder("sh").redirectErrorStream(true).start();
         }

         DataOutputStream os = null;
         try {
             os = new DataOutputStream(p.getOutputStream());
             os.writeBytes("export PATH=/system/bin:/system/xbin:/data/local/bin;\n");
             for (final String command : commands) {
                 os.writeBytes(command + ";\n");
             }
             os.writeBytes("exit\n");
             os.flush();
         } finally {
             if (os != null) {
                 os.close();
             }
         } 
      } catch (IOException e) {
         Log.d("Shell", "execute", e);
      }

      if (p != null) {
         StreamGobbler stdout = new StreamGobbler("stdout", p.getInputStream());
         stdout.start();

         if (p.waitFor() != 0) {
            String error = "Commands failed:\n";
            for (String command : commands) {
               error += command + "\n";
            }
            error += "\n" + stdout.getOutput();
            throw new Exception(error);
         }

         return stdout.getOutput();
      } else {
         String error = "Commands not run:\n";
         for (String command : commands) {
            error += command + "\n";
         }
         throw new Exception(error);
      }
   }

   /**
    * Convenience method
    * 
    * @param command
    * @return stdout and stderr output from executed command
    * @throws Exception
    */
   public static String execute(String command) throws Exception {
      List<String> commands = new ArrayList<String>();
      commands.add(command);
      return execute(commands, false);
   }

   /**
    * Convenience method
    * 
    * @param commands
    * @return stdout and stderr output from executed command
    * @throws Exception
    */
   public static String executeAsRoot(List<String> commands) throws Exception {
      return execute(commands, true);
   }

   /**
    * Convenience method
    * 
    * @param command
    * @return stdout and stderr output from executed command
    * @throws Exception
    */
   public static String executeAsRoot(String command) throws Exception {
      List<String> commands = new ArrayList<String>();
      commands.add(command);
      return execute(commands, true);
   }
}
