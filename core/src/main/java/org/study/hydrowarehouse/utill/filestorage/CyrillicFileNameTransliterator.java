package org.study.hydrowarehouse.utill.filestorage;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * Service implementation of {@link FileNameTransliterator} that transliterates file names from Cyrillic
 * to Latin characters.
 *
 * @see FileNameTransliterator
 *
 * @author Aliaksandr Pishchala
 */
@Service
public class CyrillicFileNameTransliterator implements FileNameTransliterator {

    private static final Map<Character, String> TRANSLIT_MAP = new HashMap<>();

    static {
        String[][] translitPairs = {
                {"А", "A"}, {"Б", "B"}, {"В", "V"}, {"Г", "G"}, {"Д", "D"},
                {"Е", "E"}, {"Ё", "E"}, {"Ж", "Zh"}, {"З", "Z"}, {"И", "I"},
                {"Й", "Y"}, {"К", "K"}, {"Л", "L"}, {"М", "M"}, {"Н", "N"},
                {"О", "O"}, {"П", "P"}, {"Р", "R"}, {"С", "S"}, {"Т", "T"},
                {"У", "U"}, {"Ф", "F"}, {"Х", "Kh"}, {"Ц", "Ts"}, {"Ч", "Ch"},
                {"Ш", "Sh"}, {"Щ", "Shch"}, {"Ы", "Y"}, {"Э", "E"}, {"Ю", "Yu"},
                {"Я", "Ya"}, {"Ъ", ""}, {"Ь", ""},
                {"а", "a"}, {"б", "b"}, {"в", "v"}, {"г", "g"}, {"д", "d"},
                {"е", "e"}, {"ё", "e"}, {"ж", "zh"}, {"з", "z"}, {"и", "i"},
                {"й", "y"}, {"к", "k"}, {"л", "l"}, {"м", "m"}, {"н", "n"},
                {"о", "o"}, {"п", "p"}, {"р", "r"}, {"с", "s"}, {"т", "t"},
                {"у", "u"}, {"ф", "f"}, {"х", "kh"}, {"ц", "ts"}, {"ч", "ch"},
                {"ш", "sh"}, {"щ", "shch"}, {"ы", "y"}, {"э", "e"}, {"ю", "yu"},
                {"я", "ya"}, {"ъ", ""}, {"ь", ""}
        };

        for (String[] pair : translitPairs) {
            TRANSLIT_MAP.put(pair[0].charAt(0), pair[1]);
        }
    }

    @Override
    public String transliterate(String fileName) {
        StringBuilder translitName = new StringBuilder();
        for (char ch : fileName.toCharArray()) {
            translitName.append(TRANSLIT_MAP.getOrDefault(ch, String.valueOf(ch)));
        }
        return translitName.toString();
    }
}
