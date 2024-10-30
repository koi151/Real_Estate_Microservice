import { RouteObject } from "react-router-dom";
import LayoutDefault from '../components/admin/Layouts/layoutDefault';
import ProtectedRoute from '../components/shared/ProtectedRoute/protectedRoute';
import PageNotFound from '../components/shared/PageNotFound/pageNotFound';
import RegisterAndLogin from "../pages/admin/RegisterAndLogin/adminRegisterLogin";
import Properties from '../components/admin/Properties/properties';
import React from "react";

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
    ],
  },
  {
    path: '/admin/auth/login',
    element: <RegisterAndLogin isRegisterPage={false} />
  },
  {
    path: '*',
    element: <PageNotFound />,
  }
];
