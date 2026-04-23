import api from './api';

// ── Single source of truth for storage keys ───────────────────
const TOKEN_KEY = 'token';
const USER_KEY = 'user';

const saveSession = (data) => {
  localStorage.setItem(TOKEN_KEY, data.token);
  localStorage.setItem(USER_KEY, JSON.stringify({
    id: data.id,
    name: data.name,
    email: data.email,
    role: data.role,
  }));
};

export const authService = {

  // POST /api/auth/login
  login: async (email, password) => {
    try {
      const { data } = await api.post('/api/auth/login', { email, password });
      saveSession(data);
      return data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.response?.data ||
        'Email ou mot de passe incorrect.';
      throw new Error(message);
    }
  },

  // POST /api/auth/register
  register: async ({ name, email, password }) => {
    try {
      const { data } = await api.post('/api/auth/register', { name, email, password });
      saveSession(data);
      return data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.response?.data ||
        "Erreur lors de la création du compte.";
      throw new Error(message);
    }
  },

  logout: () => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  },

  getToken: () => localStorage.getItem(TOKEN_KEY),
  getUser: () => { const u = localStorage.getItem(USER_KEY); return u ? JSON.parse(u) : null; },
  isAuthenticated: () => !!localStorage.getItem(TOKEN_KEY),
};