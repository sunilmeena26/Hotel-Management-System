import axios from 'axios';

const API_URL = 'http://localhost:8080/rooms';

export const getAllRooms = async () => {
  return axios.get(`${API_URL}/getAll`);
};

export const getRoomByNumber = async (roomNumber) => {
  return axios.get(`${API_URL}/get/${roomNumber}`);
};

export const getAvailableRooms = () => {
  return axios.get(`${API_URL}/available`);
};

export const getAvailableRoomsByType = async (type) => {
  return axios.get(`${API_URL}/available/byType?type=${type}`);
};

export const getAvailableRoomsByDate = async (checkInDate, checkOutDate) => {
  return axios.get(`http://localhost:8080/rooms/availableByDate/${checkInDate}/${checkOutDate}`);
};