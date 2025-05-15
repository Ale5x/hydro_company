package org.study.hydrowarehouse.utill;

/**
 * An interface {@link FileNameFormatter} for formatting file names by applying basic transformations to strings.
 *
 * @author Aliaksandr Pishchala
 */
public interface FileNameFormatter {

    /**
     * Removes special characters from the given text. Special characters might include symbols like @, #, $, %, etc.
     *
     * @param text the input string to process
     * @return a new string with special characters removed
     */
    String removeSpecialCharacters(String text);

    /**
     * Replaces spaces in the given text with another character or pattern.
     *
     * @param text the input string to process
     * @return a new string with spaces replaced
     */
    String replaceSpaces(String text);
}
