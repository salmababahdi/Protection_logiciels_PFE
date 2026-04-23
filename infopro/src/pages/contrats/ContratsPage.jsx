import React, { useState } from 'react';
import { Plus, Search, Pencil, Trash2, FileText } from 'lucide-react';
import { contratService } from '../../services/contratService';
import { clientService } from '../../services/clientService';
import { useApi, useAsyncAction } from '../../hooks/useApi';
import Modal from '../../components/ui/Modal';
import Button from '../../components/ui/Button';
import { PageHeader, StatusBadge, Spinner, EmptyState, Alert, Input, Select, FormRow, FormActions } from '../../components/ui/Ui';

const EMPTY = { numero: '', clientId: '', dateDebut: '', dateFin: '', montant: '', statut: 'actif' };

export default function ContratsPage() {
  const { data: items, loading, error, refetch } = useApi(() => contratService.getAll());
  const { data: clients } = useApi(() => clientService.getAll());
  const { loading: saving, execute } = useAsyncAction();
  const [search, setSearch] = useState('');
  const [filter, setFilter] = useState('Tous');
  const [modal, setModal]   = useState(null);
  const [form, setForm]     = useState(EMPTY);
  const [editId, setEditId] = useState(null);
  const [formErr, setFormErr] = useState('');

  const filtered = (items||[]).filter(i => {
    const ms = !search || [i.numero, i.clientNom].some(v=>v?.toLowerCase().includes(search.toLowerCase()));
    const mf = filter === 'Tous' || i.statut === filter;
    return ms && mf;
  });

  const openCreate = () => { setForm(EMPTY); setEditId(null); setFormErr(''); setModal('create'); };
  const openEdit   = i  => { setForm({...i, clientId: i.clientId}); setEditId(i.id); setFormErr(''); setModal('edit'); };
  const closeModal = ()  => setModal(null);
  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  const handleSave = () => {
    if (!form.numero || !form.clientId || !form.dateDebut || !form.dateFin) { setFormErr('Numéro, dates et client sont obligatoires.'); return; }
    execute(
      () => editId ? contratService.update(editId, form) : contratService.create(form),
      () => { refetch(); closeModal(); }
    );
  };

  const handleDelete = id => {
    if (!window.confirm('Supprimer ce contrat ?')) return;
    execute(() => contratService.delete(id), () => refetch());
  };

  return (
    <div className="animate-fade">
      <PageHeader
        title="Contrats d'assistance"
        subtitle="Gérez les contrats d'assistance et leurs échéances"
        action={<Button onClick={openCreate}><Plus size={15} /> Nouveau Contrat</Button>}
      />

      <div className="stats-row">
        {[
          { label: 'Total Contrats',     value: (items||[]).length },
          { label: 'Contrats Actifs',    value: (items||[]).filter(i=>i.statut==='actif').length },
          { label: 'Contrats Expirés',   value: (items||[]).filter(i=>i.statut==='expire').length },
          { label: 'Contrats Suspendus', value: (items||[]).filter(i=>i.statut==='suspendu').length },
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
            <input className="table-search" placeholder="Rechercher un contrat..." value={search} onChange={e=>setSearch(e.target.value)} />
          </div>
          <div className="filter-tabs">
            {['Tous','actif','expire','suspendu'].map(t=>(
              <button key={t} className={`filter-tab ${filter===t?'active':''}`} onClick={()=>setFilter(t)}>{t==='actif'?'Actifs':t==='expire'?'Expirés':t==='suspendu'?'Suspendus':t}</button>
            ))}
          </div>
        </div>

        {loading ? <Spinner /> : error ? <Alert type="error">{error}</Alert> : (
          <div className="table-wrap">
            {filtered.length === 0
              ? <EmptyState icon={FileText} title="Aucun contrat" subtitle="Commencez par créer votre premier contrat" />
              : (
                <table>
                  <thead><tr><th>Numéro Contrat</th><th>Nom Client</th><th>Date début</th><th>Date fin</th><th>Montant</th><th>Statut</th><th>Actions</th></tr></thead>
                  <tbody>
                    {filtered.map(i => (
                      <tr key={i.id}>
                        <td style={{ fontWeight:500 }}>{i.numero}</td>
                        <td>
                          <div style={{ display:'flex', alignItems:'center', gap:8 }}>
                            <div style={{ width:26, height:26, borderRadius:'50%', background:'var(--primary)', color:'#fff', display:'flex', alignItems:'center', justifyContent:'center', fontSize:11, fontWeight:700, flexShrink:0 }}>
                              {(i.clientNom||'').slice(0,2).toUpperCase()}
                            </div>
                            <span style={{ fontWeight:500 }}>{i.clientNom}</span>
                          </div>
                        </td>
                        <td style={{ fontSize:13 }}>{i.dateDebut}</td>
                        <td style={{ fontSize:13 }}>{i.dateFin}</td>
                        <td style={{ fontSize:13, fontWeight:500 }}>{i.montant ? `${i.montant} €` : '—'}</td>
                        <td><StatusBadge statut={i.statut} /></td>
                        <td>
                          <div style={{ display:'flex', gap:4 }}>
                            <button className="action-btn success" onClick={()=>openEdit(i)}><Pencil size={14} /></button>
                            <button className="action-btn danger"  onClick={()=>handleDelete(i.id)}><Trash2 size={14} /></button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
          </div>
        )}
      </div>

      <Modal open={!!modal} onClose={closeModal} title={modal==='edit' ? 'Modifier le Contrat' : 'Nouveau Contrat'}>
        {formErr && <Alert type="error">{formErr}</Alert>}
        <div className="form-fields">
          <Input label="Numéro Contrat *" name="numero" placeholder="CONT-2026-001" value={form.numero} onChange={handleChange} />
          <Select label="Client *" name="clientId" value={form.clientId} onChange={handleChange}>
            <option value="">— Sélectionner un client —</option>
            {(clients||[]).map(c => <option key={c.id} value={c.id}>{c.nom}</option>)}
          </Select>
          <FormRow>
            <Input label="Date début *" name="dateDebut" type="date" value={form.dateDebut} onChange={handleChange} />
            <Input label="Date fin *"   name="dateFin"   type="date" value={form.dateFin}   onChange={handleChange} />
          </FormRow>
          <Input label="Montant (€)" name="montant" type="number" step="0.01" placeholder="0.00" value={form.montant} onChange={handleChange} />
          <Select label="Statut *" name="statut" value={form.statut} onChange={handleChange}>
            <option value="actif">Actif</option>
            <option value="expire">Expiré</option>
            <option value="suspendu">Suspendu</option>
          </Select>
        </div>
        <FormActions>
          <Button variant="secondary" onClick={closeModal}>Annuler</Button>
          <Button loading={saving} onClick={handleSave}>Enregistrer</Button>
        </FormActions>
      </Modal>
    </div>
  );
}
