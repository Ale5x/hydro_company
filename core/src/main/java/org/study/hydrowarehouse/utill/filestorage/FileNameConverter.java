package org.study.hydrowarehouse.utill.filestorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The service {@link FileNameConverter} responsible for converting and formatting file names.
 *
 * @author Aliaksandr Pishchala
 */
@Service
public class FileNameConverter {

    private final FileNameValidator validator;
    private final FileNameTransliterator transliterator;
    private final FileNameFormatter formatter;

    @Autowired
    public FileNameConverter(FileNameValidator validator, FileNameTransliterator transliterator, FileNameFormatter formatter) {
        this.validator = validator;
        this.transliterator = transliterator;
        this.formatter = formatter;
    }

    public String sanitizeAndTransliterateFileName(String fileName) {
        validator.validateFileName(fileName);

        int dotIndex = fileName.lastIndexOf(".");
        String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);

        validator.validateAllowedExtension(extension);

        String processedName = transliterator.transliterate(baseName);
        processedName = formatter.replaceSpaces(processedName);
        processedName = formatter.removeSpecialCharacters(processedName);

        return processedName + extension;
    }
}