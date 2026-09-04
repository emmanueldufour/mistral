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
     * Un mot peut avoir plusieurs définitions (pour ses différents sens)
     * @param mot Le mot à définir
     * @return Liste des définitions possibles pour ce mot
     */
    public List<String> getDefinitionsForMot(String mot) {
        // Mockup: retourne une liste de définitions possibles pour le mot
        List<String> definitions = new ArrayList<>();
        
        Map<String, List<String>> motDefinitions = new HashMap<>();
        
        // Définitions pour "coupable"
        List<String> coupableDefs = new ArrayList<>();
        coupableDefs.add("Qui a commis une faute ou un délit et mérite une punition.");
        coupableDefs.add("Qui est responsable d'une action répréhensible.");
        coupableDefs.add("Qui peut être blâmé pour quelque chose.");
        motDefinitions.put("coupable", coupableDefs);
        
        // Définitions pour "abrupt"
        List<String> abruptDefs = new ArrayList<>();
        abruptDefs.add("Qui est escarpé, très pentu.");
        abruptDefs.add("Qui est soudain, brusque.");
        abruptDefs.add("Qui manque de douceur ou de progressivité.");
        motDefinitions.put("abrupt", abruptDefs);
        
        // Définitions pour "prévenu"
        List<String> prevenuDefs = new ArrayList<>();
        prevenuDefs.add("Qui est averti ou informé à l'avance.");
        prevenuDefs.add("Personne accusée dans une procédure pénale.");
        prevenuDefs.add("Qui a reçu un avis ou une notification.");
        motDefinitions.put("prévenu", prevenuDefs);
        
        // Définitions pour "conformer"
        List<String> conformerDefs = new ArrayList<>();
        conformerDefs.add("Rendre conforme, adapter à un modèle ou à une règle.");
        conformerDefs.add("Se soumettre à une norme ou à une autorité.");
        conformerDefs.add("Donner une forme particulière à quelque chose.");
        motDefinitions.put("conformer", conformerDefs);
        
        // Définitions pour "ignicole"
        List<String> ignicoleDefs = new ArrayList<>();
        ignicoleDefs.add("Relatif au feu ou à la combustion.");
        ignicoleDefs.add("Qui peut s'enflammer facilement.");
        motDefinitions.put("ignicole", ignicoleDefs);
        
        // Définitions pour "foulage"
        List<String> foulageDefs = new ArrayList<>();
        foulageDefs.add("Action de fouler, de presser avec les pieds.");
        foulageDefs.add("Opération de traitement des tissus par foulage.");
        motDefinitions.put("foulage", foulageDefs);
        
        // Définitions pour "consomption"
        List<String> consomptionDefs = new ArrayList<>();
        consomptionDefs.add("Action de consommer, de dépenser.");
        consomptionDefs.add("Quantité consommée.");
        consomptionDefs.add("Usure progressive par l'usage.");
        motDefinitions.put("consomption", consomptionDefs);
        
        // Définitions pour "avant"
        List<String> avantDefs = new ArrayList<>();
        avantDefs.add("Qui précède dans le temps ou dans l'espace.");
        avantDefs.add("Devant, en première position.");
        avantDefs.add("Auparavant, dans le passé.");
        motDefinitions.put("avant", avantDefs);
        
        // Définitions pour "wingsuit"
        List<String> wingsuitDefs = new ArrayList<>();
        wingsuitDefs.add("Combinaison ailée utilisée pour le vol en chute libre.");
        wingsuitDefs.add("Équipement permettant de planer lors d'un saut.");
        motDefinitions.put("wingsuit", wingsuitDefs);
        
        // Définitions pour "chamarré"
        List<String> chamarreDefs = new ArrayList<>();
        chamarreDefs.add("Qui est orné de couleurs vives et contrastées.");
        chamarreDefs.add("Qui a un aspect voyants et multicolore.");
        motDefinitions.put("chamarré", chamarreDefs);
        
        // Définitions pour "débuter"
        List<String> debuterDefs = new ArrayList<>();
        debuterDefs.add("Commencer quelque chose.");
        debuterDefs.add("Faire ses premiers pas dans un domaine.");
        debuterDefs.add("Inaugurer, lancer une activité.");
        motDefinitions.put("débuter", debuterDefs);
        
        // Définitions pour "liseron"
        List<String> liseronDefs = new ArrayList<>();
        liseronDefs.add("Plante grimpante de la famille des convolvulacées.");
        liseronDefs.add("Fleur en forme de trompette.");
        motDefinitions.put("liseron", liseronDefs);
        
        // Définitions pour "quatrième"
        List<String> quatriemeDefs = new ArrayList<>();
        quatriemeDefs.add("Qui occupe le rang numéro quatre dans une série.");
        quatriemeDefs.add("Qui vient après le troisième.");
        motDefinitions.put("quatrième", quatriemeDefs);
        
        // Définitions pour "léniniste"
        List<String> leninisteDefs = new ArrayList<>();
        leninisteDefs.add("Partisan des théories de Lénine.");
        leninisteDefs.add("Relatif au léninisme ou à ses principes.");
        motDefinitions.put("léniniste", leninisteDefs);
        
        // Définitions pour "grandiloquent"
        List<String> grandiloquentDefs = new ArrayList<>();
        grandiloquentDefs.add("Qui s'exprime avec emphase, de manière pompeuse.");
        grandiloquentDefs.add("Qui utilise un langage trop solennel.");
        motDefinitions.put("grandiloquent", grandiloquentDefs);
        
        // Définitions pour "attablé"
        List<String> attableDefs = new ArrayList<>();
        attableDefs.add("Qui est assis à table pour manger.");
        attableDefs.add("Qui participe à un repas.");
        motDefinitions.put("attablé", attableDefs);
        
        // Définitions pour "avalé"
        List<String> avaleDefs = new ArrayList<>();
        avaleDefs.add("Qui a été avalé, ingéré.");
        avaleDefs.add("Qui a été accepté sans discussion.");
        motDefinitions.put("avalé", avaleDefs);
        
        // Définitions pour "gourrer"
        List<String> gourrerDefs = new ArrayList<>();
        gourrerDefs.add("Faire une erreur, se tromper.");
        gourrerDefs.add("Commettre une faute par négligence.");
        motDefinitions.put("gourrer", gourrerDefs);
        
        // Définitions pour "éclusier"
        List<String> eclusierDefs = new ArrayList<>();
        eclusierDefs.add("Personne chargée de la manœuvre des écluses.");
        eclusierDefs.add("Gardien d'une écluse.");
        motDefinitions.put("éclusier", eclusierDefs);
        
        // Si le mot a des définitions spécifiques
        if (motDefinitions.containsKey(mot)) {
            return motDefinitions.get(mot);
        }
        
        // Définition générique
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
