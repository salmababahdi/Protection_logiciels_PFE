import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Shield, User, Mail, Lock, Eye, EyeOff } from 'lucide-react';
import { authService } from '../../services/authService';
import Button from '../../components/ui/Button';
import { Input, Alert } from '../../components/ui/Ui';
import './Auth.css';

export default function RegisterPage() {
  const navigate = useNavigate();
  const [form, setForm]       = useState({ name: '', email: '', password: '', confirm: '', agree: false });
  const [showPwd, setShowPwd] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error,   setError]   = useState('');

  const handleChange = e => {
    const { name, value, type, checked } = e.target;
    setForm(f => ({ ...f, [name]: type === 'checkbox' ? checked : value }));
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError('');
    if (!form.name || !form.email || !form.password) { setError('Veuillez remplir tous les champs obligatoires.'); return; }
    if (form.password !== form.confirm) { setError('Les mots de passe ne correspondent pas.'); return; }
    if (!form.agree) { setError("Veuillez accepter les conditions d'utilisation."); return; }
    setLoading(true);
    try {
      await authService.register({ name: form.name, email: form.email, password: form.password });
      navigate('/dashboard');
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-brand">
        <div className="auth-brand-logo"><Shield size={34} /></div>
        <div className="auth-brand-name">INFOPRO</div>
        <div className="auth-brand-sub">License Manager</div>
        <div className="auth-brand-desc">Gérez vos licences logicielles en toute sécurité et conformité</div>
        <div className="auth-brand-dots">
          <div className="auth-brand-dot" /><div className="auth-brand-dot active" /><div className="auth-brand-dot" />
        </div>
      </div>

      <div className="auth-form-panel">
        <div className="auth-form-box animate-fade">
          <h1 className="auth-form-title">Créer votre compte</h1>
          <p style={{ fontSize: 13, color: 'var(--gray-500)', marginBottom: 4 }}>Rejoignez notre plateforme en quelques étapes</p>

          {error && <Alert type="error">{error}</Alert>}

          <form onSubmit={handleSubmit}>
            <div className="auth-form-fields">
              <div className="form-row">
                <Input label="Nom complet *" name="name" placeholder="Votre nom complet" value={form.name} onChange={handleChange} icon={User} />
                <Input label="Adresse email *" name="email" type="email" placeholder="votre@email.com" value={form.email} onChange={handleChange} icon={Mail} />
              </div>

              <div className="field">
                <label className="field-label">Mot de passe *</label>
                <div style={{ position: 'relative' }}>
                  <Lock size={15} style={{ position:'absolute', left:11, top:'50%', transform:'translateY(-50%)', color:'var(--gray-400)' }} />
                  <input name="password" type={showPwd ? 'text' : 'password'} placeholder="••••••••"
                    value={form.password} onChange={handleChange}
                    className="field-input field-input-icon" style={{ paddingRight: 38 }} />
                  <button type="button" onClick={() => setShowPwd(v => !v)}
                    style={{ position:'absolute', right:10, top:'50%', transform:'translateY(-50%)', background:'none', border:'none', cursor:'pointer', color:'var(--gray-400)' }}>
                    {showPwd ? <EyeOff size={16} /> : <Eye size={16} />}
                  </button>
                </div>
              </div>

              <div className="field">
                <label className="field-label">Confirmer le mot de passe</label>
                <div style={{ position: 'relative' }}>
                  <Lock size={15} style={{ position:'absolute', left:11, top:'50%', transform:'translateY(-50%)', color:'var(--gray-400)' }} />
                  <input name="confirm" type="password" placeholder="••••••••"
                    value={form.confirm} onChange={handleChange}
                    className="field-input field-input-icon" />
                </div>
              </div>

              <label className="auth-remember" style={{ fontSize: 13 }}>
                <input type="checkbox" name="agree" checked={form.agree} onChange={handleChange} />
                J'accepte les <span className="auth-link" style={{ margin: '0 3px' }}>conditions d'utilisation</span> et la <span className="auth-link" style={{ marginLeft: 3 }}>politique de confidentialité</span>
              </label>
            </div>

            <Button type="submit" loading={loading} className="btn-block">Créer mon compte</Button>
          </form>

          <div className="auth-ssl"><Lock size={12} /> Inscription sécurisée avec chiffrement SSL</div>
          <div className="auth-divider">Vous avez déjà un compte ?</div>
          <Button variant="outline" className="btn-block" onClick={() => navigate('/login')}>Se connecter</Button>
        </div>
        <div className="auth-copyright">© 2025 INFOPRO · Tous droits réservés</div>
      </div>
    </div>
  );
}
