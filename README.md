# Client Java pour l'API Mistral AI

Ce projet est un client Java simple pour interroger l'API de Mistral AI en utilisant le modèle **mistral-tiny** (gratuit) ou d'autres modèles comme **mistral-large-2407** (avec cache de contexte).

## ✨ Fonctionnalités

- **Requêtes API basiques** vers Mistral AI
- **Mode JSON structuré** (JSON Mode officiel) pour des réponses garanties en JSON
- **Support du cache de contexte** pour les modèles compatibles (ex: `mistral-large-2407`)
- **Interface interactive** en console avec plusieurs modes
- **Gestion des schémas JSON personnalisés**

## 📦 Prérequis

- Java 17 ou supérieur
- Maven 3.6 ou supérieur
- Une **clé API Mistral AI** (gratuite ou payante selon le modèle utilisé)

## 🔑 Obtenir une clé API gratuite

1. Allez sur [https://console.mistral.ai/](https://console.mistral.ai/)
2. Inscrivez-vous ou connectez-vous
3. Allez dans "API Keys" ou "Clés API"
4. Créez une nouvelle clé API
5. Copiez la clé générée

## 📥 Installation

### 1. Cloner le dépôt
```bash
git clone https://github.com/emmanueldufour/mistral.git
cd mistral
```

### 2. Remplacer la clé API
Ouvrez le fichier `src/main/java/fr/example/Main.java` et remplacez la ligne 14 :
```java
private static final String API_KEY = "votre_cle_api_mistral_ici";
```
par votre clé API réelle.

## 🚀 Utilisation

### Compilation
```bash
mvn clean compile
```

### Exécution
```bash
mvn exec:java -Dexec.mainClass="fr.example.Main"
```

## 🎯 Commandes disponibles

Une fois le programme lancé, vous pouvez utiliser les commandes suivantes :

| Commande | Description |
|----------|-------------|
| `/normal` | Mode texte normal (réponses libres) |
| `/json` | Mode JSON (réponses structurées garanties) |
| `/schema <schéma>` | Mode JSON avec schéma personnalisé |
| `/context` | Mode avec cache de contexte (nécessite un modèle compatible comme `mistral-large-2407`) |
| `/clear` | Effacer le contexte de la conversation |
| `/quit` | Quitter le programme |

### Exemples d'utilisation

#### 1. Mode texte normal
```
Vous: /normal
Vous: Quelle est la capitale de la France ?
Assistant: La capitale de la France est Paris.
```

#### 2. Mode JSON structuré
```
Vous: /json
Vous: Donne-moi 2 pays avec leur capitale en JSON
Assistant (JSON): {"pays": [{"nom": "France", "capitale": "Paris"}, {"nom": "Espagne", "capitale": "Madrid"}]}
```

#### 3. Mode avec schéma personnalisé
```
Vous: /schema {"type":"object","properties":{"nom":{"type":"string"},"age":{"type":"number"}}}
Vous: Décris-toi
Assistant (JSON avec schéma): {"nom": "Mistral", "age": 2}
```

#### 4. Mode avec cache de contexte (économie de tokens)
```
Vous: /context
Vous: Quelle est la capitale de la France ?
Assistant (avec cache de contexte): La capitale de la France est Paris.
Vous: Et celle de l'Espagne ?
Assistant (avec cache de contexte): La capitale de l'Espagne est Madrid.
```
> ⚠️ **Note**: Le cache de contexte nécessite un modèle compatible comme `mistral-large-2407`. Les tokens du contexte sont mis en cache côté serveur par Mistral.

## 📂 Structure du projet

```
mistral/
├── pom.xml                          # Configuration Maven
├── README.md                        # Documentation
└── src/main/java/fr/example/
    ├── Main.java                    # Programme principal avec interface interactive
    └── MistralClient.java           # Client API avec support JSON Mode et cache de contexte
```

## 🔧 Configuration avancée

### Changer le modèle utilisé
Par défaut, le client utilise `mistral-tiny` (gratuit). Vous pouvez changer le modèle dans le code :

```java
// Dans MistralClient.java
ChatRequest request = new ChatRequest();
request.model = "mistral-large-2407"; // Modèle avec cache de contexte
```

### Modèles compatibles avec le cache de contexte
- `mistral-large-2407` (recommandé)
- `mistral-large-2402` (partiellement)

> ⚠️ **Note**: Le modèle `mistral-tiny` (gratuit) ne supporte **pas** le cache de contexte.

## 💰 Cache de contexte et économie de tokens

Le **cache de contexte** est une fonctionnalité officielle de Mistral qui permet de :
- **Réutiliser les tokens input déjà traités** dans la même conversation
- **Économiser jusqu'à 90% des tokens** pour les requêtes avec un contexte répété
- **Améliorer les performances** en évitant de retraiter les mêmes informations

### Exemple d'économie
| Scénario | Sans cache | Avec cache | Économie |
|----------|------------|------------|----------|
| 10 requêtes avec 1000 tokens de contexte | 10 × 1010 = **10 100 tokens** | 1000 + (9 × 10) = **1090 tokens** | **~90%** |

### Comment l'utiliser ?
1. Utilisez un modèle compatible (`mistral-large-2407`)
2. Maintenez le même ordre des messages dans le contexte
3. Ajoutez progressivement des messages à la conversation

## 📚 Utilisation dans votre code

### Exemple 1: Requête simple
```java
MistralClient client = new MistralClient("votre_clé_api");
String response = client.sendMessage("Quelle est la capitale de la France ?");
System.out.println(response);
```

### Exemple 2: Mode JSON
```java
String jsonResponse = client.sendMessageWithJSON("Donne-moi 3 livres en JSON");
System.out.println(jsonResponse);
// Output: {"livres": [{"titre": "1984", "auteur": "George Orwell"}, ...]}
```

### Exemple 3: Avec cache de contexte
```java
List<MistralClient.Message> context = new ArrayList<>();
context.add(new MistralClient.Message("system", "Tu es un expert en géographie."));

// Première question
String response1 = client.sendMessageWithContextCache(context, "Quelle est la capitale de la France ?");
context.add(new MistralClient.Message("assistant", response1));

// Deuxième question (le contexte est en cache)
String response2 = client.sendMessageWithContextCache(context, "Et celle de l'Espagne ?");
```

## 🛠️ Dépendances

- **OkHttp 4.12.0**: Client HTTP pour faire les requêtes
- **Gson 2.10.1**: Bibliothèque pour parser le JSON

## 📜 Licence

Ce projet est sous licence MIT.

## 🔗 Ressources

- [Documentation officielle Mistral AI](https://docs.mistral.ai/)
- [Console Mistral AI](https://console.mistral.ai/)
- [API Reference](https://docs.mistral.ai/api/)
- [JSON Mode Documentation](https://docs.mistral.ai/capabilities/structured_output/json_mode)
