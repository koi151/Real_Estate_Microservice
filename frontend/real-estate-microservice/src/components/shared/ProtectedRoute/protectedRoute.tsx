import React, { useEffect, useState } from "react";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import { Spin, message } from "antd";
import { useDispatch } from "react-redux";

interface UserResponse {
  code: number;
  user: any; 
}

interface ProtectedRouteProps {
  userType: 'admin' | 'client',
  children: React.ReactNode
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ userType, children }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const dispatch = useDispatch();

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        if (location.pathname === "/properties/") {
          setIsAuthenticated(true);
          return;
        }        

        setIsAuthenticated(true);

        // Mocking the authentication flow.
        // Replace this with your actual async logic.
        // const response: UserResponse = userType === 'admin' 
        //   ? await adminAccountsService.getSingleAccountLocal()
        //   : await clientAccountsService.getSingleAccountLocal()
          
        // if (response.code === 200 && response.user) {
        //   setIsAuthenticated(true);
        //   userType === 'admin' ? dispatch(setAdminUser(response.user)) : dispatch(setClientUser(response.user))
        // } else {
        //   setIsAuthenticated(false);
        // }

      } catch (err: any) {
        if (err.response && err.response.status === 401) {
          message.error('Unauthorized - Please log in to access this feature.', 3);
          navigate(`${userType === 'admin' ? "/admin/auth/login" : "/auth/login"}`);
        } else {
          console.error('Error occurred while fetching user data:', err);
        }
        setIsAuthenticated(false);
      } finally {
        setIsLoading(false);
      }
    };

    fetchUserData();

  }, [navigate, dispatch, userType, location.pathname]);

  // Render loading indicator
  if (isLoading) {
    return (
      <Spin size="large" className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }} />
    );
  }

  // Render based on authentication state
  return isAuthenticated ? (
    <>{children}</> // This ensures children are always rendered as React nodes.
  ) : (
    <Navigate to={userType === 'admin' ? "/admin/auth/login" : "/auth/login"} replace />
  );
};

export default ProtectedRoute;
