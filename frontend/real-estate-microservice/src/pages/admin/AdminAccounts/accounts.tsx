import React, { useEffect, useState } from 'react';
import { Breadcrumb, Button, Checkbox, Col, Image, Popconfirm, Row, Skeleton, Tooltip, message } from 'antd';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { CheckboxChangeEvent } from 'antd/es/checkbox';
import { FaPlus } from 'react-icons/fa';

import { AdminAccountType, AdminPermissions } from '../../../commonTypes';
import StatusButton from '../../../components/admin/StatusButton/statusButton';

import adminAccountsService from '../../../services/admin/admin-accounts.service';
import NoPermission from '../../../components/admin/NoPermission/noPermission';
import '../Properties/properties.scss';

const AdminAccounts: React.FC = () => {

  const navigate = useNavigate();
  const location = useLocation();

  const [permissions, setPermissions] = useState<AdminPermissions | undefined>(undefined);

  const [accessAllowed, setAccessAllowed] = useState(true);
  const [loading, setLoading] = useState(true);

  const [accountList, setAccountList] = useState<AdminAccountType[]>([]);
  const [error, setError] = useState<string | null>(null); 

  // Searching and filtering
  const [checkedList, setCheckedList] = useState<string[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const response = await adminAccountsService.getAccounts();

        if(response?.status >= 200 && response?.status < 300) {
          setAccountList(response.data.data);
          setAccessAllowed(true);

        } else {
          setAccessAllowed(false);
          message.error(response.data.desc, 4);
        }

      } catch (error: any) {
        if (error.response && error.response.status === 401) {
          message.error('Unauthorized - Please log in to access this feature.', 3);
          navigate('/admin/auth/login');
        } else {
          message.error('Error occurred while fetching properties data', 2);
          setError('No account found.');
          console.log('Error occurred:', error);
        }

        setAccessAllowed(false);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [navigate]);

  const handleCheckboxChange = (id: string | undefined) => (e: CheckboxChangeEvent) => {
    if (id === undefined) {
      message.error('Error occurred', 3);
      console.log('id parameter is undefined');
      return;
    }
    if (e.target.checked) {
      const position = document.querySelector(`.item-wrapper__upper-content--position input[data-id="${id}"]`) as HTMLInputElement;
      setCheckedList([...checkedList, `${id}-${position.value}`]);
    } else {
      setCheckedList(checkedList.filter((itemId) => itemId !== id));
    }
  };

  // Delete item
  // const confirmDelete = async (id?: string) => {
  //   if (!id) {
  //     message.error('Error occurred, can not delete');
  //     console.log('Can not get id')
  //     return;
  //   } 
  //   const response = await adminAccountsService.deleteAccount(id);

  //   if (response?.code === 200) {
  //     message.success(response.message, 3);
  //     setAccountList(prevaccountList => prevaccountList.filter(account => account._id !== id));

  //   } else {
  //     message.error('Error occurred, can not delete', 3);
  //   }
  // };
  
  return (
    <>
      { accessAllowed ? (
        <>
          <div className='title-wrapper'>
            <h1 className="main-content-title">Administrator accounts:</h1>
            <Breadcrumb
              className='mt-1 mb-1'
              items={[
                { title: <Link to="/admin">Admin</Link> },
                { title: <Link to="/admin/admin-accounts">Admin accounts</Link> },
              ]}
            />
          </div>

          { error ? (
            <div>{error}</div>
          ) : (
            <>
              <Skeleton loading={loading} active style={{ padding: '3.5rem' }}>
                {permissions?.administratorAccountsCreate && (
                  <div className='d-flex justify-content-end' style={{width: '100%'}}>
                    <Link to={`${location.pathname}/create`} className='custom-link'>
                      <Button className='add-new-button'>
                        Add new <FaPlus />
                      </Button>
                    </Link>
                  </div>
                )}

                {accountList?.length > 0 ? (
                  accountList.map((account, index) => {
                    
                    return (
                      <div className='item-wrapper' key={index} data-id={account.accountId}>  
                        <Row className='item-wrapper__custom-row'>
                          <div className='item-wrapper__upper-content' key={index} style={{
                                  maxHeight: '16rem',
                                  display: 'flex',
                                  alignItems: 'center'
                              }}>
                            <Col
                              className='d-flex flex-column justify-content-center'  
                              span={1}
                            >
                              <Checkbox
                                onChange={handleCheckboxChange(account.accountId)}
                                className='item-wrapper__upper-content--checkbox'
                                id={account.accountId}
                              ></Checkbox>
                            </Col>

                            <Col xxl={4} xl={4} lg={4} md={4} sm={4}>
                              {account.avatar ? 
                                <Image
                                  src={account.avatar ?? ""} 
                                  alt='avatar thumbnail' 
                                  width={200}
                                />
                                : <span className='d-flex justify-content-center align-items-center' style={{height: "100%"}}> No image </span>
                              }
                            </Col>
                            <Col 
                              xxl={7} xl={7} lg={7} md={7} sm={7}
                              className='item-wrapper__custom-col' 
                            >
                              <div>
                                <h3 className='item-wrapper__upper-content--title'>
                                  {account?.firstName + ' ' + account?.lastName}
                                </h3>
                                <div className='mt-2'>
                                  Role: <b style={{color: '#777'}}>{ account.roles[0] }</b>
                                </div>
                                <div className='mt-2'>
                                  Email: { account.email }
                                </div>
                                <div className='mt-1'>
                                  Phone: { account.phone }
                                </div>
                              </div>
                            </Col>
                            <Col
                              className='item-wrapper__custom-col-two'  
                              xxl={6} xl={6} lg={6} md={6} sm={6}
                            >
                              <div style={{marginLeft: "2rem"}}>
                                {account.status && account.accountId ? (
                                  <StatusButton 
                                    typeofChange='changeAccountStatus' 
                                    itemId={account.accountId} 
                                    status={account.status} 
                                  />
                                ) : (
                                  <Tooltip title='Please add account status or id'>No data</Tooltip>
                                )}
                              </div>
                            </Col>
                            <Col xxl={3} xl={3} lg={3} md={3} sm={3}></Col>
                            <Col
                              className='item-wrapper__custom-col-two'  
                              xxl={2} xl={2} lg={2} md={2} sm={2}
                            >
                              <div className='button-wrapper'>
                                <Link to={`/admin/admin-accounts/detail/${account.accountId}`}> 
                                  <Button className='detail-btn'>Detail</Button> 
                                </Link>
                                {/* {permissions?.administratorAccountsEdit && ( */}
                                  <Link to={`/admin/admin-accounts/edit/${account.accountId}`}> 
                                    <Button className='edit-btn mt-3'>Edit</Button> 
                                  </Link>
                                {/* )} */}
                                {permissions?.administratorAccountsDelete && (
                                  <Popconfirm
                                    title="Delete the task"
                                    description="Are you sure to delete this property account?"
                                    // onConfirm={() => confirmDelete(account._id)}
                                    okText="Yes"
                                    cancelText="No"
                                  >
                                    <Button type="primary" danger>Delete</Button> 
                                  </Popconfirm>
                                )}
                              </div>
                            </Col>
                          </div>
                        </Row>      
                        <div className='line'></div>
                        <Row>
                          <Col span={24}>
                            <div className='item-wrapper__lower-content'>
                              <div className='item-wrapper__lower-content--date-created'>
                                Created at: {account.createdAt ? new Date(account.createdAt).toLocaleString() : 'No data'}
                              </div>
                            </div>
                          </Col>
                        </Row>          
                      </div>
                    );
                  })
                ) : (
                  <>Loading...</>
                )}
              </Skeleton>
              <Skeleton loading={loading} active style={{ padding: '3.5rem' }}></Skeleton>
              <Skeleton loading={loading} active style={{ padding: '3.5rem' }}></Skeleton>
            </>
          )}
        </>
      ) : (
        <NoPermission permissionType='view' />
      )}
    </>
  );
};

export default AdminAccounts;
