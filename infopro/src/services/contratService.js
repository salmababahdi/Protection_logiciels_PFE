import api from './api';

const toBackend = (form) => ({
  numeroContrat: form.numero,
  clientId: form.clientId,
  dateDebut: form.dateDebut,
  dateFin: form.dateFin,
  montant: parseFloat(form.montant) || 0,
  statut: form.statut,
});

const toFrontend = (contrat) => ({
  id: contrat.id,
  numero: contrat.numeroContrat,
  clientId: contrat.clientId,
  clientNom: contrat.clientNom,
  dateDebut: contrat.dateDebut,
  dateFin: contrat.dateFin,
  montant: contrat.montant,
  statut: contrat.statut,
});

export const contratService = {
  getAll: async () => {
    const res = await api.get('/api/contrats');
    return res.data.map(toFrontend);
  },
  getById: async (id) => {
    const res = await api.get(`/api/contrats/${id}`);
    return toFrontend(res.data);
  },
  getByClient: async (clientId) => {
    const res = await api.get(`/api/contrats/client/${clientId}`);
    return res.data.map(toFrontend);
  },
  getByStatut: async (statut) => {
    const res = await api.get(`/api/contrats/statut/${statut}`);
    return res.data.map(toFrontend);
  },
  create: async (form) => {
    const res = await api.post('/api/contrats', toBackend(form));
    return toFrontend(res.data);
  },
  update: async (id, form) => {
    const res = await api.put(`/api/contrats/${id}`, toBackend(form));
    return toFrontend(res.data);
  },
  delete: async (id) => api.delete(`/api/contrats/${id}`),
};
