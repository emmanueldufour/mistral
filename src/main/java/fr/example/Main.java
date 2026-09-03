package fr.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Programme principal pour tester le client Mistral AI
 * 
 * Fonctionnalit	s :
 * - Mode texte normal
 * - Mode JSON (rponses structures)
 * - Mode JSON avec schma personnalise
 * - Support du cache de contexte pour les modles compatibles
 * - Traitement du fichier inversions - sample.json pour gnrer des prompts
 * 
 * Pour excuter:
 * 1. Remplacez la cl API dans ce fichier (ligne 14)
 * 2. Compilez: mvn clean compile
 * 3. Excutez: mvn exec:java -Dexec.mainClass="fr.example.Main"
 */
public class Main {
    // Remplacez ici par votre cl API Mistral (obtenez-la sur https://console.mistral.ai/)
    private static final String API_KEY = "votre_cle_api_mistral_ici";
    private static final String INVERSION_FILE_PATH = "inversions - sample.json";
    
    public static void main(String[] args) {
        // Vrifier que la cl a t remplace
        if (API_KEY.equals("votre_cle_api_mistral_ici")) {
            System.err.println("Erreur: Veuillez remplacer la cl API dans le code source (Main.java, ligne 14).");
            System.err.println("Obtenez une cl API gratuite sur https://console.mistral.ai/");
            System.exit(1);
        }
        
        // Crer le client Mistral
        MistralClient client = new MistralClient(API_KEY);
        
        // Crer le processeur de donnes d'inversions
        InversionDataProcessor inversionProcessor = new InversionDataProcessor(client);
        
        // Liste pour le cache de contexte (pour les modles compatibles)
        List<MistralClient.Message> contextMessages = new ArrayList<>();
        
        // Lire l'entre utilisateur
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Client Java pour Mistral AI ===");
        System.out.println("Commandes:");
        System.out.println("  /normal - Mode texte normal");
        System.out.println("  /json - Mode JSON (rponses structures)");
        System.out.println("  /schema <schma> - Mode JSON avec schma personnalise");
        System.out.println("  /context - Mode avec cache de contexte (ncessite mistral-large-2407)");
        System.out.println("  /clear - Effacer le contexte");
        System.out.println("  /inversions - Traiter le fichier inversions - sample.json");
        System.out.println("  /inversions <mot> - Traiter un mot spcifique du fichier");
        System.out.println("  /quit - Quitter");
        System.out.println("\nTapez votre message:");
        
        String mode = "normal";
        String customSchema = "";
        
        while (true) {
            System.out.print("\nVous: ");
            String userInput = scanner.nextLine();
            
            if (userInput.equalsIgnoreCase("quit") || userInput.equalsIgnoreCase("exit")) {
                System.out.println("Au revoir!");
                break;
            }
            
            // Gestion des commandes
            if (userInput.equalsIgnoreCase("/normal")) {
                mode = "normal";
                System.out.println("Mode: Texte normal");
                continue;
            } else if (userInput.equalsIgnoreCase("/json")) {
                mode = "json";
                System.out.println("Mode: JSON (rponses structures)");
                System.out.println("Exemple: 'Donne-moi 3 livres avec titre et auteur en JSON'");
                continue;
            } else if (userInput.startsWith("/schema")) {
                mode = "schema";
                customSchema = userInput.substring(7).trim();
                System.out.println("Mode: JSON avec schma personnalise");
                System.out.println("Schma: " + customSchema);
                continue;
            } else if (userInput.equalsIgnoreCase("/context")) {
                mode = "context";
                System.out.println("Mode: Cache de contexte activ (modle: mistral-large-2407)");
                System.out.println("Les messages prcdents seront mis en cache ct Mistral");
                continue;
            } else if (userInput.equalsIgnoreCase("/clear")) {
                contextMessages.clear();
                System.out.println("Contexte effac");
                continue;
            } else if (userInput.equalsIgnoreCase("/inversions")) {
                // Traiter tout le fichier inversions
                try {
                    System.out.println("Traitement de tous les mots du fichier inversions...");
                    inversionProcessor.processAllMots(INVERSION_FILE_PATH);
                } catch (IOException e) {
                    System.err.println("Erreur lors du traitement du fichier: " + e.getMessage());
                }
                continue;
            } else if (userInput.startsWith("/inversions ")) {
                // Traiter un mot spcifique
                String mot = userInput.substring(12).trim();
                try {
                    System.out.println("Traitement du mot: " + mot);
                    String prompt = inversionProcessor.processSingleMot(INVERSION_FILE_PATH, mot);
                    System.out.println("\n--- Prompt gnr pour Mistral ---");
                    System.out.println(prompt);
                    
                    // Option: envoyer automatiquement  Mistral
                    System.out.println("\nVoulez-vous envoyer ce prompt  Mistral? (oui/non)");
                    String answer = scanner.nextLine();
                    if (answer.equalsIgnoreCase("oui") || answer.equalsIgnoreCase("yes")) {
                        String response = client.sendMessage(prompt);
                        System.out.println("Rponse de Mistral: " + response);
                    }
                } catch (IOException e) {
                    System.err.println("Erreur: " + e.getMessage());
                }
                continue;
            }
            
            if (userInput.isEmpty()) {
                continue;
            }
            
            try {
                String response;
                switch (mode) {
                    case "json":
                        System.out.print("Assistant (JSON): ");
                        response = client.sendMessageWithJSON(userInput);
                        break;
                    case "schema":
                        System.out.print("Assistant (JSON avec schma): ");
                        response = client.sendMessageWithSchema(userInput, customSchema);
                        break;
                    case "context":
                        System.out.print("Assistant (avec cache de contexte): ");
                        // Ajouter le message au contexte
                        contextMessages.add(new MistralClient.Message("user", userInput));
                        response = client.sendMessageWithContextCache(contextMessages, userInput);
                        // Ajouter la rponse au contexte pour la prochaine requte
                        contextMessages.add(new MistralClient.Message("assistant", response));
                        break;
                    default:
                        System.out.print("Assistant: ");
                        response = client.sendMessage(userInput);
                }
                System.out.println(response);
            } catch (IOException e) {
                System.err.println("Erreur lors de l'appel  l'API: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
}
