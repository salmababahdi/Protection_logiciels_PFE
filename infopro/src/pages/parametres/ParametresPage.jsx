import React, { useState, useEffect } from 'react';
import { User, Shield, Settings, Bell, Camera, Save } from 'lucide-react';
import { parametresService } from '../../services/parametresService';
import Button from '../../components/ui/Button';
import { PageHeader, Input, Select, Alert, Spinner } from '../../components/ui/Ui';
import './Parametres.css';

export default function ParametresPage() {

  // ── State ──────────────────────────────────────────────────
  const [loadingProfile, setLoadingProfile] = useState(true);
  const [profile,   setProfile]   = useState({ name: '', email: '' });
  const [security,  setSecurity]  = useState({ current: '', newPwd: '', confirm: '' });
  const [system,    setSystem]    = useState({ company: 'INFO PRO Solutions', supportEmail: 'support@infpro.fr', country: 'France', language: 'fr' });
  const [notifs,    setNotifs]    = useState({ email: true, system: true, licences: true, clients: false });

  const [profileMsg,  setProfileMsg]  = useState({ type: '', text: '' });
  const [passwordMsg, setPasswordMsg] = useState({ type: '', text: '' });
  const [systemMsg,   setSystemMsg]   = useState({ type: '', text: '' });

  const [savingProfile,  setSavingProfile]  = useState(false);
  const [savingPassword, setSavingPassword] = useState(false);

  // ── Load profile on mount ──────────────────────────────────
  useEffect(() => {
    parametresService.getProfile()
        .then(data => {
          setProfile({ name: data.name, email: data.email });
        })
        .catch(() => {
          setProfileMsg({ type: 'error', text: 'Impossible de charger le profil.' });
        })
        .finally(() => setLoadingProfile(false));
  }, []);

  // ── Save profile ───────────────────────────────────────────
  const handleSaveProfile = async () => {
    if (!profile.name || !profile.email) {
      setProfileMsg({ type: 'error', text: 'Nom et email sont obligatoires.' });
      return;
    }
    setSavingProfile(true);
    setProfileMsg({ type: '', text: '' });
    try {
      await parametresService.updateProfile(profile);
      setProfileMsg({ type: 'success', text: 'Profil mis à jour avec succès.' });
    } catch (err) {
      setProfileMsg({ type: 'error', text: err.message || 'Erreur lors de la mise à jour.' });
    } finally {
      setSavingProfile(false);
      setTimeout(() => setProfileMsg({ type: '', text: '' }), 3000);
    }
  };

  // ── Change password ────────────────────────────────────────
  const handleChangePassword = async () => {
    if (!security.current || !security.newPwd || !security.confirm) {
      setPasswordMsg({ type: 'error', text: 'Veuillez remplir tous les champs.' });
      return;
    }
    if (security.newPwd !== security.confirm) {
      setPasswordMsg({ type: 'error', text: 'Les mots de passe ne correspondent pas.' });
      return;
    }
    setSavingPassword(true);
    setPasswordMsg({ type: '', text: '' });
    try {
      await parametresService.changePassword({
        currentPassword:  security.current,
        newPassword:      security.newPwd,
        confirmPassword:  security.confirm,
      });
      setPasswordMsg({ type: 'success', text: 'Mot de passe modifié avec succès.' });
      setSecurity({ current: '', newPwd: '', confirm: '' });
    } catch (err) {
      setPasswordMsg({ type: 'error', text: err.response?.data?.message || err.message || 'Erreur.' });
    } finally {
      setSavingPassword(false);
      setTimeout(() => setPasswordMsg({ type: '', text: '' }), 3000);
    }
  };

  // ── Save system settings (local only for now) ──────────────
  const handleSaveSystem = () => {
    setSystemMsg({ type: 'success', text: 'Paramètres système enregistrés.' });
    setTimeout(() => setSystemMsg({ type: '', text: '' }), 3000);
  };

  const toggle = key => setNotifs(n => ({ ...n, [key]: !n[key] }));

  if (loadingProfile) return <Spinner />;

  return (
      <div className="animate-fade">
        <PageHeader
            title="Paramètres"
            subtitle="Gérez la configuration de votre système"
        />

        <div className="params-grid">

          {/* ── Profile ──────────────────────────────────────── */}
          <div className="card params-card">
            <div className="card-header">
              <span className="card-title"><User size={15} /> Profil utilisateur</span>
            </div>
            <div className="params-body">
              {profileMsg.text && <Alert type={profileMsg.type}>{profileMsg.text}</Alert>}
              <div className="params-avatar-row">
                <div className="params-avatar">
                  {(profile.name || 'JP').slice(0, 2).toUpperCase()}
                </div>
                <button className="params-avatar-btn"><Camera size={14} /> Modifier la photo</button>
              </div>
              <div className="form-fields">
                <Input
                    label="Nom complet"
                    value={profile.name}
                    onChange={e => setProfile(p => ({ ...p, name: e.target.value }))}
                />
                <Input
                    label="Email"
                    type="email"
                    value={profile.email}
                    onChange={e => setProfile(p => ({ ...p, email: e.target.value }))}
                />
              </div>
              <div style={{ marginTop: 16 }}>
                <Button loading={savingProfile} onClick={handleSaveProfile}>
                  <Save size={14} /> Enregistrer le profil
                </Button>
              </div>
            </div>
          </div>

          {/* ── Security ─────────────────────────────────────── */}
          <div className="card params-card">
            <div className="card-header">
              <span className="card-title"><Shield size={15} /> Sécurité</span>
            </div>
            <div className="params-body">
              {passwordMsg.text && <Alert type={passwordMsg.type}>{passwordMsg.text}</Alert>}
              <div className="form-fields">
                <Input
                    label="Mot de passe actuel"
                    type="password"
                    placeholder="••••••••"
                    value={security.current}
                    onChange={e => setSecurity(s => ({ ...s, current: e.target.value }))}
                />
                <Input
                    label="Nouveau mot de passe"
                    type="password"
                    placeholder="••••••••"
                    value={security.newPwd}
                    onChange={e => setSecurity(s => ({ ...s, newPwd: e.target.value }))}
                />
                <Input
                    label="Confirmer le mot de passe"
                    type="password"
                    placeholder="••••••••"
                    value={security.confirm}
                    onChange={e => setSecurity(s => ({ ...s, confirm: e.target.value }))}
                />
                <Button variant="outline" loading={savingPassword} onClick={handleChangePassword}>
                  <Shield size={14} /> Changer le mot de passe
                </Button>
              </div>
            </div>
          </div>

          {/* ── System settings (local only) ─────────────────── */}
          <div className="card params-card">
            <div className="card-header">
              <span className="card-title"><Settings size={15} /> Paramètres système</span>
            </div>
            <div className="params-body">
              {systemMsg.text && <Alert type={systemMsg.type}>{systemMsg.text}</Alert>}
              <div className="form-fields">
                <Input
                    label="Nom de l'entreprise"
                    value={system.company}
                    onChange={e => setSystem(s => ({ ...s, company: e.target.value }))}
                />
                <Input
                    label="Email support"
                    type="email"
                    value={system.supportEmail}
                    onChange={e => setSystem(s => ({ ...s, supportEmail: e.target.value }))}
                />
                <Select
                    label="Fuseau horaire"
                    value={system.country}
                    onChange={e => setSystem(s => ({ ...s, country: e.target.value }))}
                >
                  <option value="France">Europe/UTC+1</option>
                  <option value="Maroc">Africa/Casablanca</option>
                  <option value="USA">America/New_York</option>
                </Select>
                <Select
                    label="Langue"
                    value={system.language}
                    onChange={e => setSystem(s => ({ ...s, language: e.target.value }))}
                >
                  <option value="fr">🇫🇷 Français</option>
                  <option value="en">🇬🇧 English</option>
                  <option value="ar">🇲🇦 العربية</option>
                </Select>
              </div>
              <div style={{ marginTop: 16 }}>
                <Button variant="outline" onClick={handleSaveSystem}>
                  <Save size={14} /> Enregistrer les paramètres
                </Button>
              </div>
            </div>
          </div>

          {/* ── Notifications (local only) ────────────────────── */}
          <div className="card params-card">
            <div className="card-header">
              <span className="card-title"><Bell size={15} /> Notifications</span>
            </div>
            <div className="params-body">
              {[
                { key: 'email',    label: 'Notifications email',       sub: 'Recevoir des alertes par email' },
                { key: 'system',   label: 'Notifications système',     sub: "Alertes dans l'application" },
                { key: 'licences', label: 'Alertes licences expirées', sub: 'Notification avant expiration' },
                { key: 'clients',  label: 'Alertes nouveaux clients',  sub: "Notification à l'ajout d'un client" },
              ].map(({ key, label, sub }) => (
                  <div key={key} className="notif-row">
                    <div>
                      <div className="notif-label">{label}</div>
                      <div className="notif-sub">{sub}</div>
                    </div>
                    <button
                        className={`toggle ${notifs[key] ? 'toggle-on' : ''}`}
                        onClick={() => toggle(key)}
                    />
                  </div>
              ))}
            </div>
          </div>

        </div>
      </div>
  );
}