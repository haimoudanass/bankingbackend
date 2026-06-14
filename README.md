# Digital Banking — Backend & Frontend

Application bancaire (Spring Boot 3 + Angular 17) avec gestion des clients, JWT et Spring Security 6.

## Structure

- `src/` — API REST Spring Boot (port **8085**)
- `digital-banking-front/` — Interface Angular (port **4200**)

## Prérequis

- Java 17+
- Node.js 18+
- Maven (ou `mvnw` inclus)

## Démarrage rapide

### Backend

```powershell
.\start-backend.ps1
```

Ou manuellement :

```powershell
.\mvnw.cmd spring-boot:run -DskipTests
```

### Frontend

```powershell
cd digital-banking-front
npm install
npm start
```

## URLs

| Service | URL |
|---------|-----|
| API | http://localhost:8085 |
| Angular | http://localhost:4200 |
| Swagger | http://localhost:8085/swagger-ui.html |
| Console H2 | http://localhost:8085/h2-console |

**H2 :** JDBC URL `jdbc:h2:mem:banking`, user `sa`, mot de passe vide.

## Authentification (JWT)

| Utilisateur | Mot de passe | Rôles |
|-------------|--------------|-------|
| `user1` | `12345` | USER (lecture) |
| `admin` | `12345` | USER + ADMIN |

`POST /login` avec `application/x-www-form-urlencoded` (`username`, `password`).

## Comptes de test

Utilisez `admin` pour créer/supprimer des clients, `user1` pour la consultation seule.

## Partie 4 — Spring AI, RAG & Telegram

Architecture inspiree des projets [Prof Youssfi](https://github.com/mohamedYoussfi) :
- [spring-ai-agents-angular](https://github.com/mohamedYoussfi/spring-ai-agents-angular) (RAG)
- [chatbot-spring-ai-mcp-telegram-client](https://github.com/mohamedYoussfi/chatbot-spring-ai-mcp-telegram-client) (Telegram)

### Activation

1. Copiez `application-local.properties.example` vers `application-local.properties`
2. Ajoutez votre cle OpenAI et le token Telegram (BotFather)
3. Placez `banque-reglementation.pdf` dans `src/main/resources/pdfs/`
4. Definissez `banking.ai.enabled=true` et `telegram.bot.enabled=true`

```properties
banking.ai.enabled=true
spring.ai.openai.api-key=sk-proj-...
telegram.bot.enabled=true
telegram.bot.token=...
telegram.bot.username=...
```

Le bot Telegram repond via **RAG** : le PDF est indexe dans un vector store, et `QuestionAnswerAdvisor` enrichit automatiquement les prompts.

