package org.study.hydro.utill;

/**
 * Interface {@link FileNameTransliterator} for transliterating file names. Intended to convert characters from one
 * alphabet to another (e.g., from Cyrillic to Latin).
 *
 * @author Aliaksandr Pishchala
 */
public interface FileNameTransliterator {

    /**
     * Transliterates the given file name.
     * @param fileName the original file name containing characters that need to be transliterated.
     * @return the transliterated file name in the target alphabet (e.g., Latin).
     */
    String transliterate(String fileName);
}
