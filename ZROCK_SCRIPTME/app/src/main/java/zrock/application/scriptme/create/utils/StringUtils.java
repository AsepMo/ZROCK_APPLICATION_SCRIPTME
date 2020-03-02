package zrock.application.scriptme.create.utils;

import zrock.application.scriptme.R;

/**
 * This class provides some easy of use methods for strings
 */
public class StringUtils {
   public static boolean isNullorEmpty(String str) {
      if ((str != null) && (str != "")) {
         return false;
      }
      return true;
   }
}
