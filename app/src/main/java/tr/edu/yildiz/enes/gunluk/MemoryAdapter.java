package tr.edu.yildiz.enes.gunluk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.MyViewHolder>{

    private static ArrayList<Memory> mDataset;
    private Context c;
    private Activity a;

    EditText input;
    Button onay;

    public MemoryAdapter(Context c, ArrayList<Memory> mDataset, Activity a) {
        this.c =c;
        this.mDataset = mDataset;
        this.a = a;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText;
        public TextView dateText;
        public TextView mainText;
        public ImageButton deleteButton;

        public MyViewHolder(View v) {
            super(v);
            titleText = (TextView) v.findViewById(R.id.titleText);
            dateText = (TextView) v.findViewById(R.id.dateText);
            mainText = (TextView) v.findViewById(R.id.mainText);
            deleteButton = (ImageButton) v.findViewById(R.id.deleteButton);
        }
    }

    // adaptör oluşturulduğunda viewholder'ın başlatmasını sağlar
    @Override
    public MemoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.ani_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // onCreateViewHolder'dan dönen verileri bağlama işlemi
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Memory selectedItem = mDataset.get(position);
        holder.titleText.setText(selectedItem.getTitle());
        holder.mainText.setText(selectedItem.getMainText());
        holder.dateText.setText(selectedItem.getDate());


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(a);
                builder.setTitle("Onayla");
                builder.setMessage("Seçilen anı silinecek, silmek istediğinize emin misiniz?");

                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mDataset.remove(holder.getAdapterPosition());
                        Memory.setMemorys(mDataset);
                        Memory.saveArray(Memory.getMemorys(), a);
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(),mDataset.size());
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
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Memory.getMemorys().get(position).getPassword().isEmpty()){
                     Intent intent = new Intent(c, SelectedItem.class);
                     intent.putExtra("Position", holder.getPosition());
                     intent.putExtra("Size", getItemCount());
                     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     c.startActivity(intent);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(a);
                    builder.setTitle("Onay");
                    builder.setMessage("Lütfen parolayı giriniz");
                    input = new EditText(a);
                    builder.setView(input);
                    builder.setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String txt = input.getText().toString();
                            if(txt.equals(Memory.getMemorys().get(position).getPassword())){
                                Intent intent = new Intent(c, SelectedItem.class);
                                intent.putExtra("Position", holder.getPosition());
                                intent.putExtra("Size", getItemCount());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                c.startActivity(intent);
                            }else{
                                Toast.makeText(a, "Parola yanlış!", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog ad = builder.create();
                    ad.show();
                }






            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static Memory getItem(int position) {
        return mDataset.get(position);
    }
}
