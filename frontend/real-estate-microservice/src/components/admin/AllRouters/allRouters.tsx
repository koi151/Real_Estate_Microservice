import { useRoutes } from 'react-router-dom';
import { adminRoutes } from '../../../routes/admin.routes';
// import { clientRoutes } from '../../../routes/client.routes.tsx';
import React from 'react';

const AllRouter: React.FC = () => {
  const routes = [
    ...adminRoutes,
    // ...clientRoutes
  ];
  const elements = useRoutes(routes);

  return elements || <div>Route not found</div>;
};

export default AllRouter;
