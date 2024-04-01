# Memori Sync Backend
Spring backend for Memori app's cloud sync.

## Dev Requirements
1. OpenJDK version 21
2. PostgreSQL
3. Firebase Auth Setup
4. Firebase local emulator
5. Docker

## Getting Started
### Firebase
1. This application requires Firebase credentials to validate Firebase users at every API request.
2. Create a new Firebase project and enable Firebase Auth.
3. Go to Project settings > Service accounts > Manage service account permissions to navigate to Google Cloud Platform (GCP).
4. Create a custom role that is assigned with:
- firebaseappcheck.appCheckTokens.verify
- firebaseauth.users.get
- firebaseauth.users.update
5. Create a new service account with that custom role in the Google Cloud Platform.
6. Go to the service account detail page > Keys to generate a key in JSON format.
7. Place the generated JSON file at the project root and rename as firebase-config.json.
8. Add two properties in the firebase-config.json as shown below:
```
{
  "projectId": "<your-firebase-project-id>",
  "apiKey": "<the-generated-api-key-from-last-step>",
}
```

### IDE setup
1. The steps below are for Visual Studio Code. You may adapt it in other IDEs.
2. The following VS Code extensions are required:
- Extension Pack for Java [(links)](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)
- Spring Boot Extension Pack [(links)](https://marketplace.visualstudio.com/items?itemName=vmware.vscode-boot-dev-pack)
- Spring Boot Dashboard [(links)](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-spring-boot-dashboard)
3. Open launch.json and add the environment variables as shown below:
```
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Current File",
            "request": "launch",
            "mainClass": "${file}"
        },
        {
            "type": "java",
            "name": "MemoriApplication",
            "request": "launch",
            "mainClass": "com.memori.MemoriApplication",
            "projectName": "memori",
            "env": {
                "FIREBASE_PROJECT_ID": "<your-firebase-project-id>",
                "FIREBASE_PROJECT_NUMBER": "<your-firebase-project-number>",
                "GOOGLE_APPLICATION_CREDENTIALS": "firebase-config.json", // Get the credentials from GCP
                "FIREBASE_AUTH_EMULATOR_HOST": "127.0.0.1:9099", // Comment this line to disable Firebase local emulator
                "DB_JDBC_URL": "jdbc:postgresql://localhost:5432/<your-db-name>",
                "DB_USERNAME": "<your-db-username>",
                "DB_PASSWORD": "<your-db-password>",
                // flyway - Default profile
                // gensql - Generate postgresql script
                // gensql,h2 - Generate h2 script
                // gensql,sqlite - Generate sqlite script
                "SPRING_PROFILES_ACTIVE": "flyway",
                "FSRS_URL": "http://localhost:8000",
                "FSRS_SCHEDULE_CARD_PATH": "scheduleusercard",
            }
        }
    ]
}
```
4. Open settings.json and add the "java.test.config" as shown below:
```
"java.test.config": {
    "name": "MemoriApplicationTests",
    "env": {
        "FIREBASE_PROJECT_ID": "<your-firebase-project-id>",
        "FIREBASE_PROJECT_NUMBER": "<your-firebase-project-number>",
        "GOOGLE_APPLICATION_CREDENTIALS": "/absolute-file-path/firebase-config.json", // Must be an absolute path, should be located at project root
        "FIREBASE_AUTH_EMULATOR_HOST": "127.0.0.1:9099",
        "DB_JDBC_URL": "jdbc:postgresql://localhost:5432/memori_dev",
        "DB_USERNAME": "<your-db-username>",
        "DB_PASSWORD": "<your-db-password>",
        "NODEJS_CLIENT_REL_PATH": "src/test/firebase_client/main.js",
        "SPRING_PROFILES_ACTIVE": "h2,flyway,test",
        "FSRS_URL": "http://localhost:5001",
        "FSRS_PORT": "5001",
        "FSRS_SCHEDULE_CARD_PATH": "scheduleusercard",
    }
}
```
5. You can now debug and test application in VS code.
### Docker

## Libraries

## Project Structure

## License

Distributed under the MIT License. See LICENSE for more information.
