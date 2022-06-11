package tr.edu.yildiz.enes.gunluk;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Memory implements Serializable {
    private String date;
    private String mode;
    //private Location location;
    private SelectedLocation location;
    private String title;
    private String mainText;
    private Uri image;
    private String password;

    private static final long serialVersionUID = 8264888396180477662L;
    private static ArrayList<Memory> memorys;

    public Memory(String date, String mode, SelectedLocation location, String title, String mainText, String password) {
        this.date = date;
        this.mode = mode;
        this.location = location;
        this.title = title;
        this.mainText = mainText;
        this.password = password;
    }

    public Memory(String date, String mode, SelectedLocation location, String title, String mainText, String password, Uri image) {
        this.date = date;
        this.mode = mode;
        this.location = location;
        this.title = title;
        this.mainText = mainText;
        this.image = image;
        this.password = password;
    }


    public static ArrayList<Memory> getMemorys() {
        return memorys;
    }

    public static void setMemorys(ArrayList<Memory> memorys) {
        Memory.memorys = memorys;
    }

    public static void saveArray(ArrayList memorys, Activity ma){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(ma.getFilesDir(), "memorysArray.data")));
            oos.writeObject(memorys);
            oos.flush();
            oos.close();
        }catch(Exception ex){
            Log.v("Serialization Save Error: ", ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static Object loadArray(File file){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.getPath() + File.separator + "memorysArray.data"));
            Object o = ois.readObject();
            return o;
        }catch(Exception ex){
            Log.v("Serialization Read Error: ", ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public SelectedLocation getLocation() {
        return location;
    }

    public void setLocation(SelectedLocation location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
