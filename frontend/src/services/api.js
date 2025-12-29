import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8888';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Interceptor to add JWT token to requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export const keynoteService = {
  getAll: () => api.get('/keynotes/queries'),
  getById: (id) => api.get(`/keynotes/queries/${id}`),
  create: (data) => api.post('/keynotes/commands', data),
  update: (id, data) => api.put(`/keynotes/commands/${id}`, data),
  delete: (id) => api.delete(`/keynotes/commands/${id}`)
};

export const conferenceService = {
  getAll: () => api.get('/conferences/queries'),
  getById: (id) => api.get(`/conferences/queries/${id}`),
  create: (data) => api.post('/conferences/commands', data),
  update: (id, data) => api.put(`/conferences/commands/${id}`, data),
  addReview: (conferenceId, data) => api.post(`/conferences/commands/${conferenceId}/reviews`, data),
  getReviews: (conferenceId) => api.get(`/conferences/queries/${conferenceId}/reviews`)
};

export const analyticsService = {
  getRealTimeAnalytics: () => api.get('/analytics/reviews/realtime')
};

export const authService = {
  login: (credentials) => api.post('/auth/login', credentials),
  register: (userData) => api.post('/auth/register', userData),
  logout: () => {
    localStorage.removeItem('token');
  }
};

export default api;
