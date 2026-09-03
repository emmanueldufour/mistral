package fr.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Programme principal pour tester le client Mistral AI
 * 
 * Fonctionnalités :
 * - Mode texte normal
 * - Mode JSON (réponses structurées)
 * - Mode JSON avec schéma personnalisé
 * - Support du cache de contexte pour les modèles compatibles
 * 
 * Pour exécuter:
 * 1. Remplacez la clé API dans ce fichier (ligne 14)
 * 2. Compilez: mvn clean compile
 * 3. Exécutez: mvn exec:java -Dexec.mainClass="fr.example.Main"
 */
public class Main {
    // Remplacez ici par votre clé API Mistral (obtenez-la sur https://console.mistral.ai/)
    private static final String API_KEY = "votre_cle_api_mistral_ici";
    
    public static void main(String[] args) {
        // Vérifier que la clé a été remplacée
        if (API_KEY.equals("votre_cle_api_mistral_ici")) {
            System.err.println("Erreur: Veuillez remplacer la clé API dans le code source (Main.java, ligne 14).");
            System.err.println("Obtenez une clé API gratuite sur https://console.mistral.ai/");
            System.exit(1);
        }
        
        // Créer le client Mistral
        MistralClient client = new MistralClient(API_KEY);
        
        // Liste pour le cache de contexte (pour les modèles compatibles)
        List<MistralClient.Message> contextMessages = new ArrayList<>();
        
        // Lire l'entrée utilisateur
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Client Java pour Mistral AI ===");
        System.out.println("Commandes:");
        System.out.println("  /normal - Mode texte normal");
        System.out.println("  /json - Mode JSON (réponses structurées)");
        System.out.println("  /schema <schéma> - Mode JSON avec schéma personnalisé");
        System.out.println("  /context - Mode avec cache de contexte (nécessite mistral-large-2407)");
        System.out.println("  /clear - Effacer le contexte");
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
                System.out.println("Mode: JSON (réponses structurées)");
                System.out.println("Exemple: 'Donne-moi 3 livres avec titre et auteur en JSON'");
                continue;
            } else if (userInput.startsWith("/schema")) {
                mode = "schema";
                customSchema = userInput.substring(7).trim();
                System.out.println("Mode: JSON avec schéma personnalisé");
                System.out.println("Schéma: " + customSchema);
                continue;
            } else if (userInput.equalsIgnoreCase("/context")) {
                mode = "context";
                System.out.println("Mode: Cache de contexte activé (modèle: mistral-large-2407)");
                System.out.println("Les messages précédents seront mis en cache côté Mistral");
                continue;
            } else if (userInput.equalsIgnoreCase("/clear")) {
                contextMessages.clear();
                System.out.println("Contexte effacé");
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
                        System.out.print("Assistant (JSON avec schéma): ");
                        response = client.sendMessageWithSchema(userInput, customSchema);
                        break;
                    case "context":
                        System.out.print("Assistant (avec cache de contexte): ");
                        // Ajouter le message au contexte
                        contextMessages.add(new MistralClient.Message("user", userInput));
                        response = client.sendMessageWithContextCache(contextMessages, userInput);
                        // Ajouter la réponse au contexte pour la prochaine requête
                        contextMessages.add(new MistralClient.Message("assistant", response));
                        break;
                    default:
                        System.out.print("Assistant: ");
                        response = client.sendMessage(userInput);
                }
                System.out.println(response);
            } catch (IOException e) {
                System.err.println("Erreur lors de l'appel à l'API: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
}
