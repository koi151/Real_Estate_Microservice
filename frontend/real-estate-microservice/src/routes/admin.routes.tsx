// src/routes/adminRoutes.tsx

import { RouteObject } from 'react-router-dom';
import LayoutDefault from '../components/admin/Layouts/layoutDefault';
import ProtectedRoute from '../components/shared/ProtectedRoute/protectedRoute';
import PageNotFound from '../components/shared/PageNotFound/pageNotFound';
import KeycloakLogin from '../pages/KeycloakLogin/keycloakLogin';
import Properties from '../pages/admin/Properties/properties';
import Callback from '../components/callback';
import PropertyDetail from '../pages/admin/Properties/detail';
import PropertyCategories from '../pages/admin/PropertyCategories/propertyCategories';
import CreatePropertyCategory from '../pages/admin/PropertyCategories/create';
import CreateProperty from '../pages/admin/Properties/create';
import AdminAccounts from '../pages/admin/AdminAccounts/accounts';
import AdminAccountsDetail from '../pages/admin/AdminAccounts/detail';
import EditAdminAccounts from '../pages/admin/AdminAccounts/edit';
import EditProperty from '../pages/admin/Properties/edit';

export const adminRoutes: RouteObject[] = [
  {
    path: '/admin',
    element: (
      <ProtectedRoute userType='admin'>
        <LayoutDefault />
      </ProtectedRoute>
    ),
    children: [
      {
        path: 'properties',
        element: <Properties />,
      },
      {
        path: 'properties/detail/:id',
        element: <PropertyDetail />
      },
      {
        path: 'properties/create',
        element: <CreateProperty />
      },
      {
        path: 'properties/edit/:id',
        element: <EditProperty />
      },
      {
        path: 'property-categories',
        element: <PropertyCategories />
      },
      {
        path: 'property-categories/create',
        element: <CreatePropertyCategory />
      },
      {
        path: 'admin-accounts',
        element: <AdminAccounts />
      },
      {
        path: 'admin-accounts/detail/:id',
        element: <AdminAccountsDetail />
      },
      {
        path: 'admin-accounts/edit/:id',
        element: <EditAdminAccounts />
      },
    ],
  },
  {
    // path: '/admin/auth/login',
    // element: <RegisterAndLogin isRegisterPage={false} />
    path: '/admin/auth/login',
    element: <KeycloakLogin />,
  },
  {
    path: '/oauth/callback',
    element: <Callback />,
  },  
  {
    path: '*',
    element: <PageNotFound />,
  },
];
