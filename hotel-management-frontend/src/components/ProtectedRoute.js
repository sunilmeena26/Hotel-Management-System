import React, { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const ProtectedRoute = ({ children, allowedRoles }) => {
  const { user } = useContext(AuthContext);

  const userRoles = user?.roles || [];

  const hasAccess = allowedRoles.some(role => userRoles.includes(role));

  if (!user || !hasAccess) {
    return <Navigate to="/login" />;
  }

  return children;
};

export default ProtectedRoute;