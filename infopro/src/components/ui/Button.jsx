import React from 'react';
import './Button.css';

export default function Button({
  children, variant = 'primary', size = 'md',
  loading = false, disabled, className = '', ...props
}) {
  return (
    <button
      className={`btn btn-${variant} btn-${size} ${loading ? 'btn-loading' : ''} ${className}`}
      disabled={disabled || loading}
      {...props}
    >
      {loading && <span className="btn-spinner" />}
      {children}
    </button>
  );
}
