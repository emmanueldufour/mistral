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
     * Fonction mockup qui retourne les définitions d'un sens
     * Chaque sens peut avoir plusieurs définitions
     * @param sens Le sens (ex: "culpabilité#0")
     * @return Liste des définitions pour ce sens
     */
    public List<String> getDefinitionsForSens(String sens) {
        // Mockup: retourne des définitions basées sur le sens
        List<String> definitions = new ArrayList<>();
        
        // Extraire le mot principal du sens (avant le #)
        String[] parts = sens.split("#");
        String baseWord = parts[0];
        
        // Générer des définitions mockup
        if (baseWord.equals("culpabilité")) {
            definitions.add("État de celui qui est coupable ou qui se sent coupable.");
            definitions.add("Responsabilité morale ou légale d'une faute.");
        } else if (baseWord.equals("irresponsabilité")) {
            definitions.add("Absence de responsabilité ou de sérieux.");
        } else if (baseWord.equals("simoniaque")) {
            definitions.add("Qui concerne la simonie, c'est-à-dire la vente ou l'achat de choses spirituelles.");
        } else if (baseWord.equals("condamner")) {
            definitions.add("Déclarer quelqu'un coupable et lui infliger une peine.");
            definitions.add("Désapprouver fermement.");
        } else {
            // Définition générique pour les autres cas
            definitions.add("Définition principale de " + baseWord + " dans ce contexte.");
            if (parts.length > 1 && Integer.parseInt(parts[1]) > 0) {
                definitions.add("Définition secondaire ou sens figuré de " + baseWord + ".");
            }
        }
        
        return definitions;
    }
    
    /**
     * Fonction mockup qui retourne la définition d'un mot (pour le sens où il apparaît)
     * @param mot Le mot à définir
     * @return La définition du mot
     */
    public String getDefinitionForMot(String mot) {
        // Mockup: retourne une définition basée sur le mot
        Map<String, String> definitions = new HashMap<>();
        definitions.put("coupable", "Qui a commis une faute ou un délit et mérite une punition.");
        definitions.put("abrupt", "Qui est escarpé, très pentu. Qui est soudain, brusque.");
        definitions.put("prévenu", "Qui est averti ou informé à l'avance. Personne accusée dans une procédure pénale.");
        definitions.put("conformer", "Rendre conforme, adapter à un modèle ou à une règle.");
        definitions.put("ignicole", "Relatif au feu ou à la combustion.");
        definitions.put("foulage", "Action de fouler, de presser avec les pieds.");
        definitions.put("consomption", "Action de consommer, de dépenser.");
        definitions.put("avant", "Qui précède dans le temps ou dans l'espace. Devant.");
        definitions.put("wingsuit", "Combinaison ailée utilisée pour le vol en chute libre.");
        definitions.put("chamarré", "Qui est orné de couleurs vives et contrastées.");
        definitions.put("débuter", "Commencer quelque chose. Faire ses premiers pas dans un domaine.");
        definitions.put("liseron", "Plante grimpante de la famille des convolvulacées.");
        definitions.put("quatrième", "Qui occupe le rang numéro quatre dans une série.");
        definitions.put("léniniste", "Partisan des théories de Lénine.");
        definitions.put("grandiloquent", "Qui s'exprime avec emphase, de manière pompeuse.");
        definitions.put("attablé", "Qui est assis à table pour manger.");
        definitions.put("avalé", "Qui a été avalé, ingéré. Qui a été accepté sans discussion.");
        definitions.put("gourrer", "Faire une erreur, se tromper.");
        definitions.put("éclusier", "Personne chargée de la manœuvre des écluses.");
        
        return definitions.getOrDefault(mot, "Définition de " + mot + " non disponible.");
    }
    
    /**
     * Génère un prompt pour Mistral pour un mot donné
     * @param mot Le mot à analyser
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
            // Charger la définition du sens (5)
            String definitionSens = getDefinitionForMot(sens.split("#")[0]);
            
            promptBuilder.append("Dans le contexte suivant:\n");
            promptBuilder.append("- Sens: ").append(sens).append("\n");
            promptBuilder.append("- Définition du sens: ").append(definitionSens).append("\n\n");
            
            // Charger les définitions du mot (3 et 4)
            List<String> definitionsMot = getDefinitionsForSens(sens);
            
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
