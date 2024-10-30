import AllRoute from './components/admin/AllRouters/allRouters';
import './App.scss';
import React from 'react';
import { ConfigProvider } from 'antd';

const App: React.FC = () => {
  return (
    <ConfigProvider
      theme={{
        token: {
          colorPrimary: '#6259ca'
        },
      }}
    >
      <React.Suspense fallback={<div>Loading...</div>}>
        <AllRoute />
      </React.Suspense>
    </ConfigProvider>
  );
};

export default App;
