// import axios from 'axios';

// const API_URL = 'http://localhost:8080/auth';

// export const login = async (username, password) => {
//   return axios.post(`${API_URL}/login`, { username, password });
// };

// export const register = async (username, password, roles = 'USER') => {
//   return axios.post(`${API_URL}/register`, { username, password, roles });
// };


import axios from 'axios';

const API_URL = 'http://gateway-service/auth';

// export const login = async (username, password) => {
//   return axios.post(`${API_URL}/login`, { username, password });
// };

export const login = async (email, password) => {
  return axios.post(`${API_URL}/login`, 
    { email, password }, // ✅ Correct field names
    { headers: { 'Content-Type': 'application/json' } }
  );
};

export const register = async (name, email, password) => {
  return axios.post(`${API_URL}/register`, {
    name,
    email,
    password
  }, {
    headers: { 'Content-Type': 'application/json' }
  });
};