package com.jfc.josecamilo.diretordotime.settings;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.jfc.josecamilo.diretordotime.MainActivity;
import com.jfc.josecamilo.diretordotime.R;
import com.jfc.josecamilo.diretordotime.graficos.GraficosFragment;
import com.github.machinarius.preferencefragment.PreferenceFragment;

import yuku.ambilwarna.widget.AmbilWarnaPreference;


public class SettingsFragment extends PreferenceFragment {

    private int mListPreference;
    private boolean permissaoCedida = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Indicate here the XML resource you created above that holds the preferences
        addPreferencesFromResource(R.xml.settings);

        EditTextPreference pref = (EditTextPreference)findPreference("nome_time_preference");
        pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                MainActivity.setNomeTime(newValue.toString());
                return true;
            }
        });

        EditTextPreference pref2 = (EditTextPreference)findPreference("desc_time_preference");
        pref2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                MainActivity.setDescTime(newValue.toString());
                return true;
            }
        });

        Preference btnFoto = (Preference) findPreference("foto_time_preference");
        btnFoto.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                int MyVersion = Build.VERSION.SDK_INT;
                if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (!checkIfAlreadyhavePermission()) {
                        requestForSpecificPermission();
                    }else{
                        permissaoCedida = true;
                    }
                }else{
                    permissaoCedida = true;
                }

                if (permissaoCedida){
                    Intent galleryIntent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent , RESULT_GALLERY );
                }

                return true;
            }
        });

        AmbilWarnaPreference pref3 = (AmbilWarnaPreference) findPreference("cor_primaria_preferences");
        pref3.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                MainActivity.setFundoMenu((int) newValue,0);
                return true;
            }
        });

        AmbilWarnaPreference pref5 = (AmbilWarnaPreference) findPreference("cor_acentuada_preferences");
        pref5.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                MainActivity.setFundoMenu(0,(int) newValue);
                return true;
            }
        });
    }




    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    permissaoCedida = true;
                } else {
                    //not granted
                    permissaoCedida = false;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /*TRATAMENTO PARA A ESCOLHA DA FOTO DO TIME*/
    private Uri imageUri;
    public static final int RESULT_GALLERY = 0;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GraficosFragment.RESULT_GALLERY :
                if (null != data) {
                    imageUri = data.getData();
                    //Do whatever that you desire here. or leave this blank
                    if (null != imageUri) {
                        // Get the path from the Uri
                        /*String path = getPathFromURI(imageUri);
                        Log.i(TAG, "Image Path : " + path);*/
                        // Set the image in ImageView
                        //imgView.setImageURI(imageUri);

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor myPrefsEdit = prefs.edit();
                        myPrefsEdit.putString("foto_time_preference", imageUri.toString());
                        myPrefsEdit.commit();

                        MainActivity.setImagemTime(imageUri);

                    }
                }
                break;
            default:
                break;
        }
    }

    /* Get the real path from the URI */
    /*    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }*/


}