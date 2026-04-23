import React from 'react';
import './Ui.css';

/* ── Badge ── */
export function Badge({ children, variant = 'default' }) {
  return <span className={`badge badge-${variant}`}>{children}</span>;
}

export function StatusBadge({ statut }) {
  const map = {
    'Actif':    'success', 'Active':  'success',
    'Inactif':  'default', 'Expirée': 'danger',  'Expirée':'danger',
    'Suspendu': 'warning', 'Prospect':'info',
    'Non lu':   'danger',  'Lu':      'default',
  };
  return <Badge variant={map[statut] || 'default'}>{statut}</Badge>;
}

/* ── Spinner ── */
export function Spinner({ size = 20 }) {
  return (
    <div className="spinner-wrap">
      <div className="spinner" style={{ width: size, height: size }} />
    </div>
  );
}

/* ── Input ── */
export function Input({ label, error, icon: Icon, ...props }) {
  return (
    <div className="field">
      {label && <label className="field-label">{label}</label>}
      <div className="field-input-wrap">
        {Icon && <Icon size={15} className="field-icon" />}
        <input className={`field-input ${Icon ? 'field-input-icon' : ''} ${error ? 'field-input-error' : ''}`} {...props} />
      </div>
      {error && <span className="field-error">{error}</span>}
    </div>
  );
}

/* ── Select ── */
export function Select({ label, error, children, ...props }) {
  return (
    <div className="field">
      {label && <label className="field-label">{label}</label>}
      <select className={`field-select ${error ? 'field-input-error' : ''}`} {...props}>
        {children}
      </select>
      {error && <span className="field-error">{error}</span>}
    </div>
  );
}

/* ── Textarea ── */
export function Textarea({ label, error, ...props }) {
  return (
    <div className="field">
      {label && <label className="field-label">{label}</label>}
      <textarea className={`field-input field-textarea ${error ? 'field-input-error' : ''}`} {...props} />
      {error && <span className="field-error">{error}</span>}
    </div>
  );
}

/* ── StatCard ── */
export function StatCard({ label, value, icon: Icon, variant = 'default' }) {
  return (
    <div className={`stat-card stat-card-${variant}`}>
      <div className="stat-card-icon"><Icon size={18} /></div>
      <div className="stat-card-value">{value?.toLocaleString()}</div>
      <div className="stat-card-label">{label}</div>
    </div>
  );
}

/* ── PageHeader ── */
export function PageHeader({ title, subtitle, action }) {
  return (
    <div className="page-header">
      <div>
        <h1 className="page-title">{title}</h1>
        {subtitle && <p className="page-subtitle">{subtitle}</p>}
      </div>
      {action}
    </div>
  );
}

/* ── EmptyState ── */
export function EmptyState({ icon: Icon, title, subtitle }) {
  return (
    <div className="empty-state">
      {Icon && <div className="empty-state-icon"><Icon size={40} /></div>}
      <div className="empty-state-title">{title}</div>
      {subtitle && <div className="empty-state-subtitle">{subtitle}</div>}
    </div>
  );
}

/* ── Alert ── */
export function Alert({ type = 'error', children }) {
  return <div className={`alert alert-${type}`}>{children}</div>;
}

/* ── FormRow ── */
export function FormRow({ children }) {
  return <div className="form-row">{children}</div>;
}

/* ── FormActions ── */
export function FormActions({ children }) {
  return <div className="form-actions">{children}</div>;
}
