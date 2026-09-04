package fr.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe pour traiter les données du fichier inversions - sample.json
 * et générer les prompts pour Mistral
 */
public class InversionDataProcessor {
    
    private final MistralClient mistralClient;
    private final Gson gson;
    
    public InversionDataProcessor(MistralClient mistralClient) {
        this.mistralClient = mistralClient;
        this.gson = new Gson();
    }
    
    /**
     * Charge le fichier inversions - sample.json
     * @param filePath Chemin vers le fichier JSON
     * @return Liste des entrées du fichier
     * @throws IOException En cas d'erreur de lecture
     */
    public List<InversionEntry> loadInversionFile(String filePath) throws IOException {
        try (Reader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<List<InversionEntry>>() {}.getType();
            return gson.fromJson(reader, listType);
        }
    }
    
    /**
     * Fonction mockup qui retourne la définition d'un sens
     * Chaque sens a exactement une définition
     * @param sens Le sens (ex: "culpabilité#0")
     * @return La définition pour ce sens
     */
    public String getDefinitionForSens(String sens) {
        // Mockup: retourne une définition basée sur le sens
        // Extraire le mot principal du sens (avant le #)
        String[] parts = sens.split("#");
        String baseWord = parts[0];
        int sensNumber = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        
        // Map de définitions par mot et numéro de sens
        Map<String, Map<Integer, String>> definitions = new HashMap<>();
        
        // Définitions pour "culpabilité"
        Map<Integer, String> culpabiliteDefs = new HashMap<>();
        culpabiliteDefs.put(0, "État de celui qui est coupable ou qui se sent coupable.");
        culpabiliteDefs.put(1, "Responsabilité morale ou légale d'une faute.");
        definitions.put("culpabilité", culpabiliteDefs);
        
        // Définitions pour "irresponsabilité"
        Map<Integer, String> irresponsabiliteDefs = new HashMap<>();
        irresponsabiliteDefs.put(0, "Absence de responsabilité ou de sérieux.");
        definitions.put("irresponsabilité", irresponsabiliteDefs);
        
        // Définitions pour "simoniaque"
        Map<Integer, String> simoniaqueDefs = new HashMap<>();
        simoniaqueDefs.put(0, "Qui concerne la simonie, c'est-à-dire la vente ou l'achat de choses spirituelles.");
        definitions.put("simoniaque", simoniaqueDefs);
        
        // Définitions pour "condamner"
        Map<Integer, String> condamnerDefs = new HashMap<>();
        condamnerDefs.put(0, "Déclarer quelqu'un coupable et lui infliger une peine.");
        condamnerDefs.put(1, "Désapprouver fermement.");
        definitions.put("condamner", condamnerDefs);
        
        // Définitions pour "excuser"
        Map<Integer, String> excuserDefs = new HashMap<>();
        excuserDefs.put(0, "Déclarer quelqu'un non coupable ou innocent.");
        excuserDefs.put(1, "Trouver des excuses pour justifier une action.");
        definitions.put("excuser", excuserDefs);
        
        // Définitions pour "venger"
        Map<Integer, String> vengerDefs = new HashMap<>();
        vengerDefs.put(0, "Faire subir à quelqu'un le mal qu'il a fait.");
        definitions.put("venger", vengerDefs);
        
        // Définitions pour "péculateur"
        Map<Integer, String> peculateurDefs = new HashMap<>();
        peculateurDefs.put(0, "Personne qui détourne des fonds publics.");
        definitions.put("péculateur", peculateurDefs);
        
        // Définitions pour "coupablement"
        Map<Integer, String> coupablementDefs = new HashMap<>();
        coupablementDefs.put(0, "De manière coupable, avec faute.");
        definitions.put("coupablement", coupablementDefs);
        
        // Définitions pour "convaincu"
        Map<Integer, String> convaincuDefs = new HashMap<>();
        convaincuDefs.put(0, "Qui est convaincu, persuadé.");
        convaincuDefs.put(4, "Qui a été reconnu coupable par un tribunal.");
        definitions.put("convaincu", convaincuDefs);
        
        // Si le mot a des définitions spécifiques
        if (definitions.containsKey(baseWord) && definitions.get(baseWord).containsKey(sensNumber)) {
            return definitions.get(baseWord).get(sensNumber);
        }
        
        // Définition générique pour les autres cas
        return "Définition de " + baseWord + " (sens #" + sensNumber + ").";
    }
    
    /**
     * Fonction mockup qui retourne les définitions possibles d'un mot
     * Un mot peut avoir plusieurs sens, chaque sens a une définition
     * Cette méthode liste tous les sens du mot et appelle getDefinitionForSens pour chacun
     * @param mot Le mot à définir
     * @return Liste des définitions (une par sens) pour ce mot
     */
    public List<String> getDefinitionsForMot(String mot) {
        // Liste des définitions pour ce mot
        List<String> definitions = new ArrayList<>();
        
        // Map des sens connus pour chaque mot
        Map<String, List<String>> motSens = new HashMap<>();
        
        // Sens pour "coupable"
        List<String> coupableSens = new ArrayList<>();
        coupableSens.add("coupable#0");
        coupableSens.add("coupable#1");
        coupableSens.add("coupable#2");
        motSens.put("coupable", coupableSens);
        
        // Sens pour "abrupt"
        List<String> abruptSens = new ArrayList<>();
        abruptSens.add("abrupt#0");
        abruptSens.add("abrupt#1");
        motSens.put("abrupt", abruptSens);
        
        // Sens pour "prévenu"
        List<String> prevenuSens = new ArrayList<>();
        prevenuSens.add("prévenu#0");
        prevenuSens.add("prévenu#1");
        motSens.put("prévenu", prevenuSens);
        
        // Sens pour "conformer"
        List<String> conformerSens = new ArrayList<>();
        conformerSens.add("conformer#0");
        conformerSens.add("conformer#1");
        conformerSens.add("conformer#2");
        motSens.put("conformer", conformerSens);
        
        // Sens pour "ignicole"
        List<String> ignicoleSens = new ArrayList<>();
        ignicoleSens.add("ignicole#0");
        ignicoleSens.add("ignicole#1");
        motSens.put("ignicole", ignicoleSens);
        
        // Sens pour "foulage"
        List<String> foulageSens = new ArrayList<>();
        foulageSens.add("foulage#0");
        foulageSens.add("foulage#1");
        motSens.put("foulage", foulageSens);
        
        // Sens pour "consomption"
        List<String> consomptionSens = new ArrayList<>();
        consomptionSens.add("consomption#0");
        consomptionSens.add("consomption#1");
        consomptionSens.add("consomption#2");
        motSens.put("consomption", consomptionSens);
        
        // Sens pour "avant"
        List<String> avantSens = new ArrayList<>();
        avantSens.add("avant#0");
        avantSens.add("avant#1");
        avantSens.add("avant#2");
        motSens.put("avant", avantSens);
        
        // Sens pour "wingsuit"
        List<String> wingsuitSens = new ArrayList<>();
        wingsuitSens.add("wingsuit#0");
        wingsuitSens.add("wingsuit#1");
        motSens.put("wingsuit", wingsuitSens);
        
        // Sens pour "chamarré"
        List<String> chamarreSens = new ArrayList<>();
        chamarreSens.add("chamarré#0");
        motSens.put("chamarré", chamarreSens);
        
        // Sens pour "débuter"
        List<String> debuterSens = new ArrayList<>();
        debuterSens.add("débuter#0");
        debuterSens.add("débuter#1");
        debuterSens.add("débuter#2");
        motSens.put("débuter", debuterSens);
        
        // Sens pour "liseron"
        List<String> liseronSens = new ArrayList<>();
        liseronSens.add("liseron#0");
        liseronSens.add("liseron#1");
        motSens.put("liseron", liseronSens);
        
        // Sens pour "quatrième"
        List<String> quatriemeSens = new ArrayList<>();
        quatriemeSens.add("quatrième#0");
        quatriemeSens.add("quatrième#1");
        motSens.put("quatrième", quatriemeSens);
        
        // Sens pour "léniniste"
        List<String> leninisteSens = new ArrayList<>();
        leninisteSens.add("léniniste#0");
        motSens.put("léniniste", leninisteSens);
        
        // Sens pour "grandiloquent"
        List<String> grandiloquentSens = new ArrayList<>();
        grandiloquentSens.add("grandiloquent#0");
        grandiloquentSens.add("grandiloquent#1");
        motSens.put("grandiloquent", grandiloquentSens);
        
        // Sens pour "attablé"
        List<String> attableSens = new ArrayList<>();
        attableSens.add("attablé#0");
        motSens.put("attablé", attableSens);
        
        // Sens pour "avalé"
        List<String> avaleSens = new ArrayList<>();
        avaleSens.add("avalé#0");
        avaleSens.add("avalé#1");
        motSens.put("avalé", avaleSens);
        
        // Sens pour "gourrer"
        List<String> gourrerSens = new ArrayList<>();
        gourrerSens.add("gourrer#0");
        motSens.put("gourrer", gourrerSens);
        
        // Sens pour "éclusier"
        List<String> eclusierSens = new ArrayList<>();
        eclusierSens.add("éclusier#0");
        motSens.put("éclusier", eclusierSens);
        
        // Si le mot a des sens définis
        if (motSens.containsKey(mot)) {
            // Pour chaque sens du mot, appeler getDefinitionForSens
            for (String sens : motSens.get(mot)) {
                definitions.add(getDefinitionForSens(sens));
            }
            return definitions;
        }
        
        // Si le mot n'a pas de sens définis, retourner une définition générique
        definitions.add("Définition de " + mot + ".");
        return definitions;
    }
    
    /**
     * Génère un prompt pour Mistral pour un mot donné
     * @param inversionEntry L'entrée du fichier d'inversions pour ce mot
     * @return Le prompt à soumettre à Mistral
     */
    public String generatePromptForMot(InversionEntry inversionEntry) {
        StringBuilder promptBuilder = new StringBuilder();
        String mot = inversionEntry.getMot();
        List<String> sensList = inversionEntry.getRef();
        
        promptBuilder.append("Analyse du mot: **").append(mot).append("**\n\n");
        
        // Pour chaque sens où le mot apparaît
        for (String sens : sensList) {
            // Charger la définition du sens (5) - chaque sens a une seule définition
            String definitionSens = getDefinitionForSens(sens);
            
            promptBuilder.append("Dans le contexte suivant:\n");
            promptBuilder.append("- Sens: ").append(sens).append("\n");
            promptBuilder.append("- Définition du sens: ").append(definitionSens).append("\n\n");
            
            // Charger les définitions possibles du mot (3 et 4) - le mot peut avoir plusieurs définitions
            List<String> definitionsMot = getDefinitionsForMot(mot);
            
            promptBuilder.append("Les définitions possibles du mot **").append(mot).append("** sont:\n");
            for (int i = 0; i < definitionsMot.size(); i++) {
                promptBuilder.append("  ").append(i + 1).append(". ").append(definitionsMot.get(i)).append("\n");
            }
            promptBuilder.append("\n");
            
            // Question pour Mistral
            promptBuilder.append("Question: Dans ce contexte, quel est le sens de **").append(mot).append("** qu'il faut comprendre?");
            promptBuilder.append(" Réponds avec le numéro de la définition la plus appropriée.\n\n");
            promptBuilder.append("---\n\n");
        }
        
        return promptBuilder.toString();
    }
    
    /**
     * Traite tous les mots du fichier et génère les prompts
     * @param filePath Chemin vers le fichier inversions - sample.json
     * @throws IOException En cas d'erreur
     */
    public void processAllMots(String filePath) throws IOException {
        List<InversionEntry> entries = loadInversionFile(filePath);
        
        System.out.println("=== Début du traitement des mots ===\n");
        
        for (InversionEntry entry : entries) {
            String mot = entry.getMot();
            System.out.println("Traitement du mot: " + mot);
            System.out.println("Nombre de sens: " + entry.getRef().size());
            
            String prompt = generatePromptForMot(entry);
            System.out.println("\n--- Prompt généré pour Mistral ---");
            System.out.println(prompt);
            
            // Optionnel: envoyer le prompt à Mistral
            // String response = mistralClient.sendMessage(prompt);
            // System.out.println("Réponse de Mistral: " + response);
            
            System.out.println("\n" + "=".repeat(50) + "\n");
        }
        
        System.out.println("=== Fin du traitement ===");
    }
    
    /**
     * Traite un mot spécifique et retourne le prompt
     * @param filePath Chemin vers le fichier
     * @param mot Le mot à traiter
     * @return Le prompt généré
     * @throws IOException En cas d'erreur
     */
    public String processSingleMot(String filePath, String mot) throws IOException {
        List<InversionEntry> entries = loadInversionFile(filePath);
        
        for (InversionEntry entry : entries) {
            if (entry.getMot().equals(mot)) {
                return generatePromptForMot(entry);
            }
        }
        
        throw new IOException("Mot non trouvé dans le fichier: " + mot);
    }
}
