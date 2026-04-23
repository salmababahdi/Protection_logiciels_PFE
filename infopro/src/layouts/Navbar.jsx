import React, { useState } from 'react';
import { Search, Bell } from 'lucide-react';
import { authService } from '../services/authService';
import './Navbar.css';

export default function Navbar() {

  const user = authService.getUser();
  const [query, setQuery] = useState('');

  return (
    <header className="navbar">
      <div className="navbar-search">
        <Search size={15} className="navbar-search-icon" />
        <input
          className="navbar-search-input"
          placeholder="Rechercher un client, une licence..."
          value={query}
          onChange={e => setQuery(e.target.value)}
        />
      </div>

      <div className="navbar-right">
        <button className="navbar-icon-btn">
          <Bell size={18} />
          <span className="navbar-notif-dot" />
        </button>

        <div className="navbar-user">
          <div className="navbar-avatar">{user?.initials || 'JD'}</div>
          <div className="navbar-user-info">
            <div className="navbar-user-name">{user?.name || 'Jean Dupont'}</div>
            <div className="navbar-user-role">{user?.role || 'Administrateur'}</div>
          </div>
        </div>
      </div>
    </header>
  );
}
