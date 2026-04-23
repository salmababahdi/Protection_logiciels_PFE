import api from './api';

export const dashboardService = {

  // GET /api/dashboard
  // Returns: { totalClients, licencesActives, licencesExpirees,
  //            contratsEnCours, expirantBientot, paiementsAttente,
  //            licencesSuspendues, evolution, alerts }
  getStats: async () => {
    const res = await api.get('/api/dashboard');
    return res.data;
  },
};
