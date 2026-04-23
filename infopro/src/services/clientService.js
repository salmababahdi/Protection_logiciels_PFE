// ⚠️ IMPORTANT: adjust this import path to match your project structure
// If clientService.js is in src/services/ and api.js is also in src/services/ → use './api'
// If api.js is in src/           → use '../api'
import api from './api';

const toBackend = (form) => ({
  codeClient: form.code,
  raisonSociale: form.nom,
  email: form.email,
  telephone: form.telephone,
  adresse: form.adresse,
  ville: form.ville,
  statut: form.statut,
});

const toFrontend = (client) => ({
  id: client.id,
  code: client.codeClient,
  nom: client.raisonSociale,
  email: client.email,
  telephone: client.telephone,
  adresse: client.adresse,
  ville: client.ville,
  statut: client.statut,
});

export const clientService = {
  getAll: async () => {
    const res = await api.get('/api/clients');
    return res.data.map(toFrontend);
  },
  getById: async (id) => {
    const res = await api.get(`/api/clients/${id}`);
    return toFrontend(res.data);
  },
  getByStatut: async (statut) => {
    const res = await api.get(`/api/clients/statut/${statut}`);
    return res.data.map(toFrontend);
  },
  create: async (form) => {
    const res = await api.post('/api/clients', toBackend(form));
    return toFrontend(res.data);
  },
  update: async (id, form) => {
    const res = await api.put(`/api/clients/${id}`, toBackend(form));
    return toFrontend(res.data);
  },
  delete: async (id) => api.delete(`/api/clients/${id}`),
};