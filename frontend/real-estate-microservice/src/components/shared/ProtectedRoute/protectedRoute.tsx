import React, { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import { setClientUser } from "../../../redux/reduxSlices/clientUserSlice";
import { Spin, message } from "antd";
import adminAccountsService from "../../../services/admin/admin-accounts.service";
import { setAdminUser } from "../../../redux/reduxSlices/adminUserSlice";

interface UserResponse {
  code: number;
  user: any; 
}

interface ProtectedRouteProps {
  userType: 'admin' | 'client';
  children: React.ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ userType, children }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(true);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const dispatch = useDispatch();

  // useEffect(() => {
  //   const fetchUserData = async () => {
  //     try {

  //       setIsAuthenticated(true); // t
  //       // Fetch user data based on userType
  //       // const response: UserResponse = userType === "admin"
  //       //   ? await adminAccountsService.getSingleAccountLocal()
  //       //   : await clientAccountsService.getSingleAccountLocal();

  //       // if (response.code === 200 && response.user) {
  //       //   // Set authenticated and update Redux store
  //       //   setIsAuthenticated(true);
  //       //   userType === "admin"
  //       //     ? dispatch(setAdminUser(response.user))
  //       //     : dispatch(setClientUser(response.user));
  //       // } else {
  //       //   throw new Error("User not authenticated");
  //       // }
  //     } catch (err: any) {
  //       // Handle error responses
  //       if (err.response && err.response.status === 401) {
  //         message.error("Unauthorized - Please log in to access this feature.", 3);
  //         navigate(userType === "admin" ? "/admin/auth/login" : "/auth/login", { replace: true });
  //       } else {
  //         console.error("Error occurred while fetching user data:", err);
  //       }
  //       setIsAuthenticated(false);
  //     } finally {
  //       setIsLoading(false);
  //     }
  //   };

  //   fetchUserData();

  //   // eslint-disable-next-line react-hooks/exhaustive-deps
  // }, [navigate, userType, dispatch]);

  // Render loading indicator
  if (isLoading) {
    return (
      <Spin
        size="large"
        className="d-flex justify-content-center align-items-center"
        style={{ height: "100vh" }}
      />
    );
  }

  console.log("isAuthenticated: ", isAuthenticated)
  // Render protected content or redirect
  return isAuthenticated ? (
    <>{children}</>
  ) : (
    <Navigate to={userType === "admin" ? "/admin/auth/login" : "/auth/login"} replace />
  );
};

export default ProtectedRoute;
