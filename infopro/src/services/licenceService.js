import api from './api';

const toBackend = (form) => ({
  clientId: form.clientId,
  logicielId: form.logicielId,
  contratId: form.contratId,
  cleLicence: form.cle,
  codeDebridage: form.codeDebridage,
  nbPostesAutorises: parseInt(form.postes) || 1,
  nbPostesActuels: parseInt(form.postesUtilises) || 0,
  dateDebut: form.dateDebut,
  dateFin: form.dateFin,
  statut: form.statut,
});

const toFrontend = (licence) => ({
  id: licence.id,
  cle: licence.cleLicence,
  codeDebridage: licence.codeDebridage,
  clientId: licence.clientId,
  clientNom: licence.clientNom,
  logicielId: licence.logicielId,
  logicielNom: licence.logicielNom,
  contratId: licence.contratId,
  postes: licence.nbPostesAutorises,
  postesUtilises: licence.nbPostesActuels,
  dateDebut: licence.dateDebut,
  dateFin: licence.dateFin,
  statut: licence.statut,
});

export const licenceService = {
  getAll: async () => {
    const res = await api.get('/api/licences');
    return res.data.map(toFrontend);
  },
  getById: async (id) => {
    const res = await api.get(`/api/licences/${id}`);
    return toFrontend(res.data);
  },
  getByClient: async (clientId) => {
    const res = await api.get(`/api/licences/client/${clientId}`);
    return res.data.map(toFrontend);
  },
  getByLogiciel: async (logicielId) => {
    const res = await api.get(`/api/licences/logiciel/${logicielId}`);
    return res.data.map(toFrontend);
  },
  getByStatut: async (statut) => {
    const res = await api.get(`/api/licences/statut/${statut}`);
    return res.data.map(toFrontend);
  },
  create: async (form) => {
    const res = await api.post('/api/licences', toBackend(form));
    return toFrontend(res.data);
  },
  update: async (id, form) => {
    const res = await api.put(`/api/licences/${id}`, toBackend(form));
    return toFrontend(res.data);
  },
  delete: async (id) => api.delete(`/api/licences/${id}`),
  activer: async (id) => {
    const res = await api.patch(`/api/licences/${id}/activer`);
    return toFrontend(res.data);
  },
  desactiver: async (id) => {
    const res = await api.patch(`/api/licences/${id}/desactiver`);
    return toFrontend(res.data);
  },
  suspendre: async (id, motif) => {
    const res = await api.patch(`/api/licences/${id}/suspendre`, null, { params: { motif } });
    return toFrontend(res.data);
  },
  reactiver: async (id) => {
    const res = await api.patch(`/api/licences/${id}/reactiver`);
    return toFrontend(res.data);
  },
};
