package tr.edu.yildiz.enes.gunluk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddMemory extends AppCompatActivity {

    ImageButton iconButton;
    EditText titleEditText;
    EditText mainTextEditText;
    AutoCompleteTextView locationText;
    EditText passwordText;
    Button saveMemoryButton;

    final int RESULT_LOAD_IMG = 100; // istek kodu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_memory_activity);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#507BF1")));
        getSupportActionBar().setTitle("Anı Ekleme");
        defineVariables();

    }

    public void defineVariables(){
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        mainTextEditText = (EditText) findViewById(R.id.mainTextEditText);
        locationText = (AutoCompleteTextView) findViewById(R.id.locationText);
        locationText.setAdapter(new PlaceAutoSuggestAdapter(AddMemory.this, android.R.layout.simple_list_item_1));
        passwordText = (EditText) findViewById(R.id.passwordText);
        saveMemoryButton = (Button) findViewById(R.id.saveMemoryButton);
        iconButton = (ImageButton) findViewById(R.id.iconButton);
    }

    public boolean checkNullText(){
        if (titleEditText.getText().toString().isEmpty() ||
                mainTextEditText.getText().toString().isEmpty() ||
                locationText.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }

    public void saveMemory(View view){
        if(checkNullText()){
            Toast.makeText(getApplicationContext(), "Lütfen ilgili yerleri giriniz!", Toast.LENGTH_SHORT).show();
        }else if (iconButton.getBackground() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Onayla");
            builder.setMessage("Bir profil fotoğrafı belirlemediniz, bu şekilde devam etmek istediğinize emin misiniz?");

            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    createMemory();
                }
            });

            builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
        else{
            createMemory();
        }
    }

    public void createMemory(){
        SelectedLocation selectedLocation = new SelectedLocation(locationText.getText().toString().substring(0, locationText.getText().toString().indexOf(" |")),
                locationText.getText().toString().substring(locationText.getText().toString().indexOf(" |")+3, locationText.getText().toString().lastIndexOf(" |")),
                locationText.getText().toString().substring(locationText.getText().toString().lastIndexOf("| ")+2 ));
        Memory memory = new Memory(new SimpleDateFormat("yyyy/MM/dd").format(new Date()),
                    findEmoji(),
                    selectedLocation,
                    titleEditText.getText().toString(),
                    mainTextEditText.getText().toString(),
                    passwordText.getText().toString());
        if ((ArrayList<Memory>) Memory.loadArray(new File(String.valueOf(this.getFilesDir()))) == null){
            ArrayList<Memory> first = new ArrayList<Memory>();
            first.add(memory);
            Memory.setMemorys(first);
            Memory.saveArray(Memory.getMemorys(), this);
        }else {
            Memory.getMemorys().add(memory);
            Memory.setMemorys(Memory.getMemorys());
            Memory.saveArray(Memory.getMemorys(), this);
        }
        AlertDialog alertDialog = new AlertDialog.Builder(AddMemory.this).create();
        alertDialog.setTitle("Bilgi");
        alertDialog.setMessage("Anı başarıyla eklendi");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();


    }

    public void uploadImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                iconButton.setBackground(null);
                iconButton.setImageURI(data.getData());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Bir şeyler yanlış gitti", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "Resim seçmediniz",Toast.LENGTH_LONG).show();
        }
    }

    public String findEmoji() {
        String regexPattern = "[\uD83C-\uDBFF\uDC00-\uDFFF]+";
        byte[] utf8 = new byte[0];
        String emoji = "";
        try {
            utf8 = mainTextEditText.getText().toString().getBytes("UTF-8");
            String string1 = new String(utf8, "UTF-8");
            Pattern pattern = Pattern.compile(regexPattern);
            Matcher matcher = pattern.matcher(string1);
            if (matcher.find()) {
                emoji=  matcher.group();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return emoji;
    }

}