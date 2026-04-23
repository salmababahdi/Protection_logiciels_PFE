export const MOCK_CLIENTS = [
  { id: 1, code: 'CLI-001', nom: 'Entreprise DIKAR', email: 'dikar2002@gmail.com', telephone: '+212 519841278', adresse: '45 rue de Allal el fassi', ville: 'Marrakech', statut: 'Actif' },
  { id: 2, code: 'CLI-002', nom: 'TechCorp SA',      email: 'contact@techcorp.com', telephone: '+33 1 23 45 67 89', adresse: '12 avenue Victor Hugo', ville: 'Paris', statut: 'Actif' },
  { id: 3, code: 'CLI-003', nom: 'TechCorp SARL',    email: 'info@techcorpsarl.ma', telephone: '+212 522 334455', adresse: '7 boulevard Zerktouni', ville: 'Casablanca', statut: 'Actif' },
  { id: 4, code: 'CLI-004', nom: 'Digital Agency',   email: 'hello@digitalagency.com', telephone: '+33 4 56 78 90 12', adresse: '89 rue de la Liberté', ville: 'Lyon', statut: 'Inactif' },
  { id: 5, code: 'CLI-005', nom: 'Global Corp',      email: 'global@corp.net', telephone: '+1 555 234 5678', adresse: '200 Main Street', ville: 'New York', statut: 'Suspendu' },
];

export const MOCK_LOGICIELS = [
  { id: 1, code: 'SOFT-001', nom: 'Microsoft Office', version: '2024.1.0', description: 'Suite bureautique complète',  statut: 'Actif' },
  { id: 2, code: 'CPT-001',  nom: 'SuperCompta',      version: '2025.1.0', description: 'Logiciel de comptabilité',    statut: 'Actif' },
  { id: 3, code: 'SOFT-002', nom: 'INFOPRO Suite',    version: '3.2.1',    description: 'Suite de gestion intégrée',   statut: 'Actif' },
  { id: 4, code: 'SOFT-003', nom: 'Sage 100',         version: '9.0.0',    description: 'Gestion PME avancée',         statut: 'Inactif' },
];

export const MOCK_LICENCES = [
  { id: 1, cle: 'L0ZK-PSPD-WR43-FWGG', clientId: 2, clientNom: 'TechCorp SA',   logicielId: 3, logicielNom: 'INFOPRO Suite', postes: 6, postesUtilises: 0, dateDebut: '2026-02-21', dateFin: '2026-12-21', statut: 'Active',   codeDebridage: 'FXQC3-UNEER-KTC9R-7GFHF-S3HS9' },
  { id: 2, cle: 'LIC-2024-001',         clientId: 3, clientNom: 'TechCorp SARL', logicielId: 1, logicielNom: 'Microsoft Office', postes: 5, postesUtilises: 3, dateDebut: '2024-01-01', dateFin: '2024-12-31', statut: 'Expirée', codeDebridage: 'ABCD-1234-EFGH-5678' },
];

export const MOCK_CONTRATS = [
  { id: 1, numero: 'Contrat-01-2026', clientId: 3, clientNom: 'TechCorp SARL', cleLicence: 'LIC-2024-001', dateDebut: '2026-02-28', dateFin: '2026-05-30', statut: 'Actif' },
  { id: 2, numero: 'Contrat-02-2026', clientId: 2, clientNom: 'TechCorp SA',   cleLicence: 'L0ZK-PSPD-WR43-FWGG', dateDebut: '2026-01-15', dateFin: '2026-07-15', statut: 'Actif' },
];

export const MOCK_NOTIFICATIONS = [
  { id: 1, type: 'alerte', titre: 'Licence expirée',         message: 'La licence Microsoft Office pour Tech Solution a expiré.', date: '2024-01-15', statut: 'Non lu' },
  { id: 2, type: 'message', titre: 'Nouveau contrat signé',  message: "Digital Agency a signé un nouveau contrat d'assistance.", date: '2024-01-15', statut: 'Non lu' },
  { id: 3, type: 'alerte', titre: 'Renouvellement proche',   message: 'Le contrat de Global Corp expire dans 7 jours.',          date: '2024-01-14', statut: 'Non lu' },
  { id: 4, type: 'message', titre: 'Mise à jour disponible', message: 'Une nouvelle version de Sage 100 est disponible.',         date: '2024-01-14', statut: 'Lu' },
];

export const DASHBOARD_STATS = {
  totalClients: 248, licencesActives: 1847, licencesExpirees: 23,
  contratsEnCours: 156, expirantBientot: 12, paiementsAttente: 8,
  totalLicences: 1958, actives: 1847, suspendues: 88, expirees: 23,
};

export const EVOLUTION_DATA = [
  { mois: 'Jan', activations: 145 }, { mois: 'Fév', activations: 178 },
  { mois: 'Mar', activations: 156 }, { mois: 'Avr', activations: 210 },
  { mois: 'Mai', activations: 189 }, { mois: 'Juin', activations: 234 },
  { mois: 'Juil', activations: 267 }, { mois: 'Août', activations: 198 },
];
