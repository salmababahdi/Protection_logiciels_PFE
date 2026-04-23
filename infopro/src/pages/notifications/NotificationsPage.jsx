import React, { useState } from 'react';
import { Bell, AlertTriangle, MessageCircle, CheckCheck, Eye, Trash2 } from 'lucide-react';
import { notificationService } from '../../services/notificationService';
import { useApi, useAsyncAction } from '../../hooks/useApi';
import Button from '../../components/ui/Button';
import { PageHeader, StatusBadge, Spinner, EmptyState, Alert } from '../../components/ui/Ui';
import './Notifications.css';

const TYPE_ICON = { alerte: AlertTriangle, message: MessageCircle };
const TYPE_COLOR = { alerte: '#F59E0B', message: '#2563EB' };

export default function NotificationsPage() {
  const { data: items, loading, error, refetch } = useApi(() => notificationService.getAll());
  const { execute } = useAsyncAction();
  const [filter, setFilter] = useState('Tous');

  const filtered = (items||[]).filter(i => {
    if (filter === 'Non lues') return i.statut === 'Non lu';
    if (filter === 'Alertes')  return i.type === 'alerte';
    if (filter === 'Messages') return i.type === 'message';
    return true;
  });

  const markRead = id => execute(() => notificationService.update(id, { statut: 'Lu' }), refetch);
  const remove   = id => execute(() => notificationService.delete(id), refetch);
  const markAll  = ()  => {
    const unread = (items||[]).filter(i => i.statut === 'Non lu');
    Promise.all(unread.map(i => notificationService.update(i.id, { statut: 'Lu' }))).then(refetch);
  };

  const counts = {
    total: (items||[]).length,
    nonLues: (items||[]).filter(i=>i.statut==='Non lu').length,
    alertes: (items||[]).filter(i=>i.type==='alerte').length,
    messages: (items||[]).filter(i=>i.type==='message').length,
  };

  return (
    <div className="animate-fade">
      <PageHeader
        title="Notifications"
        subtitle="Gérez et consultez toutes vos notifications"
        action={<Button variant="outline" onClick={markAll}><CheckCheck size={15} /> Marquer tout comme lu</Button>}
      />

      {/* Summary cards */}
      <div className="stats-row">
        {[
          { label: 'Total Notifications', value: counts.total,    icon: Bell },
          { label: 'Non lues',            value: counts.nonLues,  icon: Bell },
          { label: 'Alertes',             value: counts.alertes,  icon: AlertTriangle },
          { label: 'Messages',            value: counts.messages, icon: MessageCircle },
        ].map(s => (
          <div key={s.label} className="mini-stat">
            <s.icon size={18} className="mini-stat-icon" />
            <div><div className="mini-stat-val">{s.value}</div><div className="mini-stat-lbl">{s.label}</div></div>
          </div>
        ))}
      </div>

      <div className="card">
        <div className="table-toolbar">
          <div className="filter-tabs">
            {['Tous','Non lues','Alertes','Messages'].map(t => (
              <button key={t} className={`filter-tab ${filter===t?'active':''}`} onClick={()=>setFilter(t)}>{t}</button>
            ))}
          </div>
        </div>

        {loading ? <Spinner /> : error ? <Alert type="error">{error}</Alert> : (
          <>
            <div style={{ padding:'10px 16px 6px', fontSize:13, fontWeight:600, color:'var(--gray-700)' }}>
              Liste des Notifications
            </div>
            {filtered.length === 0
              ? <EmptyState icon={Bell} title="Aucune notification" subtitle="Vous êtes à jour !" />
              : (
                <div className="table-wrap">
                  <table>
                    <thead><tr><th>Type</th><th>Titre</th><th>Message</th><th>Date</th><th>Statut</th><th>Actions</th></tr></thead>
                    <tbody>
                      {filtered.map(i => {
                        const Icon = TYPE_ICON[i.type] || Bell;
                        const color = TYPE_COLOR[i.type] || '#6B7280';
                        return (
                          <tr key={i.id} style={{ opacity: i.statut==='Lu' ? .7 : 1 }}>
                            <td>
                              <div style={{ width:30, height:30, borderRadius:6, background: color+'18', display:'flex', alignItems:'center', justifyContent:'center', color }}>
                                <Icon size={15} />
                              </div>
                            </td>
                            <td style={{ fontWeight: i.statut==='Non lu' ? 600 : 400 }}>{i.titre}</td>
                            <td style={{ color:'var(--gray-500)', maxWidth:280, overflow:'hidden', textOverflow:'ellipsis', whiteSpace:'nowrap' }}>{i.message}</td>
                            <td style={{ fontSize:12, color:'var(--gray-500)', whiteSpace:'nowrap' }}>{i.date}</td>
                            <td><StatusBadge statut={i.statut} /></td>
                            <td>
                              <div style={{ display:'flex', gap:4 }}>
                                {i.statut === 'Non lu' && (
                                  <button className="action-btn success" onClick={()=>markRead(i.id)} title="Marquer lu"><Eye size={14} /></button>
                                )}
                                <button className="action-btn danger" onClick={()=>remove(i.id)} title="Supprimer"><Trash2 size={14} /></button>
                              </div>
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              )}
          </>
        )}
      </div>
    </div>
  );
}
