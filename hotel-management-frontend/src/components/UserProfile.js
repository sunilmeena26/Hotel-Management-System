import React, { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
//import { useNavigate } from 'react-router-dom';
import '../styles/UserProfile.css';
import MainLayout from '../components/MainLayout'; // ✅ Import layout

const UserProfile = () => {
  const { user } = useContext(AuthContext);
  //const navigate = useNavigate();

 

  const generatedUsername = user?.name
    ? `${user.name.replace(/\s+/g, '').toLowerCase()}${Math.floor(1000 + Math.random() * 9000)}`
    : 'Guest';

    const profileContent = (
      <div className="profile-container">
        <h2>User Profile</h2>
        <div className="profile-card">
          <div className="profile-row">
            <span className="label">Name:</span>
            <span className="value">{user?.name || 'Not available'}</span>
          </div>
          <div className="profile-row">
            <span className="label">Username:</span>
            <span className="value">{generatedUsername}</span>
          </div>
          <div className="profile-row">
            <span className="label">Email:</span>
            <span className="value">{user?.sub || 'Not available'}</span>
          </div>
          <div className="profile-row">
            <span className="label">Phone Number:</span>
            <span className="value">+91 9876543210</span>
          </div>
          <div className="profile-row">
            <span className="label">Address:</span>
            <span className="value">123, MG Road, Chennai</span>
          </div>
          <div className="profile-row">
            <span className="label">Gender:</span>
            <span className="value">Male</span>
          </div>
        </div>
      </div>
    );

  return <MainLayout content={profileContent} />;
};

export default UserProfile;