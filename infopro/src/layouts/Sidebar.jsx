import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import {
  LayoutDashboard, Users, Monitor, Key, FileText,
  Bell, Settings, LogOut, Shield,
} from 'lucide-react';
import { authService } from '../services/authService';
import './Sidebar.css';

const NAV = [
  { to: '/dashboard',     label: 'Dashboard',              icon: LayoutDashboard },
  { to: '/clients',       label: 'Clients',                icon: Users },
  { to: '/logiciels',     label: 'Logiciels',              icon: Monitor },
  { to: '/licences',      label: 'Licences',               icon: Key },
  { to: '/contrats',      label: "Contrats d'assistance",  icon: FileText },
  { to: '/notifications', label: 'Notifications',          icon: Bell, badge: 5 },
  { to: '/parametres',    label: 'Paramètres',             icon: Settings },
];

export default function Sidebar() {
  const navigate = useNavigate();
  const handleLogout = () => { authService.logout(); navigate('/login'); };

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <div className="sidebar-logo"><Shield size={22} /></div>
        <div>
          <div className="sidebar-brand-name">INFOPRO</div>
          <div className="sidebar-brand-sub">License Manager</div>
        </div>
      </div>

      <nav className="sidebar-nav">
        {NAV.map(({ to, label, icon: Icon, badge }) => (
          <NavLink key={to} to={to} className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}>
            <Icon size={18} />
            <span>{label}</span>
            {badge && <span className="sidebar-badge">{badge}</span>}
          </NavLink>
        ))}
      </nav>

      <button className="sidebar-logout" onClick={handleLogout}>
        <LogOut size={18} />
        <span>Déconnexion</span>
      </button>
    </aside>
  );
}
