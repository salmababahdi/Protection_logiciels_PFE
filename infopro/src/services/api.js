import axios from 'axios';

export const BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: BASE_URL,
  headers: { 'Content-Type': 'application/json' },
});

// ── REQUEST: attach token ─────────────────────────────────────
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    console.log(`[API →] ${config.method?.toUpperCase()} ${config.url} | token: ${token ? token.slice(0, 20) + '...' : 'MISSING ⚠️'}`);
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// ── RESPONSE: handle errors ───────────────────────────────────
api.interceptors.response.use(
  (response) => {
    console.log(`[API ✓] ${response.status} ${response.config.url}`);
    return response;
  },
  (error) => {
    const status = error.response?.status;
    const url = error.config?.url;
    console.error(`[API ✗] ${status} ${url}`);

    if (status === 403) {
      console.error('[API] 403 — Token in localStorage:', localStorage.getItem('token'));
      console.error('[API] 403 — Full request headers:', error.config?.headers);
    }
    if (status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;