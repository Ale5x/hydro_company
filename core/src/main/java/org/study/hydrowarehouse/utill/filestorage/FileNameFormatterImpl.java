package org.study.hydrowarehouse.utill.filestorage;

import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link FileNameFormatter} interface.
 *
 * @see FileNameFormatter
 *
 * @author Aliaksandr Pishchala
 */
@Service
public class FileNameFormatterImpl implements FileNameFormatter {
    private static final String INVALID_CHARACTERS_PATTERN = "[^a-zA-Z0-9._-]";

    @Override
    public String removeSpecialCharacters(String text) {
        return text.replaceAll(INVALID_CHARACTERS_PATTERN, "_");
    }

    @Override
    public String replaceSpaces(String text) {
        return text.replaceAll("\\s+", "_");
    }
}