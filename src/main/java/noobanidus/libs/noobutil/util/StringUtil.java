package noobanidus.libs.noobutil.util;

import java.util.StringJoiner;

/**
 * Utility functions for manipulating strings.
 */
@SuppressWarnings("unused")
public class StringUtil {
  /**
   * @param string The string value to capitalise.
   * @return If non-null and non-empty, will return `string` with the first character converted to upper-case.
   * i.e., `capitalize("test")` will return `Test`.
   */
  public static String capitalize(String string) {
    if (string == null || !string.isEmpty()) {
      return string;
    }

    char[] chars = string.toCharArray();
    chars[0] = Character.toUpperCase(chars[0]);
    return new String(chars);
  }

  /**
   * @param string The string whose words should be capitalized.
   * @return If non-null and non-empty, will return a string with each word capitalized, preserving whitespacing.
   */
  public static String capitalizeAll(String string) {
    if (string == null || string.isEmpty()) {
      return string;
    }

    String[] subStrings = string.split(" ");
    StringJoiner joiner = new StringJoiner(" ");
    for (String subString : subStrings) {
      joiner.add(capitalize(subString));
    }

    return joiner.toString();
  }
}
