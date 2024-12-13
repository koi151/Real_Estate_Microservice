import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { Badge, Card, Col, Form, Input, Radio, Row, Spin, message } from "antd";

import adminAccountsService from "../../../services/admin/admin-accounts.service";

import { AdminAccountType } from "../../../commonTypes";
import NoPermission from "../../../components/admin/NoPermission/noPermission";
import UploadMultipleFile from "../../../components/admin/UploadMultipleFile/uploadMultipleFile";
import clientAccountsServiceAdminSide from "../../../services/client-accounts.service";
import accountsService from "../../../services/client-accounts.service";


const AdminAccountsDetail: React.FC = () => {

  const { id } = useParams();
  const navigate = useNavigate();

  const [accessAllowed, setAccessAllowed] = useState(true);
  const [loading, setLoading] = useState<boolean>(true);

  const [account, setAccount] = useState<AdminAccountType | undefined>(undefined);

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (!id) {
          message.error('Could not found account, redirect to previous page', 3);
          navigate(-1);
          return;
        }

        const response = await accountsService.getAccountDetails(id);
        setLoading(true)

        if(response?.status >= 200 && response?.status < 300) {
          setAccessAllowed(true);
          setAccount(response.data.data);
        } else {
          setAccessAllowed(false);
          message.error(response.data.desc, 2);
        }

      } catch (err: any) {
        if (err.response && err.response.status === 401) {
          message.error('Unauthorized - Please log in to access this feature.', 3);
          navigate('/admin/auth/login');
        } else {
          message.error('Error occurred while fetching administrator account data', 2);
          console.log('Error occurred:', err);
        }

        setAccessAllowed(false);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id, navigate])

  return (
    <>
      { !loading ? (
        <>
          { accessAllowed ? (
            <div className="d-flex align-items-center justify-content-center"> 
              <Form 
                layout="vertical" 
                disabled
                className="custom-form"
                style={{maxWidth: "75%"}} 
              >
                <Badge.Ribbon text={<Link to="/admin/admin-accounts">Back</Link>} color="purple" className="custom-ribbon">
                  <Card 
                    title="View administrator account" 
                    className="custom-card" 
                    style={{marginTop: '2rem'}}
                    extra={<Link to="/admin/admin-accounts">Back</Link>}
                  >
                    <Row gutter={16}>
                      <Col sm={24} md={24} lg={12} xl={12} xxl={12}>
                        <Form.Item 
                          label='Full name:'
                          name='fullName'
                          rules={[{ required: true }]}
                          initialValue={account?.firstName + " " + account?.lastName}
                        >
                          <Input 
                            type="text" required
                            id="fullName" disabled
                            placeholder="Please enter your full name"
                          />
                        </Form.Item>
                      </Col>
                      <Col sm={24} md={24} lg={12} xl={12} xxl={12}>
                        <Form.Item 
                          label='Email:' 
                          name='email' 
                          rules={[{ type: 'email' }]}
                          initialValue={account?.email}
                        >
                          <Input 
                            type='email' id="email" disabled
                            placeholder="Please enter your email"
                          />
                        </Form.Item>
                      </Col>

                      <Col sm={24} md={24} lg={12} xl={12} xxl={12}>
                        <Form.Item
                          name="phone"
                          label="Phone number:"
                          initialValue={account?.phone}
                        >
                          <Input 
                            placeholder="Phone empty"
                            style={{ width: '100%' }} 
                            disabled
                          />
                        </Form.Item>
                      </Col>
                      
                      <Col sm={24} md={24} lg={12} xl={12} xxl={12}>
                        <Form.Item label="Account status:" name='status' initialValue={account?.accountEnabled ? 'active' : 'inactive'}>
                          <Radio.Group disabled>
                            <Radio value="active">Active</Radio>
                            <Radio value="inactive">Inactive</Radio>
                          </Radio.Group>
                        </Form.Item>
                      </Col>

                      <Col span={24}>
                        <UploadMultipleFile 
                          uploadedImages={account?.avatar ? [account.avatar] : []} 
                          singleImageMode={true} />
                      </Col>
                    </Row>
                  </Card>
                </Badge.Ribbon>
              </Form>
            </div>
          ) : (
            <NoPermission permissionType='access' />
          )}
        </>
      ) : (
        <div className='d-flex justify-content-center' style={{width: "100%", height: "100vh"}}>
          <Spin tip='Loading...' size="large">
            <div className="content" />
          </Spin>
        </div>
      )}
    </>
  )
}

export default AdminAccountsDetail;