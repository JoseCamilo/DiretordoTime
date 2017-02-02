package com.jfc.josecamilo.diretordotime.graficos;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jfc.josecamilo.diretordotime.R;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseManager;

import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraficosFragment extends Fragment {

    private TextView listagem;
    private DatabaseManager db;
    private SimpleDateFormat dateFormatter;

    private Uri imageUri;
    private static ImageView imgView;


    public GraficosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graficos, container, false);

        /*dateFormatter = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm", new Locale("pt", "BR"));

        Calendar newCalendar = Calendar.getInstance();


        if (db == null) {
            DatabaseManager.initializeInstance(new DatabaseHelper(getContext()));
            db = DatabaseManager.getInstance();
        }

        listagem = (TextView) view.findViewById(R.id.listagem);
        List<Event> eventos = db.obterEventosAll();
        String resultado = "";
        for (Event e : eventos){
            newCalendar.setTimeInMillis(e.getStart());
            String dataStart = dateFormatter.format(newCalendar.getTime());

            newCalendar.setTimeInMillis(e.getData());
            String dataCalendar = dateFormatter.format(newCalendar.getTime());
            resultado = resultado + "\n" + e.getEventId() + " " + e.getTitle() + " " + dataStart + " " + dataCalendar;

        }
        listagem.setText(resultado);*/

        listagem = (TextView) view.findViewById(R.id.listagem);



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String imgSett = prefs.getString("nome_time_preference", "");
        int corPrim = prefs.getInt("cor_primaria_preferences", 1);

        /*SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cor_primaria_preference","nova cor");*/




        listagem.setText(String.valueOf(corPrim));
        listagem.setTextColor(corPrim);


        imgView = (ImageView) view.findViewById(R.id.imagem);


        Button btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent , RESULT_GALLERY );

                Toast.makeText(getActivity(),String.valueOf(RESULT_GALLERY),Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

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
                        String path = getPathFromURI(imageUri);
                        //Log.i(TAG, "Image Path : " + path);
                        // Set the image in ImageView
                        imgView.setImageURI(imageUri);
                    }
                }
                break;
            default:
                break;
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static void setImage(Uri imageUri){
        imgView.setImageURI(imageUri);
    }
}
