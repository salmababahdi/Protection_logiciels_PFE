import api from './api';

const toBackend = (form) => ({
  codeLogiciel: form.code,
  nom: form.nom,
  version: form.version,
  description: form.description,
  statut: form.statut,
});

const toFrontend = (logiciel) => ({
  id: logiciel.id,
  code: logiciel.codeLogiciel,
  nom: logiciel.nom,
  version: logiciel.version,
  description: logiciel.description,
  statut: logiciel.statut,
});

export const logicielService = {
  getAll: async () => {
    const res = await api.get('/api/logiciels');
    return res.data.map(toFrontend);
  },
  getById: async (id) => {
    const res = await api.get(`/api/logiciels/${id}`);
    return toFrontend(res.data);
  },
  getByStatut: async (statut) => {
    const res = await api.get(`/api/logiciels/statut/${statut}`);
    return res.data.map(toFrontend);
  },
  create: async (form) => {
    const res = await api.post('/api/logiciels', toBackend(form));
    return toFrontend(res.data);
  },
  update: async (id, form) => {
    const res = await api.put(`/api/logiciels/${id}`, toBackend(form));
    return toFrontend(res.data);
  },
  delete: async (id) => api.delete(`/api/logiciels/${id}`),
};
