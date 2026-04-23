import React, { useState, useRef, useEffect } from 'react';
import { Plus, Search, Copy, MoreVertical, Power, PauseCircle, CheckCircle, Clock, Trash2, Key } from 'lucide-react';
import { licenceService } from '../../services/licenceService';
import { clientService } from '../../services/clientService';
import { logicielService } from '../../services/logicielService';
import { contratService } from '../../services/contratService';
import { useApi, useAsyncAction } from '../../hooks/useApi';
import Modal from '../../components/ui/Modal';
import Button from '../../components/ui/Button';
import { PageHeader, StatusBadge, Spinner, EmptyState, Alert, Input, Select, FormRow, FormActions } from '../../components/ui/Ui';
import './Licences.css';

const EMPTY = { clientId: '', logicielId: '', contratId: '', postes: 1, dateDebut: '', dateFin: '', statut: 'actif', codeDebridage: '' };

function ActionMenu({ licence, onRefetch }) {
  const [open, setOpen] = useState(false);
  const ref = useRef();
  const { execute } = useAsyncAction();

  useEffect(() => {
    const handler = e => { if (ref.current && !ref.current.contains(e.target)) setOpen(false); };
    document.addEventListener('mousedown', handler);
    return () => document.removeEventListener('mousedown', handler);
  }, []);

  const act = (action) => {
    setOpen(false);
    if (action === 'delete') {
      if (!window.confirm('Supprimer cette licence ?')) return;
      execute(() => licenceService.delete(licence.id), onRefetch);
    } else if (action === 'activate') {
      execute(() => licenceService.activer(licence.id), onRefetch);
    } else if (action === 'suspend') {
      execute(() => licenceService.suspendre(licence.id, ''), onRefetch);
    } else if (action === 'verify') {
      // TODO: implement validity check
      alert('Fonctionnalité de vérification à implémenter');
    } else if (action === 'history') {
      // TODO: implement history view
      alert('Historique à implémenter');
    }
  };

  return (
    <div ref={ref} style={{ position:'relative', display:'inline-block' }}>
      <button className="action-btn" onClick={() => setOpen(v=>!v)}><MoreVertical size={14} /></button>
      {open && (
        <div className="action-menu animate-fade">
          <div className="action-menu-header">
            <div style={{ fontSize:11, color:'var(--gray-500)', marginBottom:2 }}>Actions Licence</div>
            <div style={{ fontSize:12, fontWeight:600, color:'var(--gray-800)', wordBreak:'break-all' }}>{licence.cle}</div>
            {licence.codeDebridage && (
              <div style={{ display:'flex', alignItems:'center', gap:6, marginTop:8, padding:'6px 8px', background:'var(--gray-50)', borderRadius:6, fontSize:12 }}>
                <code style={{ flex:1, wordBreak:'break-all', color:'var(--primary)' }}>{licence.codeDebridage}</code>
                <button onClick={()=>navigator.clipboard.writeText(licence.codeDebridage)} style={{ background:'none', border:'none', cursor:'pointer', color:'var(--gray-500)' }}>
                  <Copy size={13} />
                </button>
              </div>
            )}
          </div>
          <div className="action-menu-items">
            <button className="action-menu-item" onClick={()=>act('activate')}><Power size={14} /> Désactiver</button>
            <button className="action-menu-item warning" onClick={()=>act('suspend')}><PauseCircle size={14} /> Suspendre</button>
            <button className="action-menu-item success" onClick={()=>act('verify')}><CheckCircle size={14} /> Vérifier validité</button>
            <button className="action-menu-item info" onClick={()=>act('history')}><Clock size={14} /> Consulter historique</button>
            <div style={{ height:1, background:'var(--gray-100)', margin:'4px 0' }} />
            <button className="action-menu-item danger" onClick={()=>act('delete')}><Trash2 size={14} /> Supprimer la licence</button>
          </div>
        </div>
      )}
    </div>
  );
}

export default function LicencesPage() {
  const { data: items, loading, error, refetch } = useApi(() => licenceService.getAll());
  const { data: clients } = useApi(() => clientService.getAll());
  const { data: logiciels } = useApi(() => logicielService.getAll());
  const { data: contrats } = useApi(() => contratService.getAll());
  const { loading: saving, execute } = useAsyncAction();
  const [search, setSearch]   = useState('');
  const [modal, setModal]     = useState(false);
  const [form, setForm]       = useState(EMPTY);
  const [formErr, setFormErr] = useState('');

  const filtered = (items||[]).filter(i => !search || [i.cle, i.clientNom, i.logicielNom].some(v => v?.toLowerCase().includes(search.toLowerCase())));

  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  const handleSave = () => {
    if (!form.clientId || !form.logicielId || !form.contratId || !form.dateDebut || !form.dateFin) { setFormErr('Veuillez remplir tous les champs obligatoires.'); return; }
    const cle = `LIC-${Date.now().toString(36).toUpperCase()}`;
    execute(
      () => licenceService.create({ ...form, cle, postesUtilises: 0 }),
      () => { refetch(); setModal(false); setForm(EMPTY); setFormErr(''); }
    );
  };

  return (
    <div className="animate-fade">
      <PageHeader
        title="Gestion des Licences"
        subtitle="Administration et contrôle des licences actives"
        action={<Button onClick={()=>{ setForm(EMPTY); setFormErr(''); setModal(true); }}><Plus size={15} /> Nouvelle Licence</Button>}
      />

      <div className="stats-row">
        {[
          { label: 'Total Licences',   value: (items||[]).length },
          { label: 'Licences Actives', value: (items||[]).filter(i=>i.statut==='actif').length },
          { label: 'Expirées',         value: (items||[]).filter(i=>i.statut==='expire').length },
          { label: 'Codes Générés',    value: (items||[]).filter(i=>i.codeDebridage).length },
        ].map(s => (
          <div key={s.label} className="mini-stat">
            <div><div className="mini-stat-val">{s.value}</div><div className="mini-stat-lbl">{s.label}</div></div>
          </div>
        ))}
      </div>

      <div className="card">
        <div className="table-toolbar">
          <div className="table-search-wrap">
            <Search size={14} className="table-search-icon" />
            <input className="table-search" placeholder="Rechercher une licence..." value={search} onChange={e=>setSearch(e.target.value)} />
          </div>
          <Select style={{ maxWidth: 160, marginBottom:0 }} value="" onChange={()=>{}}>
            <option value="">Tous les statuts</option>
            <option value="actif">Actif</option><option value="expire">Expiré</option><option value="suspendu">Suspendu</option>
          </Select>
        </div>

        {loading ? <Spinner /> : error ? <Alert type="error">{error}</Alert> : (
          <div className="table-wrap">
            {filtered.length === 0
              ? <EmptyState icon={Key} title="Aucune licence" subtitle="Commencez par créer votre première licence" />
              : (
                <table>
                  <thead><tr><th>Clé Licence</th><th>Client</th><th>Logiciel</th><th>Postes</th><th>Période</th><th>Statut</th><th>Code Débridage</th><th>Actions</th></tr></thead>
                  <tbody>
                    {filtered.map(i => (
                      <tr key={i.id}>
                        <td><code style={{ fontSize:12, color:'var(--primary)' }}>{i.cle}</code></td>
                        <td>
                          <div style={{ display:'flex', alignItems:'center', gap:8 }}>
                            <div style={{ width:26, height:26, borderRadius:'50%', background:'var(--primary)', color:'#fff', display:'flex', alignItems:'center', justifyContent:'center', fontSize:11, fontWeight:700, flexShrink:0 }}>
                              {(i.clientNom||'').slice(0,2).toUpperCase()}
                            </div>
                            <span style={{ fontWeight:500 }}>{i.clientNom}</span>
                          </div>
                        </td>
                        <td>{i.logicielNom}</td>
                        <td>{i.postesUtilises} / {i.postes}</td>
                        <td style={{ fontSize:12 }}>
                          <div>{i.dateDebut}</div>
                          <div style={{ color:'var(--gray-500)' }}>{i.dateFin}</div>
                        </td>
                        <td><StatusBadge statut={i.statut} /></td>
                        <td>
                          {i.codeDebridage
                            ? <code style={{ fontSize:11, color:'var(--gray-600)' }}>{i.codeDebridage.slice(0,12)}…</code>
                            : <span style={{ color:'var(--gray-400)', fontSize:12 }}>—</span>}
                        </td>
                        <td><ActionMenu licence={i} onRefetch={refetch} /></td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
          </div>
        )}
      </div>

      <Modal open={modal} onClose={()=>setModal(false)} title="Nouvelle Licence">
        {formErr && <Alert type="error">{formErr}</Alert>}
        <div className="form-fields">
          <FormRow>
            <Select label="Client *" name="clientId" value={form.clientId} onChange={handleChange}>
              <option value="">— Sélectionner un client —</option>
              {(clients||[]).map(c => <option key={c.id} value={c.id}>{c.nom}</option>)}
            </Select>
            <Select label="Logiciel *" name="logicielId" value={form.logicielId} onChange={handleChange}>
              <option value="">— Sélectionner un logiciel —</option>
              {(logiciels||[]).map(l => <option key={l.id} value={l.id}>{l.nom}</option>)}
            </Select>
          </FormRow>
          <Select label="Contrat d'assistance *" name="contratId" value={form.contratId} onChange={handleChange}>
            <option value="">— Sélectionner un contrat —</option>
            {(contrats||[]).map(c => <option key={c.id} value={c.id}>{c.numero} – {c.clientNom}</option>)}
          </Select>
          <Input label="Nombre de postes autorisés *" name="postes" type="number" min="1" value={form.postes} onChange={handleChange} />
          <FormRow>
            <Input label="Date début *" name="dateDebut" type="date" value={form.dateDebut} onChange={handleChange} />
            <Input label="Date fin *"   name="dateFin"   type="date" value={form.dateFin}   onChange={handleChange} />
          </FormRow>
          <Select label="Statut initial" name="statut" value={form.statut} onChange={handleChange}>
            <option value="actif">Actif</option>
            <option value="suspendu">Suspendu</option>
          </Select>
          <div style={{ background:'var(--primary-light)', borderRadius:6, padding:'10px 12px', fontSize:13, color:'var(--primary)', display:'flex', gap:8 }}>
            <Key size={14} style={{ marginTop:1, flexShrink:0 }} />
            <span>Le code de débridage peut être généré ultérieurement via le bouton "Générer Code" dans les actions.</span>
          </div>
        </div>
        <FormActions>
          <Button variant="secondary" onClick={()=>setModal(false)}>Annuler</Button>
          <Button loading={saving} onClick={handleSave}>Créer la licence</Button>
        </FormActions>
      </Modal>
    </div>
  );
}
