# Guide de Configuration et Implémentation

## Services Déjà Implémentés ✅

1. **common-api** - Module partagé avec Commands, Events, Queries, DTOs
2. **discovery-service** - Eureka Server complet
3. **gateway-service** - Spring Cloud Gateway avec routing

## Services à Compléter

### Structure Standard pour les Services

Chaque service suit la même structure Maven/Spring Boot:

```
service-name/
├── pom.xml
├── Dockerfile
└── src/
    └── main/
        ├── java/com/conference/[service]/
        │   ├── Application.java
        │   ├── aggregate/ (pour command services)
        │   ├── command/ (handlers)
        │   ├── query/ (handlers)
        │   ├── projection/ (event handlers)
        │   ├── entity/ (JPA entities)
        │   ├── repository/
        │   └── controller/
        └── resources/
            └── application.yml
```

### Keynote-Service-Command

**Fichiers requis:**
- `pom.xml` - Dépendances: Axon, Spring Data JPA, MySQL, Eureka Client, Kafka
- `KeynoteCommandServiceApplication.java` - @SpringBootApplication
- `KeynoteAggregate.java` - Aggregate avec @AggregateRoot
- `KeynoteCommandController.java` - REST endpoints pour commands
- `application.yml` - Configuration DB, Axon, Eureka

**Aggregate Example:**
```java
@Aggregate
public class KeynoteAggregate {
    @AggregateIdentifier
    private String keynoteId;
    
    @CommandHandler
    public KeynoteAggregate(CreateKeynoteCommand cmd) {
        AggregateLifecycle.apply(new KeynoteCreatedEvent(...));
    }
    
    @EventSourcingHandler
    public void on(KeynoteCreatedEvent event) {
        this.keynoteId = event.getKeynoteId();
        // set other fields
    }
}
```

### Keynote-Service-Query

**Fichiers requis:**
- `pom.xml`
- `KeynoteQueryServiceApplication.java`
- `KeynoteEntity.java` - JPA Entity
- `KeynoteRepository.java` - extends JpaRepository
- `KeynoteEventHandler.java` - @EventHandler pour projections
- `KeynoteQueryController.java` - REST endpoints pour queries
- `application.yml`

**Event Handler Example:**
```java
@Service
@ProcessingGroup("keynote-group")
public class KeynoteEventHandler {
    @EventHandler
    public void on(KeynoteCreatedEvent event) {
        keynoteRepository.save(new KeynoteEntity(...));
    }
}
```

### Conference-Service-Command

Similaire à Keynote-Command mais avec:
- `ConferenceAggregate.java`
- Support pour ReviewAddedEvent
- Kafka producer pour events

### Conference-Service-Query

Similaire à Keynote-Query mais avec:
- `ConferenceEntity.java`
- `ReviewEntity.java`
- Relations JPA entre Conference et Reviews

### Auth-Service

**Composants requis:**
- Spring Security
- JWT Token Provider
- User/Role entities et repositories
- UserDetailsService implementation
- AuthController (login, register endpoints)
- SecurityConfig avec filter chains

**Dependencies:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
```

### Analytics-Service

**Composants requis:**
- Kafka Streams configuration
- Stream processor pour ReviewAddedEvent
- Windowing (5 secondes)
- Aggregation (count, sum)
- REST endpoint pour résultats

**Kafka Streams Example:**
```java
@Bean
public KStream<String, ReviewAddedEvent> reviewStream(StreamsBuilder builder) {
    return builder.stream("review-events")
        .groupByKey()
        .windowedBy(TimeWindows.of(Duration.ofSeconds(5)))
        .aggregate(...)
        .toStream();
}
```

## Configuration des application.yml

### Template Command Service
```yaml
server:
  port: 808X

spring:
  application:
    name: service-name
  datasource:
    url: jdbc:mysql://localhost:3306/db_name
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update

axon:
  axonserver:
    servers: localhost:8124

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

spring.kafka:
  bootstrap-servers: localhost:9092
```

### Template Query Service
```yaml
server:
  port: 808X

spring:
  application:
    name: service-name
  datasource:
    url: jdbc:mysql://localhost:3306/db_name
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update

axon:
  axonserver:
    servers: localhost:8124

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

## Template pom.xml pour Services

```xml
<dependencies>
    <!-- Common API -->
    <dependency>
        <groupId>com.conference</groupId>
        <artifactId>common-api</artifactId>
        <version>1.0.0</version>
    </dependency>
    
    <!-- Axon -->
    <dependency>
        <groupId>org.axonframework</groupId>
        <artifactId>axon-spring-boot-starter</artifactId>
    </dependency>
    
    <!-- Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- MySQL -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>
    
    <!-- Eureka Client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    
    <!-- Kafka (for command services) -->
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

## Template Dockerfile

```dockerfile
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY ../pom.xml ./parent-pom.xml
COPY ../common-api ./common-api
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 808X
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Frontend React

### Structure
```
frontend/
├── package.json
├── Dockerfile
├── public/
└── src/
    ├── App.js
    ├── components/
    │   ├── Keynotes/
    │   ├── Conferences/
    │   └── Analytics/
    ├── services/
    │   └── api.js
    └── index.js
```

### API Service
```javascript
const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8888';

export const keynoteService = {
    getAll: () => fetch(`${API_URL}/keynotes/queries`).then(r => r.json()),
    create: (data) => fetch(`${API_URL}/keynotes/commands`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
};
```

### package.json
```json
{
  "name": "conference-frontend",
  "version": "1.0.0",
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "axios": "^1.5.0",
    "react-router-dom": "^6.16.0"
  },
  "scripts": {
    "start": "react-scripts start",
    "build": "react-scripts build"
  }
}
```

## Ordre de Démarrage

1. Infrastructure: MySQL, Axon Server, Kafka+Zookeeper, Keycloak
2. Discovery Service (attendre 30s)
3. Gateway Service
4. Auth Service
5. Command & Query Services (peuvent démarrer en parallèle)
6. Analytics Service
7. Frontend

## Tests des Endpoints

```bash
# Créer un keynote
curl -X POST http://localhost:8888/keynotes/commands \\
  -H "Content-Type: application/json" \\
  -d '{
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean@example.com",
    "fonction": "Speaker"
  }'

# Lister les keynotes
curl http://localhost:8888/keynotes/queries

# Créer une conférence
curl -X POST http://localhost:8888/conferences/commands \\
  -H "Content-Type: application/json" \\
  -d '{
    "titre": "Spring Boot Advanced",
    "type": "ACADEMIQUE",
    "date": "2024-06-15",
    "duree": 120,
    "nombreInscrits": 0
  }'
```

## Points Importants

1. **Event Sourcing**: Tous les événements sont stockés dans Axon Server
2. **CQRS**: Séparation stricte command/query avec bases de données dédiées
3. **Kafka**: Utilisé pour la communication asynchrone et analytics
4. **Security**: JWT tokens avec expiration de 24h
5. **Discovery**: Tous les services s'enregistrent automatiquement dans Eureka

## Dépannage

- Si Axon Server ne démarre pas: vérifier port 8124 libre
- Si Kafka échoue: Zookeeper doit être démarré en premier
- Si les services ne se découvrent pas: vérifier Eureka sur http://localhost:8761
- Si MySQL connection échoue: vérifier que les bases de données sont créées
