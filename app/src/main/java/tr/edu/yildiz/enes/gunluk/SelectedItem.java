package tr.edu.yildiz.enes.gunluk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.pdf.PrintedPdfDocument;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectedItem extends AppCompatActivity {

    ImageView iconImageView;
    TextView titleTextView;
    TextView mainTextView;
    TextView locationTextView;
    TextView passwordTextView;
    TextView modeTextView;
    Button updateMemoryButton;
    Button shareButton;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_memory_activity);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#507BF1")));
        getSupportActionBar().setTitle("Anı Görüntüleme");
        defineVariables();
        position = getIntent().getIntExtra("Position", 0);
        Memory selectedMemory = Memory.getMemorys().get(position);
        titleTextView.setText(selectedMemory.getTitle());
        mainTextView.setText(selectedMemory.getMainText());
        locationTextView.setText(selectedMemory.getLocation().getName());
        passwordTextView.setText(selectedMemory.getPassword());
        if(findEmoji().isEmpty()){
            modeTextView.setVisibility(View.INVISIBLE);
        }else{
            modeTextView.setText(findEmoji());
        }


    }

    public void defineVariables(){
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        mainTextView = (TextView) findViewById(R.id.mainTextView);
        modeTextView = (TextView) findViewById(R.id.modeTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        passwordTextView = (TextView) findViewById(R.id.passwordTextView);
        updateMemoryButton = (Button) findViewById(R.id.updateMemoryButton);
        iconImageView = (ImageView) findViewById(R.id.iconImageView);
        shareButton = (Button) findViewById(R.id.shareButton);
    }

    public void updateMemory(View view){
        Intent intent = new Intent(this, UpdateMemory.class);
        intent.putExtra("Position", position);
        startActivity(intent);
        finish();

    }

    public String findEmoji() {
        String regexPattern = "[\uD83C-\uDBFF\uDC00-\uDFFF]+";
        byte[] utf8 = new byte[0];
        String emoji = "";
        try {
            utf8 = mainTextView.getText().toString().getBytes("UTF-8");
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

    public void sendSMS(View view){
        String smsText= "Başlık: "+titleTextView.getText()+"\n"+
                        "Ana Metin: "+mainTextView.getText()+"\n"+
                        "Konum: "+locationTextView.getText()+"\n"+
                        "Mod: "+modeTextView.getText();


        String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); //Need to change the build to API 19

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, smsText);

        if (defaultSmsPackageName != null)//Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
        {
            sendIntent.setPackage(defaultSmsPackageName);
        }
        startActivity(sendIntent);
  }


}