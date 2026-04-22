import api from './api';

export const parametresService = {

  // GET /api/parametres/profile
  // Returns: { id, name, email, username, role, actif }
  getProfile: async () => {
    const res = await api.get('/api/parametres/profile');
    return res.data;
  },

  // PUT /api/parametres/profile
  // Body: { name, email }
  updateProfile: async ({ name, email }) => {
    const res = await api.put('/api/parametres/profile', { name, email });
    return res.data;
  },

  // PUT /api/parametres/password
  // Body: { currentPassword, newPassword, confirmPassword }
  changePassword: async ({ currentPassword, newPassword, confirmPassword }) => {
    const res = await api.put('/api/parametres/password', {
      currentPassword,
      newPassword,
      confirmPassword,
    });
    return res.data;
  },
};
