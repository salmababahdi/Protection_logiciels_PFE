# INFOPRO License Manager — React Frontend

A production-ready React frontend replicating the INFOPRO License Manager UI, prepared for Spring Boot / REST API integration.

## 🚀 Quick Start

```bash
# 1. Install dependencies
npm install

# 2. Start the dev server
npm start
# → Opens at http://localhost:3000

# Default login credentials:
# Email:    admin@infopro.com
# Password: password
```

## 🗂️ Project Structure

```
src/
├── App.jsx                          # Router root
├── index.js / index.css             # Entry + global CSS (design tokens)
│
├── routes/
│   └── PrivateRoute.jsx             # JWT-protected route guard
│
├── layouts/
│   ├── DashboardLayout.jsx/.css     # Shell: sidebar + navbar + <Outlet>
│   ├── Sidebar.jsx/.css             # Navigation sidebar
│   └── Navbar.jsx/.css             # Top navbar (search, user, bell)
│
├── services/
│   ├── api.js                       # Axios instance + JWT interceptors
│   ├── authService.js               # login / register / logout (mock → real)
│   └── dataServices.js             # clients / logiciels / licences / contrats / notifications
│
├── hooks/
│   ├── useApi.js                    # useApi(fn) – fetch with loading/error state
│   └── useAsyncAction.js            # useAsyncAction() – mutation with loading/error
│
├── constants/
│   └── mockData.js                  # All mock data (replace with API calls)
│
├── components/
│   └── ui/
│       ├── Button.jsx/.css          # Primary / secondary / outline / danger / ghost
│       ├── Modal.jsx/.css           # Overlay modal with backdrop
│       └── Ui.jsx/.css             # Badge, StatusBadge, Input, Select, Textarea,
│                                   # StatCard, PageHeader, EmptyState, Alert,
│                                   # FormRow, FormActions + shared table/card CSS
│
└── pages/
    ├── auth/
    │   ├── LoginPage.jsx            # Split layout login form
    │   ├── RegisterPage.jsx         # Split layout register form
    │   └── Auth.css                 # Shared auth styles
    ├── dashboard/
    │   ├── DashboardPage.jsx        # KPI grid + AreaChart + PieChart + alerts
    │   └── Dashboard.css
    ├── clients/
    │   └── ClientsPage.jsx          # Full CRUD (list + create/edit modal + delete)
    ├── logiciels/
    │   └── LogicielsPage.jsx        # Full CRUD
    ├── licences/
    │   ├── LicencesPage.jsx         # List + create + action dropdown menu
    │   └── Licences.css
    ├── contrats/
    │   └── ContratsPage.jsx         # Full CRUD
    ├── notifications/
    │   └── NotificationsPage.jsx    # List + mark read + delete + filter tabs
    └── parametres/
        ├── ParametresPage.jsx       # Profile + Security + System + Notification toggles
        └── Parametres.css
```

## 🔌 Switching to a Real Backend

All mock logic is isolated in **`src/services/`**. To connect to your Spring Boot API:

### 1. Set the base URL
Create `.env` at the project root:
```
REACT_APP_API_URL=http://localhost:8080/api
```

### 2. Replace mock service functions

Each service file contains a commented real API call next to every mock:

```js
// src/services/clientService.js
async getAll() {
  await delay();               // ← remove this line
  // return api.get('/clients'); ← uncomment this line
  return { data: clients };    // ← remove this line
},
```

Repeat for: `authService.js`, `dataServices.js` (logiciels, licences, contrats, notifications).

### 3. Expected API endpoints

| Method | Endpoint                    | Description             |
|--------|-----------------------------|-------------------------|
| POST   | `/auth/login`               | `{ email, password }` → `{ token, user }` |
| POST   | `/auth/register`            | `{ name, email, password }` → `{ token, user }` |
| GET    | `/clients`                  | List all clients        |
| POST   | `/clients`                  | Create client           |
| PUT    | `/clients/{id}`             | Update client           |
| DELETE | `/clients/{id}`             | Delete client           |
| GET    | `/logiciels`                | List all software       |
| POST   | `/logiciels`                | Create software         |
| PUT    | `/logiciels/{id}`           | Update software         |
| DELETE | `/logiciels/{id}`           | Delete software         |
| GET    | `/licences`                 | List all licences       |
| POST   | `/licences`                 | Create licence          |
| PUT    | `/licences/{id}`            | Update / suspend        |
| DELETE | `/licences/{id}`            | Delete licence          |
| GET    | `/contrats`                 | List all contracts      |
| POST   | `/contrats`                 | Create contract         |
| PUT    | `/contrats/{id}`            | Update contract         |
| DELETE | `/contrats/{id}`            | Delete contract         |
| GET    | `/notifications`            | List notifications      |
| PUT    | `/notifications/{id}`       | Mark as read            |
| DELETE | `/notifications/{id}`       | Delete notification     |

### 4. JWT flow (already implemented)

- Login stores the token in `localStorage.getItem('token')`
- Every Axios request automatically adds `Authorization: Bearer <token>`
- Any 401 response redirects to `/login` and clears the token
- `PrivateRoute` blocks unauthenticated access to all dashboard routes

## 🎨 Design Tokens

All colors, spacing, shadows, and radii are CSS custom properties in `src/index.css`:

```css
--primary:       #2563EB   /* brand blue */
--danger:        #EF4444
--warning:       #F59E0B
--success:       #10B981
--sidebar-w:     240px
--navbar-h:      64px
--radius:        10px
```

## 📦 Dependencies

| Package          | Purpose                        |
|------------------|--------------------------------|
| react-router-dom | Client-side routing            |
| axios            | HTTP client + interceptors     |
| recharts         | Dashboard charts (Area + Pie)  |
| lucide-react     | Icon library                   |

## 🏗️ Build for Production

```bash
npm run build
# Output: /build folder → deploy to Nginx, Vercel, etc.
```
