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
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateMemory extends AppCompatActivity {

    ImageButton iconButtonUpdate;
    EditText titleEditTextUpdate;
    EditText mainTextEditTextUpdate;
    AutoCompleteTextView locationTextUpdate;
    EditText passwordTextUpdate;
    Button saveMemoryButtonUpdate;

    final int RESULT_LOAD_IMG = 100; // istek kodu

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#507BF1")));
        getSupportActionBar().setTitle("Anı Düzenleme");
        defineVariables();

    }

    public void defineVariables(){
        titleEditTextUpdate = (EditText) findViewById(R.id.titleEditTextUpdate);
        mainTextEditTextUpdate = (EditText) findViewById(R.id.mainTextEditTextUpdate);
        locationTextUpdate = (AutoCompleteTextView) findViewById(R.id.locationTextUpdate);
        locationTextUpdate.setAdapter(new PlaceAutoSuggestAdapter(UpdateMemory.this, android.R.layout.simple_list_item_1));
        passwordTextUpdate = (EditText) findViewById(R.id.passwordTextUpdate);
        saveMemoryButtonUpdate = (Button) findViewById(R.id.saveMemoryButtonUpdate);
        iconButtonUpdate = (ImageButton) findViewById(R.id.iconButtonUpdate);

        position = getIntent().getIntExtra("Position", 0);
        Memory selectedMemory = Memory.getMemorys().get(position);
        titleEditTextUpdate.setText(selectedMemory.getTitle());
        mainTextEditTextUpdate.setText(selectedMemory.getMainText());
        locationTextUpdate.setText(selectedMemory.getLocation().getName()+" | "+selectedMemory.getLocation().getBoylam()+" | "+selectedMemory.getLocation().getEnlem());
        passwordTextUpdate.setText(selectedMemory.getPassword());
    }

    public boolean checkNullText(){
        if (titleEditTextUpdate.getText().toString().isEmpty() ||
                mainTextEditTextUpdate.getText().toString().isEmpty() ||
                locationTextUpdate.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }

    public void saveUpdatedMemory(View view){
        if(checkNullText()){
            Toast.makeText(getApplicationContext(), "Lütfen ilgili yerleri giriniz!", Toast.LENGTH_SHORT).show();
        }
        else{
            updateMemory();
        }
    }

    public void updateMemory(){
        SelectedLocation selectedLocation = new SelectedLocation(locationTextUpdate.getText().toString().substring(0, locationTextUpdate.getText().toString().indexOf(" |")),
                locationTextUpdate.getText().toString().substring(locationTextUpdate.getText().toString().indexOf(" |")+3, locationTextUpdate.getText().toString().lastIndexOf(" |")),
                locationTextUpdate.getText().toString().substring(locationTextUpdate.getText().toString().lastIndexOf("| ")+2 ));
        //Memory memory = Memory.getMemorys().get(position);
        ArrayList<Memory> s = Memory.getMemorys();
        s.get(position).setDate(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        s.get(position).setMode(findEmoji());
        s.get(position).setLocation(selectedLocation);
        s.get(position).setTitle(titleEditTextUpdate.getText().toString());
        s.get(position).setMainText(mainTextEditTextUpdate.getText().toString());
        s.get(position).setPassword(passwordTextUpdate.getText().toString());
        Memory.setMemorys(s);
        Memory.saveArray(Memory.getMemorys(), this);
        AlertDialog alertDialog = new AlertDialog.Builder(UpdateMemory.this).create();
        alertDialog.setTitle("Bilgi");
        alertDialog.setMessage("Anı başarıyla güncellendi");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void uploadUpdatedImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    public String findEmoji() {
        String regexPattern = "[\uD83C-\uDBFF\uDC00-\uDFFF]+";
        byte[] utf8 = new byte[0];
        String emoji = "";
        try {
            utf8 = mainTextEditTextUpdate.getText().toString().getBytes("UTF-8");
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