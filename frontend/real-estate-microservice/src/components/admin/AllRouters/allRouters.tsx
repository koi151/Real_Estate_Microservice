import { useRoutes } from 'react-router-dom';
import { adminRoutes } from '../../../routes/admin.routes';
import React from 'react';
import { clientRoutes } from '../../../routes/client.routes';

const AllRouter: React.FC = () => {
  const routes = [
    ...adminRoutes,
    ...clientRoutes
  ];
  const elements = useRoutes(routes);

  return elements || <div>Route not found</div>;
};

export default AllRouter;
