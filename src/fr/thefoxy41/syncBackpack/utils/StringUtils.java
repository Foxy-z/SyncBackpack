package fr.thefoxy41.syncBackpack.utils;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {

    /**
     * Join arguments of a list (for sql query)
     *
     * @param separator String
     * @param strings   List
     * @return String
     */
    public static String join(String separator, List<String> strings) {
        return removeLastChars(
                strings.stream()
                        .map(string -> string + separator)
                        .collect(Collectors.joining()), separator.length()
        );
    }

    /**
     * Join two lists of strings with separators and connectors (for sql query)
     *
     * @param argsSeparator String
     * @param argsLink      String
     * @param strings1      List
     * @param strings2      List
     * @return String
     * @throws InvalidParameterException if list are empty or not of the same size
     */
    public static String join(String argsSeparator, String argsLink, List<String> strings1, List<String> strings2) throws InvalidParameterException {
        // check empty
        if (strings1.isEmpty() || strings2.isEmpty()) {
            throw new InvalidParameterException("Arguments list on insert can not be empty");
        }

        // check different
        if (strings1.size() != strings2.size()) {
            throw new InvalidParameterException("Parameters and values sizes must be the same");
        }

        // join elements
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < strings1.size(); i++) {
            result.append(strings1.get(i))
                    .append(argsLink)
                    .append(strings2.get(i))
                    .append(argsSeparator);
        }

        // remove last separator
        result = new StringBuilder(removeLastChars(result.toString(), argsSeparator.length()));

        return result.toString();
    }

    /**
     * Remove x last chars of a string
     *
     * @param str    String
     * @param amount int
     * @return String
     * @noinspection WeakerAccess
     */
    public static String removeLastChars(String str, int amount) {
        return str.substring(0, str.length() - amount);
    }
}
