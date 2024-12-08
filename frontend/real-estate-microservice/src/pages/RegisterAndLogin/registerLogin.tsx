import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { Form, Col, Row, Input, Button, message } from "antd";
import { useDispatch } from "react-redux";
import './registerLogin.scss';

import adminAuthorizationService from "../../services/authorization.service";
// import { setClientUser, setTokenInfo } from "../../redux/reduxSlices/clientUserSlice";

interface AdminRegisterLoginProps {
  isRegisterPage: boolean;
}

const AdminRegisterLogin: React.FC<AdminRegisterLoginProps> = ({ isRegisterPage = false }) => {

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLoginSuccess = (response: any) => {
    const { access_token, refresh_token, expires_in, refresh_expires_in, scope } = response.data.data.accessToken;
    const userInfo = response.data.data.userInfo;

    // dispatch(setClientUser({
    //   ...userInfo,
    // }));

    // dispatch(setTokenInfo({
    //   accessToken: access_token,
    //   refreshToken: refresh_token,
    //   expiresIn: expires_in,
    //   refreshExpiresIn: refresh_expires_in,
    //   scope: scope,
    // }));
  };

  const onFinishForm = async (data: any) => {
    try {
      if (!isRegisterPage) { // login
        const response = await adminAuthorizationService.submitLogin(data);
        const responseData = response.data.data

        console.log("RES token: ", response.data.data.accessToken.access_token);
        switch (response.status) {
          case 200:
            handleLoginSuccess(response);

            message.success(`Login successful. Welcome ${responseData.userInfo.username}!`, 3);
            // navigate('/admin/dashboard');
            navigate('/admin/properties');

            break;

          case 401:
            message.error(`${responseData.desc}, please try again`, 3);
            break;

          case 403:
            message.error(`${responseData.desc}, please try again`, 3);
            break;

          default:
            break;
        }
      } else { // register
        // const response = await adminAuthorizationService.submitRegister(data);
        // switch (response.code) {
          
        //   case 200:
        //     // if (response.user) {
        //     //   dispatch(setAdminUser(response.user))
        //     // }

        //     message.success(`Register successful!. Login to continue`, 4);
        //     navigate('/admin/auth/login');
        //     break;

        //   case 401:
        //   case 403:
        //     message.error(`${response.message}, please try again`, 3);
        //     break;

        //   default:
        //     break;
        // }
      }


    } catch (err: any) {
      console.log('Error occurred:', err);
      if (err.response.status === 429) {
        message.error("Too many login request, please try again later!", 3)
      } else {
        message.error(`Error occurred, can not ${isRegisterPage ? "register" : "login"}`)
      } 
    }
  }

  return (
    <>
      <div className="darken-layer"></div>
      <Row className="box-wrapper">
        <div className='box-sep'>
          <Col span={`${isRegisterPage ? 9 : 12}`} className='box-sep__left'>
          </Col>
          <Col span={`${isRegisterPage ? 15 : 12}`} className='box-sep__right'>
            <strong className={`box-sep__right--title ${isRegisterPage && 'mt-4'}`}>
              {isRegisterPage ? 'REGISTER' : 'LOGIN'}
            </strong>
            <span className="box-sep__right--welcome-text">
              Welcome to Administrator page
            </span>

            <Form
              layout="vertical" 
              className='login-form'
              method="POST"
              onFinish={onFinishForm}
            >
              <Row>
                {isRegisterPage ? (
                  <>
                    <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                      <Form.Item
                        label='User name:'
                        name='userName'
                        rules={[{ required: true, message: 'Please input your user name!' }]}
                      >
                        <Input 
                          style={{width: "95%"}}
                          required 
                          placeholder="Please enter your user name"
                        />
                      </Form.Item> 
                    </Col>
                    <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                      <Form.Item
                        label='Email:'
                        name='registerEmail'
                        rules={[{ required: true, message: 'Please input the email!' }]}
                      >
                        <Input 
                          style={{width: "95%"}}
                          required type="email" 
                          id="email" 
                          placeholder="Please enter your email"
                        />
                      </Form.Item> 
                    </Col>
                    <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                      <Form.Item
                        name="registerPassword"
                        label='Password:'
                        required
                        rules={[
                          { message: 'Please input your password!' },
                          {
                            min: 6, 
                            message: 'Password must be at least 6 characters long!',
                          },
                          {
                            max: 20,
                            message: 'Password must be at most 20 characters long!',
                          },
                        ]}
                      >
                        <Input.Password 
                          placeholder="Please enter your password"
                          style={{width: "95%"}}
                        />
                      </Form.Item> 
                    </Col>
                    <Col sm={24} md={24} lg={12} xl={12} xxl={12}>
                      <Form.Item
                        label="Confirm password:"
                        name='confirm'
                        dependencies={['password']}
                        rules={[
                          { message: 'Please confirm your password!'},
                          ({ getFieldValue }) => ({
                            validator(_, value) {
                              if (!value || getFieldValue('registerPassword') === value) {
                                return Promise.resolve();
                              }
                              return Promise.reject(new Error('The new password that you entered do not match!'));
                            },
                          }),
                        ]}
                      >
                        <Input.Password
                          style={{width: "95%"}}
                          placeholder="Confirm your password"
                        />
                      </Form.Item>
                    </Col>
                  </>
                ) : (
                  <>
                    <Col span={24}>
                      <Form.Item
                        label='Username or email:'
                        name='identifier'
                        rules={[{ required: true, message: 'Please input username or email!' }]}
                      >
                        <Input 
                          id="email" 
                          placeholder="Please enter your username or email"
                        />
                      </Form.Item> 
                    </Col>
                    <Col span={24}>
                      <Form.Item
                        name="password"
                        label='Password:'
                        required
                        rules={[
                          { message: 'Please input your password!' },
                          {
                            min: 6, 
                            message: 'Password must be at least 6 characters long!',
                          },
                          {
                            max: 20,
                            message: 'Password must be at most 20 characters long!',
                          },
                        ]}
                      >
                        <Input.Password placeholder="Please enter your password"/>
                      </Form.Item> 
                    </Col>
                  </>
                )}

                <Col span={24}>
                  <Form.Item className="text-center">
                    <Button className={`custom-btn-main ${isRegisterPage && 'mt-4'}`} type="primary" htmlType="submit">
                      {isRegisterPage ? 'Register now' : 'Login'}
                    </Button>
                  </Form.Item>
                </Col>
              </Row>
            </Form>

            <div className="d-flex justify-content-center align-items-center">
              <span className="line"></span>
              <span style={{color: "#aaa", fontSize: "1.2rem", margin: "3rem 1rem"}}>or</span>
              <span className="line"></span>
            </div>
            <div className="d-flex justify-content-center align-items-center">
              <div style={{fontSize: "1.4rem"}}>
                {isRegisterPage ? 'Already have an account ? ' : 'New to admin page ? '}
              </div>
              <Link 
                to={isRegisterPage ? `/admin/auth/login` : `/admin/auth/register`} 
                className="redirect-page"
              >
                {isRegisterPage ? 'Login now' : 'Create now'}
              </Link>
            </div>
          </Col>
        </div>
      </Row>
    </>
  )
}

export default AdminRegisterLogin;
