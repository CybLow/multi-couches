-- Script d'initialisation de la DB petstore pour le TP Eval Pet Store.
--
-- But : créer la base `petstore` et accorder les droits à `jpa_user`.
-- Les TABLES elles-mêmes seront créées par Hibernate au démarrage
-- (hibernate.hbm2ddl.auto = create, cf. persistence.xml).
--
-- Utilisation :
--   podman exec -i jpa-mariadb mariadb -uroot -proot < init-db/01-petstore.sql
--
-- (Ou dépôt automatique dans /docker-entrypoint-initdb.d/ lors du 1er démarrage
--  du conteneur.)

-- -----------------------------------------------------------------------------
-- Base
-- -----------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS `petstore`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Utilisateur applicatif (convention du cours : jpa_user / jpa_pass, partagé
-- avec les autres TPs JPA)
-- -----------------------------------------------------------------------------
CREATE USER IF NOT EXISTS 'jpa_user'@'%' IDENTIFIED BY 'jpa_pass';
GRANT ALL PRIVILEGES ON `petstore`.* TO 'jpa_user'@'%';
FLUSH PRIVILEGES;
