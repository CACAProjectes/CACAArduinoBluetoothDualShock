package es.xuan.cacabtconn.files;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FilesDao implements Serializable {

    private static final String CTE_CANVI_LINEA_DESCRIP = "\n";

    public static void guardarRegistresExcel(String pStrPath, ArrayList<String> pArrContenido) {
        // "/apps/competicio/estadístiques_2019-09-20_12-24.csv"
        BufferedWriter writer = null;
        try {
            //create a temporary file
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), pStrPath);
            //writer = new BufferedWriter(new FileWriter(file));
            writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file, true), StandardCharsets.UTF_8));

            for (String strLinia : pArrContenido) {
                writer.write(strLinia + CTE_CANVI_LINEA_DESCRIP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    public static ArrayList<File> filesDirectori(String pPatch) {
        String path = Environment.getExternalStorageDirectory().toString() + pPatch;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        ArrayList<File> arrFiles = new ArrayList<>();
        for (File file : files) {
            arrFiles.add(file);
        }
        return arrFiles;
    }

    public static ArrayList<String> llegirFitxerLinies(String pPatch) {
        // "/apps/competicio/estadístiques_2019-09-20_12-24.csv"
        Path pathFitxer = Paths.get(Environment.getExternalStorageDirectory().getAbsolutePath(), pPatch);
        ArrayList<String> linesStr = new ArrayList<>();
        Charset charset = Charset.forName("UTF-8");
        try {
            List lines = Files.readAllLines(pathFitxer, charset);
            for (Object line : lines) {
                linesStr.add(line.toString());
            }
        } catch (IOException e) {
            Log.e("","Error: " + e);
        }
        return linesStr;
    }

    @TargetApi(23)
    public static void sollicitarPermissos(Activity pActivity) {
        if (ContextCompat.checkSelfPermission(pActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (pActivity.shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
                Log.v("","Permission READ is granted0");
            }
            pActivity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique
            Log.v("","Permission READ is granted1");
        }
        if (ContextCompat.checkSelfPermission(pActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (pActivity.shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
                Log.v("","Permission WRITE is granted2");
            }
            pActivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique
            Log.v("","Permission WRITE is granted3");
        }
    }

    public static ArrayList<String> convertFiles2NamesString(ArrayList<File> pListaFiles) {
        ArrayList<String> arrNames = new ArrayList<String>();
        for (File file : pListaFiles) {
            arrNames.add(file.getName());
        }
        return arrNames;
    }
}
