/*
 * @file CsvUtils.java
 * @brief Class use for managing csv files
 *
 * @version 1.0
 * @date 20/07/2017
 * @author Guillaume MURET
 * @copyright
 *  Article 12 : Propriété de l’étude
 *  L'ensemble des techniques et méthodes de recherche demeure la propriété de SEIO et ne pourra
 *  faire l'objet d'aucune utilisation ou reproduction sans accord exprès. L’ensemble des travaux
 *  techniques et méthodologiques nécessaires à la réalisation de l’étude demeure toutefois la
 *  propriété exclusive de SEIO jusqu’au paiement global de l’étude, après quoi le résultat de
 *  l’étude sera la propriété exclusive du Client. SEIO, en accord avec le Client, archivera les
 *  données concernant l’étude sur support informatique et papier. Cependant, aucune utilisation
 *  ou reproduction des travaux ou études ne pourra se faire sans l’autorisation écrite du Client.
 *  Le client pourra exploiter ou faire exploiter les résultats de l'étude sans aucune rémunération
 *  au profit de SEIO autre que celle mentionnée dans l’article 5 de la présente Convention. SEIO
 *  se réserve le droit d'utiliser le nom et le logo du client à titre de référence.
 */
package com.seio.limagrain.fieldkeyboard.utils.file;

import android.graphics.Color;

import com.seio.limagrain.fieldkeyboard.model.DataStore;
import com.seio.limagrain.fieldkeyboard.model.KeyboardConfiguration;
import com.seio.limagrain.fieldkeyboard.model.ModelKey;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CsvUtils {

    // The different constant of the keyboard file
    private static final int CSV_FILE_KEYBOARD_CONFIGURATION_LINE_NUMBER = 3;
    private static final int CSV_FILE_KEYBOARD_LANGUAGE_NAME_POSITION = 3;

    // The CSV file name
    private static final String CSV_NAME_FILE = "CustomKeyboardConfig.csv";

    // The key of the CSV file
    private static final String KEY_KEYBOARD_CONFIGURATION = "KeyboardConfiguration";
    private static final String KEY_KEYBOARD_HEIGHT = "keyboardHeight";
    private static final String KEY_NB_LINE = "nbLine";
    private static final String KEY_NB_ROW = "nbRow";
    private static final String KEY_KEYBOARD_KEYS = "KeyboardKeys";
    private static final String KEY_FIXED_KEYS = "FixedKeys";
    private static final String KEY_BACKGROUND_COLOR = "keyBackgroundColor";
    private static final String KEY_FRONT_COLOR = "keyFrontColor";
    private static final String KEY_FRONT_SIZE = "keyFrontSize";
    private static final String KEY_ACTION = "keyAction";
    private static final String KEY_ICON = "keyIcon";

    /**
     * Process called to read Csv file and return an ArrayList of the file line by line
     *
     * @param keyboardConfigFile : the csv file path
     * @return an ArrayList of the file line by line
     */
    private static ArrayList<String[]> readCsvFile(File keyboardConfigFile) {
        ArrayList<String[]> stringCsvFile = new ArrayList<>();
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ";";
        try {
            br = new BufferedReader(new FileReader(keyboardConfigFile));
            while ((line = br.readLine()) != null) {
                stringCsvFile.add(line.split(cvsSplitBy));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringCsvFile;
    }

    /**
     * Process called to manage the imported keyboard file
     *
     * @param keyboardConfigFile : the csv file path
     */
    static void manageKeyboardConfigurationFileCSV(File keyboardConfigFile) {
        ArrayList<String[]> stringCsvFile = readCsvFile(keyboardConfigFile);
        DataStore.getInstance().setTmpKeyboardConfiguration(new KeyboardConfiguration());
        if (stringCsvFile.size() > 0) {
            for (int line = 0; line < stringCsvFile.size(); line++) {
                if (stringCsvFile.get(line).length > 0) {
                    if (stringCsvFile.get(line)[0].equals(KEY_KEYBOARD_CONFIGURATION)) {
                        manageKeyboardConfigurationStringCSV(line + 1, stringCsvFile);
                        line = line + CSV_FILE_KEYBOARD_CONFIGURATION_LINE_NUMBER;
                    } else if (stringCsvFile.get(line)[0].equals(KEY_KEYBOARD_KEYS)) {
                        line = manageKeyboardKeysStringCSV(line + 1, stringCsvFile);
                    } else if (stringCsvFile.get(line)[0].equals(KEY_FIXED_KEYS)) {
                        line = manageFixedKeysStringCSV(line + 1, stringCsvFile);
                    }
                }
            }
            DataStore.getInstance().setKeyboardConfiguration(DataStore.getInstance().getTmpKeyboardConfiguration());
            FileUtils.saveKeyboardConfiguration();
            FileUtils.manageDataActualization(DataStore.getInstance().getKeyboardConfiguration());
        }
    }

    /**
     * Process called to manage the KeyboardConfiguration paragraph in the CSV file
     *
     * @param line          : the current line
     * @param stringCsvFile : the ArrayList of the file line by line
     */
    private static void manageKeyboardConfigurationStringCSV(int line, ArrayList<String[]> stringCsvFile) {
        for (int i = 0; i < CSV_FILE_KEYBOARD_CONFIGURATION_LINE_NUMBER; i++) {
            switch (stringCsvFile.get(line + i)[0]) {
                case KEY_KEYBOARD_HEIGHT:
                    DataStore.getInstance().getTmpKeyboardConfiguration().setKeyboardHeight(Integer.valueOf(stringCsvFile.get(line + i)[1]));
                    break;

                case KEY_NB_LINE:
                    DataStore.getInstance().getTmpKeyboardConfiguration().setNbLine(Integer.valueOf(stringCsvFile.get(line + i)[1]));
                    break;

                case KEY_NB_ROW:
                    DataStore.getInstance().getTmpKeyboardConfiguration().setNbRow(Integer.valueOf(stringCsvFile.get(line + i)[1]));
                    break;
            }
        }
        DataStore.getInstance().getTmpKeyboardConfiguration().setSelectedLanguage(0);
    }

    /**
     * Process called to manage the first line of the KeyboardKeys paragraph in the CSV file
     * Use to instantiate the different language use in the app
     *
     * @param line          : the current line
     * @param stringCsvFile : ArrayList of the file line by line
     */
    private static void manageFirstLineKeyboardKeysStringCSV(int line, ArrayList<String[]> stringCsvFile) {
        ArrayList<String> arrayListLanquage = new ArrayList<>();
        for (int i = CSV_FILE_KEYBOARD_LANGUAGE_NAME_POSITION; i < stringCsvFile.get(line).length; i++) {
            arrayListLanquage.add(stringCsvFile.get(line)[i]);
        }
        DataStore.getInstance().getTmpKeyboardConfiguration().setKeyboardLanguages(arrayListLanquage);
    }

    /**
     * Manage the KeyboardKeys paragraph
     *
     * @param line          : the current line
     * @param stringCsvFile : ArrayList of the file line by line
     * @return the next current line (at the end of the paragraph)
     */
    private static int manageKeyboardKeysStringCSV(int line, ArrayList<String[]> stringCsvFile) {
        manageFirstLineKeyboardKeysStringCSV(line, stringCsvFile);
        line++;
        while (line < stringCsvFile.size() && stringCsvFile.get(line).length > 0 && stringCsvFile.get(line)[0].substring(0, 1).equals("#")) {
            ModelKey currentModelKey = new ModelKey(
                    stringCsvFile.get(line)[CSV_FILE_KEYBOARD_LANGUAGE_NAME_POSITION],
                    Color.parseColor(stringCsvFile.get(line)[0]),
                    Color.parseColor(stringCsvFile.get(line)[1]),
                    Integer.valueOf(stringCsvFile.get(line)[2]),
                    DataStore.ACTION_SIMPLE_KEY,
                    -1
            );
            for (int i = CSV_FILE_KEYBOARD_LANGUAGE_NAME_POSITION + 1; i < stringCsvFile.get(line).length; i++) {
                currentModelKey.getKeyLanguages().add(stringCsvFile.get(line)[i]);
            }
            DataStore.getInstance().getTmpKeyboardConfiguration().getModelKeyboardKeys().add(currentModelKey);
            line++;
        }
        DataStore.getInstance().getTmpKeyboardConfiguration().setSelectedLanguage(0);
        return line - 1;
    }

    /**
     * Manage the FixedKeys paragraph
     *
     * @param line          : the current line
     * @param stringCsvFile : ArrayList of the file line by line
     * @return the next current line (at the end of the paragraph)
     */
    private static int manageFixedKeysStringCSV(int line, ArrayList<String[]> stringCsvFile) {
        line++;
        while (line < stringCsvFile.size() && stringCsvFile.get(line).length > 0 && stringCsvFile.get(line)[0].substring(0, 1).equals("#")) {
            ModelKey currentModelKey = new ModelKey(
                    stringCsvFile.get(line)[5],
                    Color.parseColor(stringCsvFile.get(line)[0]),
                    Color.parseColor(stringCsvFile.get(line)[1]),
                    Integer.valueOf(stringCsvFile.get(line)[2]),
                    stringCsvFile.get(line)[3],
                    Integer.valueOf(stringCsvFile.get(line)[4])
            );
            DataStore.getInstance().getTmpKeyboardConfiguration().getModelParameterKeys().add(currentModelKey);
            line++;
        }
        DataStore.getInstance().getTmpKeyboardConfiguration().setSelectedLanguage(0);
        return line - 1;
    }

    /**
     * Process called to export Csv file in the CustomKeyboardConfig directory
     */
    static void exportCsvFile() {
        ArrayList<String[]> stringCsvFile = new ArrayList<>();

        // Keyboard configuration
        stringCsvFile = manageExportKeyboardConfiguration(stringCsvFile);

        // Keyboard keys
        stringCsvFile = manageExportKeyboardKeys(stringCsvFile);

        // Fixed keys
        stringCsvFile = manageExportFixedKeys(stringCsvFile);

        // Export file
        generateCsvFile(stringCsvFile);
    }

    /**
     * Manage the exportation of the KeyboardConfiguration paragraph of the CSV file
     * @param stringCsvFile : the ArrayList of the file line by line
     * @return the ArrayList of the file line by line with the new paragraph
     */
    private static ArrayList<String[]> manageExportKeyboardConfiguration(ArrayList<String[]> stringCsvFile) {
        stringCsvFile.add(new String[]{KEY_KEYBOARD_CONFIGURATION});
        stringCsvFile.add(new String[]{KEY_KEYBOARD_HEIGHT, String.valueOf(DataStore.getInstance().getKeyboardConfiguration().getKeyboardHeight())});
        stringCsvFile.add(new String[]{KEY_NB_LINE, String.valueOf(DataStore.getInstance().getKeyboardConfiguration().getNbLine())});
        stringCsvFile.add(new String[]{KEY_NB_ROW, String.valueOf(DataStore.getInstance().getKeyboardConfiguration().getNbRow())});

        // Add \n
        stringCsvFile.add(new String[]{});
        return stringCsvFile;
    }

    /**
     * Manage the exportation of the KeyboardKeys paragraph of the CSV file
     * @param stringCsvFile : the ArrayList of the file line by line
     * @return the ArrayList of the file line by line with the new paragraph
     */
    private static ArrayList<String[]> manageExportKeyboardKeys(ArrayList<String[]> stringCsvFile) {
        stringCsvFile.add(new String[]{KEY_KEYBOARD_KEYS});
        String[] strKeyboardKeysLineTitle = new String[DataStore.getInstance().getKeyboardConfiguration().getKeyboardLanguages().size() + 3];
        strKeyboardKeysLineTitle[0] = KEY_BACKGROUND_COLOR;
        strKeyboardKeysLineTitle[1] = KEY_FRONT_COLOR;
        strKeyboardKeysLineTitle[2] = KEY_FRONT_SIZE;
        for (int i = 0; i < DataStore.getInstance().getKeyboardConfiguration().getKeyboardLanguages().size(); i++) {
            strKeyboardKeysLineTitle[3 + i] = DataStore.getInstance().getKeyboardConfiguration().getKeyboardLanguages().get(i);
        }
        stringCsvFile.add(strKeyboardKeysLineTitle);

        for (int i = 0; i < DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().size(); i++) {
            String[] strLineContent = new String[DataStore.getInstance().getKeyboardConfiguration().getKeyboardLanguages().size() + 3];
            strLineContent[0] = String.format("#%06X", (0xFFFFFF & DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i).getKeyBackgroundColor()));
            strLineContent[1] = String.format("#%06X", (0xFFFFFF & DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i).getKeyFrontColor()));
            strLineContent[2] = String.valueOf(DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i).getKeyFrontSize());
            for (int j = 0; j < DataStore.getInstance().getKeyboardConfiguration().getKeyboardLanguages().size(); j++) {
                strLineContent[3 + j] = DataStore.getInstance().getKeyboardConfiguration().getModelKeyboardKeys().get(i).getKeyLanguages().get(j);
            }
            stringCsvFile.add(strLineContent);
        }

        // Add \n
        stringCsvFile.add(new String[]{});
        return stringCsvFile;
    }

    /**
     * Manage the exportation of the FixedKeys paragraph of the CSV file
     * @param stringCsvFile : the ArrayList of the file line by line
     * @return the ArrayList of the file line by line with the new paragraph
     */
    private static ArrayList<String[]> manageExportFixedKeys(ArrayList<String[]> stringCsvFile) {
        stringCsvFile.add(new String[]{KEY_FIXED_KEYS});
        String[] strFixedKeysLineTitle = new String[]{
                KEY_BACKGROUND_COLOR,
                KEY_FRONT_COLOR,
                KEY_FRONT_SIZE,
                KEY_ACTION,
                KEY_ICON,
                "Default"
        };
        stringCsvFile.add(strFixedKeysLineTitle);
        for (int i = 0; i < DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().size(); i++) {
            String[] strLineContent = new String[]{
                    String.format("#%06X", (0xFFFFFF & DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).getKeyBackgroundColor())),
                    String.format("#%06X", (0xFFFFFF & DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).getKeyFrontColor())),
                    String.valueOf(DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).getKeyFrontSize()),
                    String.valueOf(DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).getKeyFrontSize()),
                    String.valueOf(DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).getKeyIcon()),
                    DataStore.getInstance().getKeyboardConfiguration().getModelParameterKeys().get(i).getKeyLanguages().get(0)
            };
            stringCsvFile.add(strLineContent);
        }
        return stringCsvFile;
    }

    /**
     * Manage the generation of the KeyboardConfiguration paragraph of the CSV file
     * @param stringCsvFile : the ArrayList of the file line by line
     */
    private  static void generateCsvFile(ArrayList<String[]> stringCsvFile) {
        try {
            File keyboardConfigFile = new File(FileUtils.getRootFile(), CSV_NAME_FILE);
            keyboardConfigFile.setReadable(true, false);
            keyboardConfigFile.setExecutable(true, false);
            keyboardConfigFile.setWritable(true, false);
            FileWriter writer = new FileWriter(keyboardConfigFile);
            for (int i = 0; i < stringCsvFile.size(); i++) {
                String lineToWrite = "";
                if (stringCsvFile.get(i).length > 0) {
                    for (int j = 0; j < stringCsvFile.get(i).length; j++) {
                        lineToWrite += stringCsvFile.get(i)[j] + ";";
                    }
                } else {
                    lineToWrite = ";";
                }
                lineToWrite += "\r\n";
                writer.append(lineToWrite);
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
