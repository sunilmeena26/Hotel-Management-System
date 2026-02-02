import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
// import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Booking from './pages/Booking';
import { AuthProvider } from './context/AuthContext';
import './styles/styles.css';
import GuestDetails from './components/GuestDetails';
import ReservationForm from './components/ReservationForm';
import PaymentPage from './components/PaymentPage';
import ConfirmationPage from './components/ConfirmationPage';
import ProtectedRoute from './components/ProtectedRoute';
//import AdminDashboard from './components/AdminDashboard';
import GuestPanel from './components/GuestPanel';
import RoomPanel from './components/RoomPanel';
import ReservationPanel from './components/ReservationPanel';

import UserDashboard from './components/UserDashboard';
// import AdminDashboard from './components/AdminDashboard';
import MainLayout from './components/MainLayout';
// import React, { useState, useContext, useEffect } from 'react'; // ✅ Add useEffect here
import React from 'react';
import About from './pages/About';
import Contact from './pages/Contact';
import PrivacyPolicy from './pages/PrivacyPolicy';
import FAQ from './pages/FAQ';
import UserProfile from './components/UserProfile';
import MyBookings from './pages/MyBookings';
import ReceptionistDashboard from './components/ReceptionistDashboard';
import PaymentStatusCheck from './components/PaymentStatusCheck';
import RegisterReceptionist from './components/RegisterReceptionist';
import ForgotPassword from './components/ForgotPassword';
import VerifyOtp from './components/VerifyOtp';
import ResetPassword from './components/ResetPassword';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          {/* Public Routes */}
         {/* <Route path="/admin" element={<AdminDashboard />} /> */}
          {/* //<Route path="/" element={<Home />} /> */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/book/:roomNumber" element={<Booking />} />
          <Route path="/guest-details" element={<GuestDetails />} />
          <Route path="/reservation" element={<ReservationForm />} />
          <Route path="/payment" element={<PaymentPage />} />
          <Route path="/confirmation" element={<ConfirmationPage />} />
         {/* <Route path="/" element={<Home />} /> */} {/* ❌ Remove */}
<Route path="/" element={<MainLayout />} /> {/* ✅ Keep only one */}
<Route path="/about" element={<MainLayout content={<About />} />} />
<Route path="/contact" element={<MainLayout content={<Contact />} />} />
<Route path="/privacy-policy" element={<MainLayout content={<PrivacyPolicy />} />} />
<Route path="/faq" element={<MainLayout content={<FAQ />} />} />
<Route path="/bookings" element={<MainLayout content={<MyBookings />} />} />
<Route path="/forgot-password" element={<ForgotPassword />} />
<Route path="/verify-otp" element={<VerifyOtp />} />
<Route path="/reset-password" element={<ResetPassword />} />
<Route
            path="/receptionist"
            element={
              <ProtectedRoute allowedRoles={['ROLE_RECEPTIONIST']}>
                <ReceptionistDashboard />
              </ProtectedRoute>
            }
          />
<Route
            path="/receptionist/payment-status"
            element={
              <ProtectedRoute allowedRoles={['ROLE_RECEPTIONIST']}>
                <PaymentStatusCheck />
              </ProtectedRoute>
            }
          />


<Route
  path="/receptionist/reservations"
  element={
    <ProtectedRoute allowedRoles={['ROLE_RECEPTIONIST']}>
      <ReservationPanel />
    </ProtectedRoute>
  }
/>
<Route
  path="/receptionist/guests"
  element={
    <ProtectedRoute allowedRoles={['ROLE_RECEPTIONIST']}>
      <GuestPanel />
    </ProtectedRoute>
  }
/>
<Route
  path="/receptionist/rooms"
  element={
    <ProtectedRoute allowedRoles={['ROLE_RECEPTIONIST']}>
      <RoomPanel />
    </ProtectedRoute>
  }
/>
<Route
  path="/profile"
  element={
    <ProtectedRoute allowedRoles={['ROLE_USER', 'ROLE_ADMIN', 'ROLE_RECEPTIONIST']}>
      <UserProfile />
    </ProtectedRoute>
  }
/>

          {/* Admin Routes */}
          {/* <Route
            path="/admin"
            element={
              <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
                <AdminDashboard />
              </ProtectedRoute>
            }
          /> */}
          {/* <Route
  path="/admin"
  element={
    <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
      <AdminDashboard />
    </ProtectedRoute>
  }
/> */}
          <Route
            path="/admin/guests"
            element={
              <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
                <GuestPanel />
              </ProtectedRoute>
            }
          />
          <Route
  path="/admin/register-receptionist"
  element={
    <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
      <RegisterReceptionist />
    </ProtectedRoute>
  }
/>
          <Route
            path="/admin/rooms"
            element={
              <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
                <RoomPanel />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/reservations"
            element={
              <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
                <ReservationPanel />
              </ProtectedRoute>
            }
          />

          {/* User Dashboard */}
          <Route
            path="/user-dashboard"
            element={
              <ProtectedRoute allowedRoles={['ROLE_USER']}>
                <UserDashboard />
              </ProtectedRoute>
            }
          />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;