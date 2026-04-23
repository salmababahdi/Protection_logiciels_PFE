import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import DashboardLayout from './layouts/DashboardLayout';
import PrivateRoute     from './routes/PrivateRoute';

import LoginPage        from './pages/auth/LoginPage';
import RegisterPage     from './pages/auth/RegisterPage';
import DashboardPage    from './pages/dashboard/DashboardPage';
import ClientsPage      from './pages/clients/ClientsPage';
import LogicielsPage    from './pages/logiciels/LogicielsPage';
import LicencesPage     from './pages/licences/LicencesPage';
import ContratsPage     from './pages/contrats/ContratsPage';
import NotificationsPage from './pages/notifications/NotificationsPage';
import ParametresPage   from './pages/parametres/ParametresPage';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public */}
        <Route path="/login"    element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        {/* Protected */}
        <Route element={<PrivateRoute />}>
          <Route element={<DashboardLayout />}>
            <Route path="/"               element={<Navigate to="/dashboard" replace />} />
            <Route path="/dashboard"      element={<DashboardPage />} />
            <Route path="/clients"        element={<ClientsPage />} />
            <Route path="/logiciels"      element={<LogicielsPage />} />
            <Route path="/licences"       element={<LicencesPage />} />
            <Route path="/contrats"       element={<ContratsPage />} />
            <Route path="/notifications"  element={<NotificationsPage />} />
            <Route path="/parametres"     element={<ParametresPage />} />
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
