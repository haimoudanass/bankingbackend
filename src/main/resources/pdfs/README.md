# Documents RAG — Banque

Placez ici le fichier **`banque-reglementation.pdf`** (reglementation, produits, conditions des comptes).

Ce PDF sera decoupe en segments et indexe dans le vector store au demarrage de l'application
(`banking.ai.enabled=true`).

Apres la premiere indexation, un cache est cree dans `data/vector-store/vector-store.json`
pour eviter de re-indexer a chaque redemarrage.
