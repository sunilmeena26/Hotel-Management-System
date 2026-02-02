import axios from 'axios';

const API_URL = 'http://localhost:8080/api/reservations';

export const createReservation = async (reservation, token) => {
  return axios.post(`${API_URL}/add`, reservation, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export const getAllReservations = async (token) => {
  return axios.get(`${API_URL}/getAll`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export const getReservationById = async (id, token) => {
  return axios.get(`${API_URL}/get/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};