package zrock.application.scriptme.create;

import zrock.application.scriptme.R;

/**
 * This class just holds a Script object for sharing between activities.
 */
public class ScriptSingleton {
   private static Script INSTANCE;

   /**
    * @return The singleton instance.
    */
   public static Script getInstance() {
      return INSTANCE;
   }

   /**
    * Sets the singleton instance.
    * 
    * @param s
    */
   public static void setInstance(Script s) {
      INSTANCE = s;
   }
}
