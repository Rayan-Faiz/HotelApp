# HotelApp
Cahier des charges - Application de réservation d'hôtel
Objectif du Projet
Le projet vise à développer une application permettant aux clients de l'hôtel de réaliser des réservations en ligne. L'application sera divisée en deux parties distinctes :

Partie Back-office (CRUD) :
Accessible uniquement aux administrateurs, cette section offrira les fonctionnalités suivantes :

Gestion des Employés/Utilisateurs : Création, modification, et suppression de comptes employés avec différents niveaux d'autorisation.
Gestion des Clients : Enregistrement, mise à jour et suppression de profils clients.
Gestion de la Disponibilité des Chambres/Suites : Suivi en temps réel de la disponibilité des chambres et suites.
Gestion et Suivi des Réservations : Création, mise à jour et annulation des réservations, suivi des statuts.
Exportation et Importation des Employés : Possibilité d'exporter/importer des données d'employés au format Excel ou CSV.
... (Autres fonctionnalités à définir)
Partie Front-office :
Accessible aux clients pour leur permettre d'accéder aux fonctionnalités suivantes :

Inscription des Clients : Création de comptes clients avec des informations de base.
Réservation avec Confirmation par Mail : Possibilité de réserver des chambres/suites avec envoi automatique de confirmations par e-mail.
Édition PDF du Bon de Réservation : Génération d'un bon de réservation au format PDF pour les clients.
... (D'autres fonctionnalités à définir)
Spécifications Techniques :
Base de Données : Utilisation de MySQL ou PostgreSQL pour stocker les données.
Couche d'Accès aux Données : Utilisation de JPA (Java Persistence API) et Hibernate.
Partie Back-office (Desktop) : Utilisation de JavaFX pour l'interface utilisateur.
Partie Front-office (Web) : Utilisation de JSP (JavaServer Pages) et Servlets pour l'interface web.
Authentification : Mise en place d'un système d'authentification sécurisé pour les utilisateurs et les clients.
Fonctionnalités de Recherche, Filtrage, Tri et Pagination : Intégration de fonctionnalités avancées pour faciliter la navigation dans les listes de données.
Envoi d'E-mail Asynchrone : Utilisation de JavaMail pour l'envoi asynchrone de notifications par e-mail.
Livrables Attendus :
Application fonctionnelle avec les fonctionnalités spécifiées.
Documentation détaillée sur l'utilisation et la maintenance de l'application.
Codes sources avec des commentaires pour faciliter la compréhension et la maintenance future.
Contraintes et Échéancier :
L'application doit être sécurisée pour garantir la confidentialité des données.
Respect des normes de développement et des bonnes pratiques de codage.
L'échéancier prévisionnel pour la livraison du projet est fixé au [insérer la date de livraison prévue].
Notes Additionnelles :
Des réunions régulières seront tenues pour le suivi du développement.
Toute modification ou ajout de fonctionnalité en cours de développement doit être approuvé par le [insérer le responsable du projet/client].
