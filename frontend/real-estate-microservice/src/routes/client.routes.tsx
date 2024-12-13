import React from 'react';
import { RouteObject } from 'react-router-dom';
import ProtectedRoute from '../components/shared/ProtectedRoute/protectedRoute';
import LayOutDefaultClient from '../components/client/Layouts/layoutDefault';
import Properties from '../pages/client/Properties/properties';
import PageNotFound from '../components/shared/PageNotFound/pageNotFound';
import KeycloakLogin from '../pages/KeycloakLogin/keycloakLogin';


export const clientRoutes: RouteObject[] = [
  {
    path: '/',
    element: 
      <ProtectedRoute userType='client'>
        <LayOutDefaultClient />
      </ProtectedRoute>,
    children: [
      // {
      //   path: "accounts/my-detail/edit",
      //   element: <EditClientAccounts />,
      // },
      {
        path: "properties",
        element: <Properties />,
      },
      // {
      //   path: "properties/my-properties",
      //   element: <MyProperty />,
      // },
      // {
      //   path: "properties/my-properties/detail/:id",
      //   element: <MyPropertyDetail />,
      // },
      // {
      //   path: "properties/my-properties/edit/:id",
      //   element: <MyPropertyEdit />,
      // },
      // {
      //   path: 'properties/create',
      //   element: <CreatePropertyPost />
      // },
      // {
      //   path: '/properties/my-favorites',
      //   element: <FavoritedProperties />
      // },
      // {
      //   path: 'properties/detail/:id',
      //   element: <PropertyDetail />
      // },
      // {
      //   path: 'deposit',
      //   element: <DepositMethods />
      // },
      // {
      //   path: 'order/deposit/vnpay',
      //   element: <DepositDetail />
      // },
      // {
      //   path: 'order/deposit/vnpay/vnpay-return',
      //   element: <VNPayResult />
      // }
    ]
  }, 
  // {
  //   path: '/auth/login',
  //   element: <RegisterAndLogin isRegisterPage={false} />
  // },
  // {
  //   path: '/auth/register',
  //   element: <RegisterAndLogin isRegisterPage />
  // },
  {
    // path: '/admin/auth/login',
    // element: <RegisterAndLogin isRegisterPage={false} />
    path: '/admin/auth/login',
    element: <KeycloakLogin />,
  },
  {
    path: '*',
    element: <PageNotFound />,
  },
];