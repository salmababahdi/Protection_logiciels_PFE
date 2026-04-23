import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { authService } from '../services/authService';

export default function PrivateRoute() {
  return authService.isAuthenticated() ? <Outlet /> : <Navigate to="/login" replace />;
}
