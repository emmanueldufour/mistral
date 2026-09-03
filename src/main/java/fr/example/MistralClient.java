package fr.example;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Client Java pour interroger l'API Mistral AI (modèle mistral-tiny)
 * API gratuite disponible sur https://mistral.ai/
 * 
 * Fonctionnalités :
 * - Support du JSON Mode (réponses structurées en JSON)
 * - Support du cache de contexte (pour les modèles compatibles comme mistral-large-2407)
 */
public class MistralClient {
    
    private static final String API_URL = "https://api.mistral.ai/v1/chat/completions";
    private final String apiKey;
    private final OkHttpClient client;
    private final Gson gson;
    
    public MistralClient(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }
    
    /**
     * Classe pour représenter un message dans la conversation
     */
    public static class Message {
        public String role; // "user" ou "assistant"
        public String content;
        
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
    
    /**
     * Classe pour représenter une requête à envoyer à l'API
     */
    public static class ChatRequest {
        public String model = "mistral-tiny"; // Modèle gratuit (remplacer par mistral-large-2407 pour le cache de contexte)
        public List<Message> messages;
        public double temperature = 0.7;
        public int max_tokens = 500;
        public ResponseFormat response_format; // Pour activer le JSON Mode
        
        public ChatRequest() {
            this.messages = new ArrayList<>();
        }
        
        public void addMessage(String role, String content) {
            this.messages.add(new Message(role, content));
        }
    }
    
    /**
     * Classe pour le format de réponse (JSON Mode)
     */
    public static class ResponseFormat {
        public String type = "json_object";
    }
    
    /**
     * Classe pour représenter la réponse de l'API
     */
    public static class ChatResponse {
        public String id;
        public String model;
        public List<Choice> choices;
        public long created;
        
        public static class Choice {
            public int index;
            public Message message;
            public Object finish_reason;
        }
    }
    
    /**
     * Envoyer une requête à l'API Mistral
     * @param request La requête contenant les messages
     * @return La réponse de l'API
     * @throws IOException En cas d'erreur réseau
     */
    public ChatResponse chat(ChatRequest request) throws IOException {
        // Créer le corps de la requête JSON
        String requestBody = gson.toJson(request);
        
        // Construire la requête HTTP
        Request httpRequest = new Request.Builder()
                .url(API_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .build();
        
        // Exécuter la requête
        try (Response response = client.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erreur API: " + response.code() + " - " + response.message());
            }
            
            String responseBody = response.body().string();
            return gson.fromJson(responseBody, ChatResponse.class);
        }
    }
    
    /**
     * Méthode simplifiée pour envoyer un message unique (mode texte)
     * @param message Le message de l'utilisateur
     * @return Le message réponse de l'assistant
     * @throws IOException En cas d'erreur
     */
    public String sendMessage(String message) throws IOException {
        ChatRequest request = new ChatRequest();
        request.addMessage("user", message);
        
        ChatResponse response = chat(request);
        
        if (response.choices == null || response.choices.isEmpty()) {
            throw new IOException("Aucune réponse reçue de l'API");
        }
        
        return response.choices.get(0).message.content;
    }
    
    /**
     * Méthode pour envoyer un message avec réponse structurée en JSON
     * Utilise le JSON Mode officiel de Mistral pour garantir une réponse en JSON valide
     * @param message Le message de l'utilisateur
     * @return La réponse au format JSON
     * @throws IOException En cas d'erreur
     */
    public String sendMessageWithJSON(String message) throws IOException {
        ChatRequest request = new ChatRequest();
        request.addMessage("user", message);
        request.response_format = new ResponseFormat(); // Active le JSON Mode
        
        ChatResponse response = chat(request);
        
        if (response.choices == null || response.choices.isEmpty()) {
            throw new IOException("Aucune réponse reçue de l'API");
        }
        
        return response.choices.get(0).message.content;
    }
    
    /**
     * Méthode pour envoyer un message avec un schéma JSON personnalisé
     * @param message Le message de l'utilisateur
     * @param jsonSchema Le schéma JSON attendu (ex: {"type": "object", "properties": {...}})
     * @return La réponse au format JSON
     * @throws IOException En cas d'erreur
     */
    public String sendMessageWithSchema(String message, String jsonSchema) throws IOException {
        ChatRequest request = new ChatRequest();
        request.addMessage("system", "Réponds UNIQUEMENT en JSON valide selon ce schéma: " + jsonSchema);
        request.addMessage("user", message);
        request.response_format = new ResponseFormat();
        
        ChatResponse response = chat(request);
        
        if (response.choices == null || response.choices.isEmpty()) {
            throw new IOException("Aucune réponse reçue de l'API");
        }
        
        return response.choices.get(0).message.content;
    }
    
    /**
     * Méthode pour envoyer une requête avec cache de contexte (modèles compatibles uniquement)
     * Utilisez un modèle comme mistral-large-2407 pour activer le cache de contexte côté Mistral
     * @param contextMessages Liste des messages formant le contexte (sera mis en cache par Mistral)
     * @param newMessage Le nouveau message de l'utilisateur
     * @return La réponse de l'assistant
     * @throws IOException En cas d'erreur
     */
    public String sendMessageWithContextCache(List<Message> contextMessages, String newMessage) throws IOException {
        ChatRequest request = new ChatRequest();
        request.model = "mistral-large-2407"; // Modèle compatible avec le cache de contexte
        request.messages = new ArrayList<>(contextMessages);
        request.messages.add(new Message("user", newMessage));
        
        ChatResponse response = chat(request);
        
        if (response.choices == null || response.choices.isEmpty()) {
            throw new IOException("Aucune réponse reçue de l'API");
        }
        
        return response.choices.get(0).message.content;
    }
}
