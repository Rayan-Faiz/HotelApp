<h1>HotelApp</h1> 
<h2>Cahier des charges - Application de réservation d'hôtel</h2>
Objectif du Projet
<p>Le projet vise à développer une application permettant aux clients de l'hôtel de réaliser des réservations en ligne. L'application sera divisée en deux parties distinctes :</p>

<ol>
  <li>Partie Back-office (CRUD) :</li>
  <p>Accessible uniquement aux administrateurs, cette section offrira les fonctionnalités suivantes :</p>
  
  <p><ul>
    <li>Gestion des Employés/Utilisateurs : Création, modification, et suppression de comptes employés avec différents niveaux d'autorisation.</li>
    <li>Gestion des Clients : Enregistrement, mise à jour et suppression de profils clients.</li>
    <li>Gestion de la Disponibilité des Chambres/Suites : Suivi en temps réel de la disponibilité des chambres et suites.</li>
    <li>Gestion et Suivi des Réservations : Création, mise à jour et annulation des réservations, suivi des statuts.</li>
    <li>Exportation et Importation des Employés : Possibilité d'exporter/importer des données d'employés au format Excel ou CSV.</li>
 </ul> </p>
  
  <li>Partie Front-office :</li>
  <p>Accessible aux clients pour leur permettre d'accéder aux fonctionnalités suivantes :</p>

  <p><ul>
    <li>Inscription des Clients : Création de comptes clients avec des informations de base.
    <li>Réservation avec Confirmation par Mail : Possibilité de réserver des chambres/suites avec envoi automatique de confirmations par e-mail.</li>
    <li>Édition PDF du Bon de Réservation : Génération d'un bon de réservation au format PDF pour les clients.</li>
  </ul></p>

  
  <li>Spécifications Techniques :</li>
  <p><ul>
    <li>Base de Données : Utilisation de MySQL ou PostgreSQL pour stocker les données.</li>
    <li>Couche d'Accès aux Données : Utilisation de JPA (Java Persistence API) et Hibernate.</li>
    <li>Partie Back-office (Desktop) : Utilisation de JavaFX pour l'interface utilisateur.</li>
    <li>Partie Front-office (Web) : Utilisation de JSP (JavaServer Pages) et Servlets pour l'interface web.</li>
    <li>Authentification : Mise en place d'un système d'authentification sécurisé pour les utilisateurs et les clients.</li>
   <li> Fonctionnalités de Recherche, Filtrage, Tri et Pagination : Intégration de fonctionnalités avancées pour faciliter la navigation dans les listes de données.</li>
    <li>Envoi d'E-mail Asynchrone : Utilisation de JavaMail pour l'envoi asynchrone de notifications par e-mail.</li>
   </ul> </p> 
  
  <li>Livrables Attendus :</li>
  <p><ul>
    <li>Application fonctionnelle avec les fonctionnalités spécifiées.</li>
    <li>Documentation détaillée sur l'utilisation et la maintenance de l'application.</li>
    <li> Codes sources avec des commentaires pour faciliter la compréhension et la maintenance future.</li>
  </ul></p>
  
  <li>Contraintes et Échéancier :</li>
  <p><ul>
    <li>L'application doit être sécurisée pour garantir la confidentialité des données.</li>
    <li> Respect des normes de développement et des bonnes pratiques de codage.</li>
    <li>L'échéancier prévisionnel pour la livraison du projet est fixé pour le 07/01/2024.</li>
  </ul></p>
  
</ol>
