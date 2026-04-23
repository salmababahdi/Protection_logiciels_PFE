import React, { useState } from 'react';
import { Plus, Search, Pencil, Trash2, Monitor } from 'lucide-react';
import { logicielService } from '../../services/logicielService'; // ✅ fixed import
import { useApi, useAsyncAction } from '../../hooks/useApi';
import Modal from '../../components/ui/Modal';
import Button from '../../components/ui/Button';
import { PageHeader, StatusBadge, Spinner, EmptyState, Alert, Input, Select, Textarea, FormRow, FormActions } from '../../components/ui/Ui';

const EMPTY = { code: '', nom: '', version: '', description: '', statut: 'Actif' };

export default function LogicielsPage() {
  const { data: items, loading, error, refetch } = useApi(() => logicielService.getAll());
  const { loading: saving, execute } = useAsyncAction();
  const [search,  setSearch]  = useState('');
  const [modal,   setModal]   = useState(null);
  const [form,    setForm]    = useState(EMPTY);
  const [editId,  setEditId]  = useState(null);
  const [formErr, setFormErr] = useState('');

  const filtered = (items||[]).filter(i =>
      !search || [i.code, i.nom].some(v => v?.toLowerCase().includes(search.toLowerCase()))
  );

  const openCreate = () => { setForm(EMPTY); setEditId(null); setFormErr(''); setModal('create'); };
  const openEdit   = i  => { setForm({...i}); setEditId(i.id); setFormErr(''); setModal('edit'); };
  const closeModal = ()  => setModal(null);
  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  const handleSave = () => {
    if (!form.code || !form.nom) { setFormErr('Code et Nom sont obligatoires.'); return; }
    execute(
        () => editId ? logicielService.update(editId, form) : logicielService.create(form),
        () => { refetch(); closeModal(); }
    );
  };

  const handleDelete = id => {
    if (!window.confirm('Supprimer ce logiciel ?')) return;
    execute(() => logicielService.delete(id), () => refetch());
  };

  const counts = {
    actifs:   (items||[]).filter(i => i.statut === 'Actif').length,
    inactifs: (items||[]).filter(i => i.statut === 'Inactif').length,
  };

  return (
      <div className="animate-fade">
        <PageHeader
            title="Gestion des Logiciels"
            subtitle="Gérez les logiciels disponibles dans la plateforme"
            action={<Button onClick={openCreate}><Plus size={15} /> Nouveau Logiciel</Button>}
        />

        <div className="stats-row">
          {[
            { label: 'Total Logiciels',      value: (items||[]).length },
            { label: 'Logiciels Actifs',     value: counts.actifs },
            { label: 'Logiciels Inactifs',   value: counts.inactifs },
            { label: 'Versions Disponibles', value: (items||[]).length },
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
              <input
                  className="table-search"
                  placeholder="Rechercher un logiciel..."
                  value={search}
                  onChange={e => setSearch(e.target.value)}
              />
            </div>
          </div>

          {loading ? <Spinner /> : error ? <Alert type="error">{error}</Alert> : (
              <div className="table-wrap">
                {filtered.length === 0
                    ? <EmptyState icon={Monitor} title="Aucun logiciel" subtitle="Commencez par ajouter votre premier logiciel" />
                    : (
                        <table>
                          <thead>
                          <tr>
                            <th>Code Logiciel</th><th>Nom</th><th>Version</th>
                            <th>Description</th><th>Statut</th><th>Actions</th>
                          </tr>
                          </thead>
                          <tbody>
                          {filtered.map(i => (
                              <tr key={i.id}>
                                <td>
                                  <code style={{ fontSize:12, background:'var(--gray-100)', padding:'2px 6px', borderRadius:4 }}>
                                    {i.code}
                                  </code>
                                </td>
                                <td style={{ fontWeight:500 }}>{i.nom}</td>
                                <td><span style={{ fontSize:12, color:'var(--gray-500)' }}>{i.version}</span></td>
                                <td style={{ color:'var(--gray-500)', maxWidth:240, overflow:'hidden', textOverflow:'ellipsis', whiteSpace:'nowrap' }}>
                                  {i.description}
                                </td>
                                <td><StatusBadge statut={i.statut} /></td>
                                <td>
                                  <div style={{ display:'flex', gap:4 }}>
                                    <button className="action-btn success" onClick={() => openEdit(i)}><Pencil size={14} /></button>
                                    <button className="action-btn danger"  onClick={() => handleDelete(i.id)}><Trash2 size={14} /></button>
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

        <Modal open={!!modal} onClose={closeModal} title={modal === 'edit' ? 'Modifier le Logiciel' : 'Nouveau Logiciel'}>
          {formErr && <Alert type="error">{formErr}</Alert>}
          <div className="form-fields">
            <Input    label="Code Logiciel" name="code"        placeholder="Ex: SOFT-001"         value={form.code}        onChange={handleChange} />
            <Input    label="Nom"           name="nom"         placeholder="Ex: Microsoft Office"  value={form.nom}         onChange={handleChange} />
            <Input    label="Version"       name="version"     placeholder="Ex: 2024.1.0"          value={form.version}     onChange={handleChange} />
            <Textarea label="Description"  name="description" placeholder="Description..."        value={form.description} onChange={handleChange} />
            <Select   label="Statut"       name="statut"                                           value={form.statut}      onChange={handleChange}>
              <option>Actif</option>
              <option>Inactif</option>
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