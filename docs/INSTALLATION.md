# Installation

Prérequis et procédure complète pour faire tourner le projet sur une machine
vierge (Linux, macOS, Windows WSL).

## 📋 Prérequis

| Outil | Version | Vérification |
|---|---|---|
| Java JDK | 17+ | `java -version` |
| Maven | 3.9+ | `mvn -version` |
| Podman (ou Docker) | 4+ | `podman --version` |
| Git | 2.30+ | `git --version` |
| `gh` CLI | 2.40+ (*optionnel, pour le workflow DevOps*) | `gh --version` |

## ☕ Java 17

### Fedora / RHEL
```bash
sudo dnf install -y java-17-openjdk-devel
```

### Debian / Ubuntu
```bash
sudo apt install -y openjdk-17-jdk
```

### macOS (Homebrew)
```bash
brew install openjdk@17
```

### Windows
Télécharger depuis [Adoptium](https://adoptium.net/temurin/releases/?version=17).

## 📦 Maven

### Fedora
```bash
sudo dnf install -y maven
```

### Debian / Ubuntu
```bash
sudo apt install -y maven
```

### macOS
```bash
brew install maven
```

## 🐋 MariaDB via Podman

Le projet pré-suppose un conteneur MariaDB local nommé `jpa-mariadb`, pattern
conforme au cours.

### Installation de Podman

- **Fedora** : préinstallé.
- **Debian/Ubuntu** : `sudo apt install -y podman`
- **macOS** : `brew install podman` puis `podman machine init && podman machine start`

### Lancement du conteneur

```bash
podman run -d \
  --name jpa-mariadb \
  -e MARIADB_ROOT_PASSWORD=root \
  -p 3306:3306 \
  mariadb:10.11
```

### Création de la DB `petstore`

```bash
cd multi-couches/
podman exec -i jpa-mariadb mariadb -uroot -proot < init-db/01-petstore.sql
```

## 🚀 Cloner + builder le projet

```bash
git clone git@github.com:CybLow/multi-couches.git
cd multi-couches
mvn clean compile
```

## ▶️ Exécution

```bash
mvn exec:java
```

Les logs Hibernate montrent :
1. `CREATE TABLE …` pour chaque entité (`hibernate.hbm2ddl.auto=create`)
2. Les `INSERT` du `SeedService` (3+ enregistrements par table)
3. Le résultat de la requête JPQL `findAllAnimalsByPetStore(1L)` (Fish + Cat mélangés via polymorphisme JOINED)

## 💻 Import dans IntelliJ IDEA

1. `File → Open…` → sélectionner le dossier `multi-couches/`
2. IntelliJ détecte automatiquement le projet Maven
3. Attendre l'import Maven (bottom-right indicator)
4. Vérifier : `File → Project Structure → Project SDK = 17`
5. Clic-droit sur `src/main/java/fr/esaip/petstore/Main` → `Run 'Main.main()'`

## 🚨 Troubleshooting

Voir [`DATABASE.md`](./DATABASE.md#-troubleshooting) pour les erreurs DB.

### `mvn: command not found`
- Maven n'est pas dans le `PATH`. Sur Linux : `which mvn`. Sur macOS Homebrew : `brew link maven`.

### `UnsupportedClassVersionError` au runtime
- JDK utilisé < 17. Vérifier `echo $JAVA_HOME` et `java -version`.

### `Port 3306 already in use`
- Un service MariaDB/MySQL tourne déjà. `sudo systemctl stop mariadb` (ou MySQL), puis relancer le conteneur.

### Les entités ne se créent pas
- Vérifier `hibernate.hbm2ddl.auto=create` dans `persistence.xml`.
- Vérifier que la DB `petstore` existe et que `jpa_user` a les droits.
