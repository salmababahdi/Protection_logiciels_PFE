import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Shield, Mail, Lock, Eye, EyeOff } from 'lucide-react';
import { authService } from '../../services/authService';
import Button from '../../components/ui/Button';
import { Input, Alert } from '../../components/ui/Ui';
import './Auth.css';

export default function LoginPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', password: '' });
  const [showPwd, setShowPwd] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  const handleSubmit = async e => {
    e.preventDefault();
    setError('');
    if (!form.email || !form.password) { setError('Veuillez remplir tous les champs.'); return; }
    setLoading(true);
    try {
      await authService.login(form.email, form.password);
      navigate('/dashboard');
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      {/* Brand panel */}
      <div className="auth-brand">
        <div className="auth-brand-logo"><Shield size={34} /></div>
        <div className="auth-brand-name">INFOPRO</div>
        <div className="auth-brand-sub">License Manager</div>
        <div className="auth-brand-desc">Gérez vos licences logicielles en toute sécurité et conformité</div>
        <div className="auth-brand-dots">
          <div className="auth-brand-dot active" />
          <div className="auth-brand-dot" />
          <div className="auth-brand-dot" />
        </div>
      </div>

      {/* Form panel */}
      <div className="auth-form-panel">
        <div className="auth-form-box animate-fade">
          <h1 className="auth-form-title">Connexion à l'espace administrateur</h1>

          {error && <Alert type="error">{error}</Alert>}

          <form onSubmit={handleSubmit}>
            <div className="auth-form-fields">
              <Input
                label="Adresse email"
                name="email"
                type="email"
                placeholder="admin@infopro.com"
                value={form.email}
                onChange={handleChange}
                icon={Mail}
              />
              <div className="field">
                <label className="field-label">Mot de passe</label>
                <div className="field-input-wrap" style={{ position: 'relative' }}>
                  <Lock size={15} className="field-icon" style={{ position: 'absolute', left: 11, top: '50%', transform: 'translateY(-50%)', color: 'var(--gray-400)' }} />
                  <input
                    name="password"
                    type={showPwd ? 'text' : 'password'}
                    placeholder="••••••••"
                    value={form.password}
                    onChange={handleChange}
                    className="field-input field-input-icon"
                    style={{ paddingRight: 38 }}
                  />
                  <button
                    type="button"
                    onClick={() => setShowPwd(v => !v)}
                    style={{ position: 'absolute', right: 10, top: '50%', transform: 'translateY(-50%)', background: 'none', border: 'none', cursor: 'pointer', color: 'var(--gray-400)' }}
                  >
                    {showPwd ? <EyeOff size={16} /> : <Eye size={16} />}
                  </button>
                </div>
              </div>
            </div>

            <div className="auth-extras">
              <label className="auth-remember">
                <input type="checkbox" /> Se souvenir de moi
              </label>
              <Link to="/forgot-password" className="auth-link">Mot de passe oublié ?</Link>
            </div>

            <Button type="submit" loading={loading} className="btn-block" style={{ marginTop: 18 }}>
              Se connecter
            </Button>
          </form>

          <div className="auth-ssl">
            <Lock size={12} /> Connexion sécurisée avec chiffrement SSL
          </div>

          <div className="auth-divider">Vous n'avez pas encore de compte ?</div>

          <Button variant="outline" className="btn-block" onClick={() => navigate('/register')}>
            Créer un compte
          </Button>
        </div>

        <div className="auth-copyright">© 2025 INFOPRO · Tous droits réservés</div>
      </div>
    </div>
  );
}
