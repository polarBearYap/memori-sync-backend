const { initializeApp } = require('firebase/app');
const { getAuth, signInWithEmailAndPassword, connectAuthEmulator } = require('firebase/auth');
const fs = require('fs');

// Check if the file path is provided as a command line argument
const args = process.argv.slice(2);
if (args.length !== 4) {
    console.error("Usage: node main.js <custom_token> <firebase_config_path> <auth_emulator_url>");
    process.exit(1);
}

const email = args[0];
const password = args[1];
const firebaseConfigPath = args[2];
const authEmulatorUrl = args[3];

// Read firebase config from the provided file path
const firebaseConfig = JSON.parse(fs.readFileSync(firebaseConfigPath));

const app = initializeApp(firebaseConfig);

async function getIdToken() {
    try {
        const auth = getAuth(app);
        connectAuthEmulator(auth, authEmulatorUrl);
        await signInWithEmailAndPassword(auth, email, password);
        const user = auth.currentUser;
        if (user) {
            const idToken = await user.getIdToken();
            console.log(idToken);
            return idToken;
        } else {
            throw new Error("Failed to sign in with custom token.");
        }
    } catch (error) {
        console.error("Error signing in with custom token:", error.message);
        throw error;
    }
}

// Call getIdToken function with the custom token
getIdToken()
    .then((idToken) => {
        // Send this ID token to your server for verification
        // Use fetch or another HTTP client to send the ID token to your server
    })
    .catch((error) => {
        // Handle error
    });
