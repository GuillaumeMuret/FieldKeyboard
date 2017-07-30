/*
 * @file FileUtils.java
 * @brief Class use for managing files
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

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.Gson;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.seio.limagrain.fieldkeyboard.model.DataStore;
import com.seio.limagrain.fieldkeyboard.model.KeyboardConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class FileUtils {

    // The different name of the files and directory
    private static final String NAME_DIRECTORY = "CustomKeyboardConfig";
    private static final String JSON_NAME_FILE = "CustomKeyboardConfig.json";

    /**
     * Process called to save the current Keyboard configuration
     * Generate the JSON and CSV file
     */
    public static void saveKeyboardConfiguration() {
        CsvUtils.exportCsvFile();
        Gson gson = new Gson();

        // Java object to JSON, and assign to a String
        String jsonInString = gson.toJson(DataStore.getInstance().getKeyboardConfiguration());

        try {
            File keyboardConfigFile = new File(getRootFile(), JSON_NAME_FILE);
            FileWriter writer = new FileWriter(keyboardConfigFile);

            writer.append(jsonInString);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Process called to reinit the keyboard after an error or when no data found
     */
    private static void reinitKeyboard(){
        Log.d(DataStore.TAG,"reinitKeyboard ERROR");
        DataStore.getInstance().setKeyboardConfiguration(new KeyboardConfiguration());
        saveKeyboardConfiguration();
    }

    /**
     * Manage if keyboard has an error (manage just language)
     * @param keyboardConfiguration : the keyboard configuration
     * @return if the keyboard has an error
     */
    private static boolean isErrorKeyboard(KeyboardConfiguration keyboardConfiguration){
        for(int i = 0; i<keyboardConfiguration.getModelBottomKeys().size(); i++){
            if(keyboardConfiguration.getModelBottomKeys().size()>0) {
                if (keyboardConfiguration.getModelBottomKeys().get(i).getKeyLanguages().size() != keyboardConfiguration.getKeyboardLanguages().size()) {
                    Log.d(DataStore.TAG, "getModelBottomKeys ERROR => " + i);
                    return true;
                }
            }
            if(keyboardConfiguration.getModelTopKeys().size()>0){
                if(keyboardConfiguration.getModelTopKeys().get(i).getKeyLanguages().size()!=keyboardConfiguration.getKeyboardLanguages().size()){
                    Log.d(DataStore.TAG,"getModelTopKeys ERROR => "+i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Manage if the data contains error or not. If there is an error => reinitialization
     * of the keyboard
     * @param keyboardConfiguration : the keyboard configuration
     */
    static void manageDataActualization(KeyboardConfiguration keyboardConfiguration){
        if(    keyboardConfiguration.getKeyboardLanguages()==null
            || keyboardConfiguration.getModelBottomKeys()==null
            || keyboardConfiguration.getModelTopKeys()==null
                ) {
            Log.d(DataStore.TAG,"manageDataActualization ERROR");
            reinitKeyboard();
        }else{
            if(isErrorKeyboard(keyboardConfiguration)) {
                reinitKeyboard();
            }
        }
    }

    /**
     * Process called to overwrite the current keyboard configuration
     * @param context : the context of the app
     * @param uri : the URI where the keyboard configuration file has been picked
     */
    public static void overwriteCurrentKeyboardConfiguration(Context context, Uri uri){
        String path = getPath(context, uri);
        try {
            // Get the file instance
            File keyboardConfigFile = new File(path);
            switch(getFileExtension(path)){
                case "json":
                    manageKeyboardConfigurationFileJSON(keyboardConfigFile);
                    break;

                case "csv":
                case "txt":
                    CsvUtils.manageKeyboardConfigurationFileCSV(keyboardConfigFile);
                    break;
            }
        } catch(Exception e){
            reinitKeyboard();
        }
    }

    /**
     * Manage the json file of the keyboard configuration that the user picked
     * @param keyboardConfigFile : the keyboard config file
     */
    private static void manageKeyboardConfigurationFileJSON(File keyboardConfigFile){
        Gson gson = new Gson();
        try {
            DataStore.getInstance().setKeyboardConfiguration(gson.fromJson(new FileReader(keyboardConfigFile), KeyboardConfiguration.class));
            if(DataStore.getInstance().getKeyboardConfiguration()==null){
                Log.d(DataStore.TAG,"getKeyboardConfiguration()==null shit !");
                reinitKeyboard();
            }else{
                manageDataActualization(DataStore.getInstance().getKeyboardConfiguration());
            }
        } catch (FileNotFoundException e) {
            Log.e(DataStore.TAG,"getKeyboardConfiguration()==null shit !",e);
            reinitKeyboard();
        } catch(Exception e){
            Log.e(DataStore.TAG,"getKeyboardConfiguration()==null shit !",e);
            reinitKeyboard();
        }
    }

    /**
     * Process called to load the current json keyboard configuration file
     */
    public static void loadKeyboardConfiguration(){
        File keyboardConfigFile = new File(getRootFile(), JSON_NAME_FILE);
        manageKeyboardConfigurationFileJSON(keyboardConfigFile);
    }

    /**
     * Process called to get the root file
     * @return : the root file
     */
    static File getRootFile(){
        File root = new File(Environment.getExternalStorageDirectory(), NAME_DIRECTORY);
        if (!root.exists()) {
            root.mkdirs();
        }
        return root;
    }

    /**
     * Process called to get a list word from a file
     * @param filePath : a file path that the user picked to import word
     * @return list of word
     */
    public static ArrayList<String> getListWordFromFile(String filePath){
        ArrayList<String> listWord = new ArrayList<>();
        String fileString = "";
        try {
            switch (getFileExtension(filePath)){
                case "pdf":
                    fileString=readPdfFile(filePath);
                    listWord=extractStringFromFileString(fileString);
                    break;

                case "xlsx":
                case "xls":
                    listWord= readExcelFile(filePath);
                    break;

                case "csv":
                case "txt":
                default:
                    fileString= readTxtFile(filePath);
                    listWord=extractStringFromFileString(fileString);
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listWord;
    }

    /**
     * Process called to get the file extension
     * @param filePath : the file path
     * @return the file extension
     */
    private static String getFileExtension(String filePath){
        String filenameArray[] = filePath.split("\\.");
        return filenameArray.length==0 ? "" : filenameArray[filenameArray.length-1];
    }

    /**
     * Process called to read a pdf file and return a string of the pdf file
     * @param filePath : the pdf file
     * @return a string of the pdf file
     */
    private static String readPdfFile(String filePath){
        String parsedText="";
        try {
            PdfReader reader = new PdfReader(filePath);
            int n = reader.getNumberOfPages();
            for (int i = 0; i <n ; i++) {
                parsedText   = parsedText+ PdfTextExtractor.getTextFromPage(reader, i+1).trim()+"\n"; //Extracting the content from the different pages
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedText;
    }

    /**
     * Process called to read Excel file and return a list of all the word of the file
     * @param filePath : the excel file
     * @return list of all the word of the file
     */
    private static ArrayList<String> readExcelFile(String filePath) {
        ArrayList<String> listWord = new ArrayList<>();

        Workbook workbook = null;
        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setGCDisabled(true);

            workbook = Workbook.getWorkbook(new File(filePath), ws);

            for(int i=0;i<workbook.getNumberOfSheets();i++){
                Sheet sheet = workbook.getSheet(0);
                int rowCount = sheet.getRows();
                for (int j = 0; j < rowCount; j++) {
                    Cell[] row = sheet.getRow(j);
                    for (Cell aRow : row) {
                        listWord.add(aRow.getContents());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }

        return listWord;
    }

    /**
     * Process called to extract word from a string
     * @param fileString : the string to export word
     * @return an array list of all the word of the string in param
     */
    private static ArrayList<String> extractStringFromFileString(String fileString){
        ArrayList<String> listWord = new ArrayList<>();
        fileString = fileString.replaceAll("[!?,.;:}]", "");
        String[] words = fileString.split("\\s+");

        for (String word : words) {
            listWord.add(word);
        }
        return listWord;
    }

    /**
     * Process called to get the path in text
     * @param uri : the path
     * @return the path in text
     */
    public static String getUploadTextFile(Context context,Uri uri){
        // Get the path
        String path = null;
        path = getPath(context, uri);
        // Get the file instance
        File file = new File(path);
        // Initiate the upload
        return file.toString();
    }

    /**
     * Process called to convert stream to string
     * @param is : the current input stream
     * @return the string of the input stream
     * @throws Exception : exception
     */
    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"ISO-8859-1"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    /**
     * Process called to read txt file and convert in string
     * @param filePath : the file that the user picked
     * @return the file in string
     * @throws Exception an exception
     */
    private static String readTxtFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }



    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     */
    private static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
