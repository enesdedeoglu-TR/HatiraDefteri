package tr.edu.yildiz.enes.gunluk;

import java.io.Serializable;

public class SelectedLocation implements Serializable {
    private String name;
    private String boylam;
    private String enlem;

    public SelectedLocation(String name, String boylam, String enlem) {
        this.name = name;
        this.boylam = boylam;
        this.enlem = enlem;
    }

    public String getName() {
        return name;
    }

    public String getBoylam() {
        return boylam;
    }

    public String getEnlem() {
        return enlem;
    }
}
