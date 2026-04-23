import React from 'react';
import { Users, Key, AlertTriangle, FileText, Clock, CreditCard } from 'lucide-react';
import {
  AreaChart, Area, XAxis, YAxis, CartesianGrid,
  Tooltip, ResponsiveContainer, PieChart, Pie, Cell,
} from 'recharts';
import { dashboardService } from '../../services/dashboardService';
import { useApi } from '../../hooks/useApi';
import { PageHeader, Spinner, Alert } from '../../components/ui/Ui';
import './Dashboard.css';

export default function DashboardPage() {
  const { data: stats, loading, error } = useApi(() => dashboardService.getStats());

  if (loading) return <Spinner />;
  if (error)   return <Alert type="error">{error}</Alert>;
  if (!stats)  return null;

  // ── KPI cards ───────────────────────────────────────────────
  const KPI = [
    { label: 'Total Clients',        value: stats.totalClients,      icon: Users,         color: '#2563EB' },
    { label: 'Licences Actives',     value: stats.licencesActives,   icon: Key,           color: '#10B981' },
    { label: 'Licences Expirées',    value: stats.licencesExpirees,  icon: AlertTriangle, color: '#EF4444' },
    { label: 'Contrats en cours',    value: stats.contratsEnCours,   icon: FileText,      color: '#2563EB' },
    { label: 'Expirant bientôt',     value: stats.expirantBientot,   icon: Clock,         color: '#F59E0B' },
    { label: 'Paiements en attente', value: stats.paiementsAttente,  icon: CreditCard,    color: '#6366F1' },
  ];

  // ── Pie chart data from real stats ──────────────────────────
  const PIE_DATA = [
    { name: 'Actives',    value: Number(stats.licencesActives),   color: '#10B981' },
    { name: 'Suspendues', value: Number(stats.licencesSuspendues),color: '#6366F1' },
    { name: 'Expirées',   value: Number(stats.licencesExpirees),  color: '#EF4444' },
  ];

  const totalLicences = PIE_DATA.reduce((s, d) => s + d.value, 0);

  return (
      <div className="animate-fade">
        <PageHeader
            title="Tableau de bord"
            subtitle="Vue d'ensemble de votre système de gestion des licences"
        />

        {/* KPI grid */}
        <div className="kpi-grid">
          {KPI.map(({ label, value, icon: Icon, color }) => (
              <div key={label} className="kpi-card">
                <div className="kpi-icon" style={{ color, background: color + '18' }}>
                  <Icon size={20} />
                </div>
                <div className="kpi-val">{Number(value).toLocaleString()}</div>
                <div className="kpi-lbl">{label}</div>
              </div>
          ))}
        </div>

        {/* Charts row */}
        <div className="charts-row">

          {/* Area chart */}
          <div className="card chart-card">
            <div className="card-header">
              <span className="card-title">📈 Évolution des activations</span>
            </div>
            <div style={{ padding: '20px 16px 12px' }}>
              <ResponsiveContainer width="100%" height={200}>
                <AreaChart data={stats.evolution}>
                  <defs>
                    <linearGradient id="grad" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%"  stopColor="#2563EB" stopOpacity={0.15} />
                      <stop offset="95%" stopColor="#2563EB" stopOpacity={0} />
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" stroke="#F3F4F6" />
                  <XAxis dataKey="mois" tick={{ fontSize: 12, fill: '#9CA3AF' }} axisLine={false} tickLine={false} />
                  <YAxis tick={{ fontSize: 12, fill: '#9CA3AF' }} axisLine={false} tickLine={false} />
                  <Tooltip
                      contentStyle={{ border: '1px solid #E5E7EB', borderRadius: 8, fontSize: 12 }}
                      labelStyle={{ fontWeight: 600 }}
                  />
                  <Area type="monotone" dataKey="activations" stroke="#2563EB" strokeWidth={2} fill="url(#grad)" />
                </AreaChart>
              </ResponsiveContainer>
            </div>
          </div>

          {/* Pie chart */}
          <div className="card chart-card">
            <div className="card-header">
              <span className="card-title">📊 Répartition des licences</span>
            </div>
            <div style={{ padding: '16px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
              <div style={{ position: 'relative', display: 'inline-flex' }}>
                <PieChart width={200} height={200}>
                  <Pie
                      data={PIE_DATA}
                      cx={100} cy={100}
                      innerRadius={60} outerRadius={85}
                      paddingAngle={3}
                      dataKey="value"
                  >
                    {PIE_DATA.map((entry, i) => <Cell key={i} fill={entry.color} />)}
                  </Pie>
                </PieChart>
                <div style={{
                  position: 'absolute', top: '50%', left: '50%',
                  transform: 'translate(-50%,-50%)', textAlign: 'center',
                }}>
                  <div style={{ fontSize: 22, fontWeight: 700, color: 'var(--gray-900)' }}>
                    {totalLicences.toLocaleString()}
                  </div>
                  <div style={{ fontSize: 11, color: 'var(--gray-500)' }}>Total</div>
                </div>
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 8, marginTop: 12, width: '100%' }}>
                {PIE_DATA.map(({ name, value, color }) => (
                    <div key={name} style={{ display: 'flex', alignItems: 'center', gap: 8, fontSize: 13 }}>
                      <div style={{ width: 10, height: 10, borderRadius: '50%', background: color, flexShrink: 0 }} />
                      <span style={{ color: 'var(--gray-700)', flex: 1 }}>{name}</span>
                      <span style={{ fontWeight: 600, color: 'var(--gray-900)' }}>{value.toLocaleString()}</span>
                    </div>
                ))}
              </div>
            </div>
          </div>
        </div>

        {/* Recent alerts */}
        <div className="card" style={{ marginTop: 20 }}>
          <div className="card-header">
            <span className="card-title">🔔 Alertes récentes</span>
          </div>
          <div style={{ padding: '8px 0' }}>
            {(stats.alerts || []).map((a, i) => (
                <div key={i} className={`dash-alert dash-alert-${a.type}`}>
                  <div className="dash-alert-dot" />
                  <span>{a.text}</span>
                </div>
            ))}
          </div>
        </div>
      </div>
  );
}