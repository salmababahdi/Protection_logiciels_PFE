import { MOCK_LOGICIELS, MOCK_LICENCES, MOCK_CONTRATS, MOCK_NOTIFICATIONS } from '../constants/mockData';

const delay = (ms = 500) => new Promise(r => setTimeout(r, ms));
const makeService = (initial) => {
  let items = [...initial];
  let nextId = items.length + 1;
  return {
    async getAll()        { await delay(); return items; },
    async getById(id)     { await delay(300); return items.find(i => i.id === +id); },
    async create(data)    { await delay(); const n = { id: nextId++, ...data }; items.push(n); return n; },
    async update(id, data){ await delay(); items = items.map(i => i.id === +id ? { ...i, ...data } : i); return items.find(i => i.id === +id); },
    async delete(id)      { await delay(300); items = items.filter(i => i.id !== +id); return { success: true }; },
  };
};

export const logicielService     = makeService(MOCK_LOGICIELS);
export const licenceService      = makeService(MOCK_LICENCES);
export const contratService      = makeService(MOCK_CONTRATS);
export const notificationService = makeService(MOCK_NOTIFICATIONS);