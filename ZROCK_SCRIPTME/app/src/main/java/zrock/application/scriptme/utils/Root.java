package zrock.application.scriptme.utils;

import zrock.application.scriptme.R;

/**
 * This class is a singleton that determines if root access can be achieved.
 */
public class Root {
   private static boolean IS_ROOT = false;

   /**
    * @return Is root access available
    */
   public static boolean isRoot() {
      return IS_ROOT;
   }

   /**
    * Check if root access is available
    * 
    * @return Is root access available
    */
   public static boolean requestRoot() {
      IS_ROOT = false;

      try {
         String root = Shell.executeAsRoot("id | grep root").toLowerCase();
         if ((root != null) && (root != "")) {
            IS_ROOT = true;
         }
      } catch (Exception e) {/* do nothing */}

      return IS_ROOT;
   }

   /**
    * Set if root access is available
    * 
    * @param isRoot
    */
   public static void setRoot(boolean isRoot) {
      IS_ROOT = isRoot;
   }
}
