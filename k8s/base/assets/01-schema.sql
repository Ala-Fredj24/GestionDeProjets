-- Bootstrap MySQL pour GestionDeProjets.
-- Le conteneur MySQL crée déjà la base via MYSQL_DATABASE.
-- Ce fichier garde seulement une initialisation idempotente côté Kubernetes.
-- Les tables applicatives sont créées/ajustées par Hibernate avec ddl-auto=update.

CREATE DATABASE IF NOT EXISTS `gestion_projet`
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `gestion_projet`;
