package fr.example;

import java.util.List;

/**
 * Représente une entrée du fichier inversions - sample.json
 * Contient un mot et la liste des sens où il apparaît
 */
public class InversionEntry {
    private String mot;
    private List<String> ref;
    
    public InversionEntry() {
    }
    
    public InversionEntry(String mot, List<String> ref) {
        this.mot = mot;
        this.ref = ref;
    }
    
    public String getMot() {
        return mot;
    }
    
    public void setMot(String mot) {
        this.mot = mot;
    }
    
    public List<String> getRef() {
        return ref;
    }
    
    public void setRef(List<String> ref) {
        this.ref = ref;
    }
    
    @Override
    public String toString() {
        return "InversionEntry{mot='" + mot + "', ref=" + ref + "}";
    }
}
