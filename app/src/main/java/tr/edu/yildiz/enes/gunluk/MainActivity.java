package tr.edu.yildiz.enes.gunluk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ImageButton addMemory;
    ImageButton locationMemory;
    private RecyclerView recyclerView;
    private TextView notFound_Text;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#507BF1")));
        getSupportActionBar().setTitle("Anı Defterim");
        defineVariables();

    }

    public void defineVariables(){
        addMemory = (ImageButton) findViewById(R.id.addMemory);
        locationMemory = (ImageButton) findViewById(R.id.locationMemory);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    public void addMemory(View view){
        Intent intent = new Intent(this, AddMemory.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("GİRDİ");
        if ((ArrayList<Memory>) Memory.loadArray(new File(String.valueOf(this.getFilesDir()))) != null) {
            Memory.setMemorys((ArrayList<Memory>) Memory.loadArray(new File(String.valueOf(this.getFilesDir()))));

            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            mAdapter = new MemoryAdapter(getApplicationContext(), Memory.getMemorys(), MainActivity.this);
            recyclerView.setAdapter(mAdapter);
            locationMemory.setVisibility(View.VISIBLE);

        } else {
            locationMemory.setVisibility(View.INVISIBLE);
        }
    }

    public void locationMemory(View view){
        Intent intent = new Intent(this, LocationView.class);
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> boylam = new ArrayList<>();
        ArrayList<String> enlem = new ArrayList<>();
        for(Memory m: Memory.getMemorys()){
            title.add(m.getTitle());
            boylam.add(m.getLocation().getBoylam());
            enlem.add(m.getLocation().getEnlem());
        }
        intent.putExtra("title", title);
        intent.putExtra("boylam", boylam);
        intent.putExtra("enlem", enlem);
        startActivity(intent);

    }

}