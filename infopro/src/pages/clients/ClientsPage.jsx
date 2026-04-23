import React, { useState } from 'react';
import { Plus, Search, Pencil, Trash2, Users } from 'lucide-react';
import { clientService } from '../../services/clientService';
import { useApi, useAsyncAction } from '../../hooks/useApi';
import Modal from '../../components/ui/Modal';
import Button from '../../components/ui/Button';
import { PageHeader, StatusBadge, Spinner, EmptyState, Alert, Input, Select, FormRow, FormActions } from '../../components/ui/Ui';
import '../../components/ui/Ui.css';

const EMPTY = { code: '', nom: '', email: '', telephone: '', adresse: '', ville: '', statut: 'Actif' };

export default function ClientsPage() {
  const { data: clients, loading, error, refetch } = useApi(() => clientService.getAll());
  const { loading: saving, execute } = useAsyncAction();

  const [search,   setSearch]   = useState('');
  const [filter,   setFilter]   = useState('Tous');
  const [modal,    setModal]    = useState(null);   // null | 'create' | 'edit'
  const [form,     setForm]     = useState(EMPTY);
  const [editId,   setEditId]   = useState(null);
  const [formErr,  setFormErr]  = useState('');

  const filtered = (clients || []).filter(c => {
    const matchSearch = !search || [c.code, c.nom, c.email].some(v => v?.toLowerCase().includes(search.toLowerCase()));
    const matchFilter = filter === 'Tous' || c.statut === filter;
    return matchSearch && matchFilter;
  });

  const openCreate = () => { setForm(EMPTY); setEditId(null); setFormErr(''); setModal('create'); };
  const openEdit   = c  => { setForm({ ...c }); setEditId(c.id); setFormErr(''); setModal('edit'); };
  const closeModal = ()  => setModal(null);

  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  const validate = () => {
    if (!form.code || !form.nom || !form.email) return 'Les champs Code, Nom et Email sont obligatoires.';
    return '';
  };

  const handleSave = () => {
    const err = validate();
    if (err) { setFormErr(err); return; }
    execute(
      () => editId ? clientService.update(editId, form) : clientService.create(form),
      () => { refetch(); closeModal(); }
    );
  };

  const handleDelete = id => {
    if (!window.confirm('Supprimer ce client ?')) return;
    execute(() => clientService.delete(id), () => refetch());
  };

  const counts = {
    Actif: (clients||[]).filter(c=>c.statut==='Actif').length,
    Inactif: (clients||[]).filter(c=>c.statut==='Inactif').length,
    Suspendu: (clients||[]).filter(c=>c.statut==='Suspendu').length,
  };

  return (
    <div className="animate-fade">
      <PageHeader
        title="Gestion des Clients"
        subtitle="Gérez votre portefeuille clients"
        action={<Button onClick={openCreate}><Plus size={15} /> Nouveau Client</Button>}
      />

      {/* Mini stats */}
      <div className="stats-row">
        {[
          { label: 'Total Clients',  value: (clients||[]).length },
          { label: 'Clients Actifs', value: counts.Actif },
          { label: 'Prospects',      value: 0 },
          { label: 'Inactifs',       value: counts.Inactif },
        ].map(s => (
          <div key={s.label} className="mini-stat">
            <div>
              <div className="mini-stat-val">{s.value}</div>
              <div className="mini-stat-lbl">{s.label}</div>
            </div>
          </div>
        ))}
      </div>

      <div className="card">
        {/* Toolbar */}
        <div className="table-toolbar">
          <div className="table-search-wrap">
            <Search size={14} className="table-search-icon" />
            <input className="table-search" placeholder="Rechercher un client..." value={search} onChange={e => setSearch(e.target.value)} />
          </div>
          <div className="filter-tabs">
            {['Tous','Actif','Inactif','Suspendu'].map(t => (
              <button key={t} className={`filter-tab ${filter===t?'active':''}`} onClick={()=>setFilter(t)}>{t}</button>
            ))}
          </div>
        </div>

        {/* Table */}
        {loading ? <Spinner /> : error ? <Alert type="error">{error}</Alert> : (
          <div className="table-wrap">
            {filtered.length === 0
              ? <EmptyState icon={Users} title="Aucun client" subtitle="Commencez par ajouter votre premier client" />
              : (
                <table>
                  <thead>
                    <tr>
                      <th>Code Client</th><th>Nom Complet</th><th>Email</th>
                      <th>Téléphone</th><th>Adresse</th><th>Statut</th><th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filtered.map(c => (
                      <tr key={c.id}>
                        <td><code style={{ fontSize: 12, background: 'var(--gray-100)', padding: '2px 6px', borderRadius: 4 }}>{c.code}</code></td>
                        <td style={{ fontWeight: 500 }}>{c.nom}</td>
                        <td>{c.email}</td>
                        <td>{c.telephone}</td>
                        <td>{c.ville}</td>
                        <td><StatusBadge statut={c.statut} /></td>
                        <td>
                          <div style={{ display: 'flex', gap: 4 }}>
                            <button className="action-btn success" onClick={() => openEdit(c)} title="Modifier"><Pencil size={14} /></button>
                            <button className="action-btn danger"  onClick={() => handleDelete(c.id)} title="Supprimer"><Trash2 size={14} /></button>
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

      {/* Create / Edit Modal */}
      <Modal open={!!modal} onClose={closeModal} title={modal === 'edit' ? 'Modifier le Client' : 'Nouveau Client'}>
        {formErr && <Alert type="error">{formErr}</Alert>}
        <div className="form-fields">
          <FormRow>
            <Input label="Code Client *" name="code" placeholder="CLI-001" value={form.code} onChange={handleChange} />
            <Select label="Statut *" name="statut" value={form.statut} onChange={handleChange}>
              <option>Actif</option><option>Inactif</option><option>Suspendu</option><option>Prospect</option>
            </Select>
          </FormRow>
          <Input label="Nom Complet *" name="nom" placeholder="Jean Dupont" value={form.nom} onChange={handleChange} />
          <FormRow>
            <Input label="Email *" name="email" type="email" placeholder="jean.dupont@email.com" value={form.email} onChange={handleChange} />
            <Input label="Téléphone" name="telephone" placeholder="+33 1 23 45 67 89" value={form.telephone} onChange={handleChange} />
          </FormRow>
          <Input label="Adresse *" name="adresse" placeholder="123 Rue de la Paix" value={form.adresse} onChange={handleChange} />
          <Input label="Ville *" name="ville" placeholder="Paris" value={form.ville} onChange={handleChange} />
        </div>
        <FormActions>
          <Button variant="secondary" onClick={closeModal}>Annuler</Button>
          <Button loading={saving} onClick={handleSave}>Enregistrer</Button>
        </FormActions>
      </Modal>
    </div>
  );
}
