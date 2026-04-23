import api from './api';

const formatDate = (dateStr) => {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  return d.toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit', year: 'numeric' });
};

export const notificationService = {
  // GET /api/notifications
  getAll: async () => {
    const res = await api.get('/api/notifications');
    // Transform backend data to frontend format
    return res.data.map(n => ({
      id: n.id,
      type: n.type || 'message',
      titre: n.titre,
      message: n.description || '',
      date: n.dateCreation ? formatDate(n.dateCreation) : formatDate(new Date()),
      statut: n.statut || 'Non lu',
    }));
  },

  // GET /api/notifications/:id
  getById: async (id) => {
    const res = await api.get(`/api/notifications/${id}`);
    const n = res.data;
    return {
      id: n.id,
      type: n.type || 'message',
      titre: n.titre,
      message: n.description || '',
      date: n.dateCreation ? formatDate(n.dateCreation) : formatDate(new Date()),
      statut: n.statut || 'Non lu',
    };
  },

  // GET /api/notifications/user/:userId
  getByUserId: async (userId) => {
    const res = await api.get(`/api/notifications/user/${userId}`);
    return res.data.map(n => ({
      id: n.id,
      type: n.type || 'message',
      titre: n.titre,
      message: n.description || '',
      date: n.dateCreation ? formatDate(n.dateCreation) : formatDate(new Date()),
      statut: n.statut || 'Non lu',
    }));
  },

  // POST /api/notifications
  create: async (data) => {
    const payload = {
      titre: data.titre,
      description: data.message,
      type: data.type || 'message',
      statut: data.statut || 'Non lu',
      userId: data.userId,
    };
    const res = await api.post('/api/notifications', payload);
    return res.data;
  },

  // PUT /api/notifications/:id
  update: async (id, data) => {
    const res = await api.put(`/api/notifications/${id}`, data);
    return res.data;
  },

  // DELETE /api/notifications/:id
  delete: async (id) => {
    const res = await api.delete(`/api/notifications/${id}`);
    return res.data;
  },

  // Mark all as read
  markAllAsRead: async () => {
    const notifications = await notificationService.getAll();
    const unread = notifications.filter(n => n.statut === 'Non lu');
    await Promise.all(unread.map(n => notificationService.update(n.id, { statut: 'Lu' })));
    return notificationService.getAll();
  },
};
