import React, { useEffect, useState } from 'react';
import { Breadcrumb, Button, Checkbox, Col, Image, InputNumber, Pagination, PaginationProps, Popconfirm, Row, Skeleton, Tooltip, message } from 'antd';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { CheckboxChangeEvent } from 'antd/es/checkbox';

import { AdminPermissions, PaginationObj, PropertyCategoryType } from '../../../commonTypes';
import { SortingQuery } from '../../../commonTypes';
import StatusButton from '../../../components/admin/StatusButton/statusButton';

import sanitizeHtml from 'sanitize-html';
import FilterBox from '../../../components/FilterBox/filterBox';
import NoPermission from '../../../components/admin/NoPermission/noPermission';
import { useDispatch } from 'react-redux';

import '../Properties/properties.scss'
import propertyCategoriesService from '../../../services/admin/property-categories.service';

const PropertyCategories: React.FC = () => {

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  const [permissions, setPermissions] = useState<AdminPermissions | undefined>(undefined);

  const [accessAllowed, setAccessAllowed] = useState(true);
  const [loading, setLoading] = useState(true);

  const [categoryList, setCategoryList] = useState<PropertyCategoryType[]>([]);
  const [error, setError] = useState<string | null>(null); 

  // Searching and filtering
  const [checkedList, setCheckedList] = useState<string[]>([]);
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [status] = useState<string | undefined>(undefined);
  const [keyword] = useState<string | null>(null); 

  const [sorting] = useState<SortingQuery>(
    { sortKey: '', sortValue: '' }
  )

  const [paginationObj, setPaginationObj] = useState<PaginationObj>({
    maxPageItems: 10, 
    totalItems: undefined,
    totalPages: undefined,
    currentPage: 1,
  })

  const onPageChange: PaginationProps['onChange'] = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  useEffect(() => {
    setLoading(true);

    const fetchData = async () => {
      try {
        setLoading(true);

        const query = location.search;
        const response = await propertyCategoriesService.getPropertyCategories(query);

        console.log("Categories res: ", response)

        if(response?.status >= 200 && response?.status < 300) {
          setCategoryList(response.data.data);
          setPaginationObj({
            maxPageItems: response.data.maxPageItems,
            totalItems: response.data.totalItems,
            totalPages: response.data.totalPages,
            currentPage: response.data.currentPage
          });          
          setAccessAllowed(true);

        } else {
          setAccessAllowed(false);
          message.error(response.message, 4);
        }

      } catch (error: any) {
        if (error.response && error.response.status === 401) {
          message.error('Unauthorized - Please log in to access this feature.', 3);
          navigate('/admin/auth/login');
        } else {
          message.error('Error occurred while fetching property categories', 2);
          setError('No property category found.');
          console.log('Error occurred:', error);
        }
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [keyword, status, sorting, currentPage, navigate, dispatch]);

  // // update url
  // useEffect(() => {
  //   navigate(buildURL());
  // // eslint-disable-next-line react-hooks/exhaustive-deps
  // }, [status, keyword, sorting])

  // const buildURL = () => {
  //   const params: { [key: string]: string } = {};
  //   if (keyword) params['keyword'] = keyword;
  //   if (status) params['status'] = status;
  //   if (sorting.sortKey && sorting.sortValue) {
  //     params['sortKey'] = sorting.sortKey;
  //     params['sortValue'] = sorting.sortValue;
  //   }

  //   return `${location.pathname}${Object.keys(params).length > 0 ? `?${new URLSearchParams(params)}` : ''}`;
  // };

  const onChangePosition = (id: number, position: number | null) => {
    if (position === null || id === undefined){
      message.error("Error occurred, can not change position of category");
      console.log('id or value parameter is undefined')
    }

    const currentCheckBox = document.querySelector(`.item-wrapper__upper-content--checkbox span input[id="${id}"]`) as HTMLInputElement;
    if (currentCheckBox?.checked) {
      setCheckedList([...checkedList, `${id}-${position}`]);
    }
  }

  const handleCheckboxChange = (id: number) => (e: CheckboxChangeEvent) => {
    if (e.target.checked) {
      const position = document.querySelector(`.item-wrapper__upper-content--position input[data-id="${id}"]`) as HTMLInputElement;
      setCheckedList([...checkedList, `${id}-${position.value}`]);
    } else {
      setCheckedList(checkedList.filter((itemId) => itemId !== id.toString()));
    }
  };

  // Delete item
  // const confirmDelete = async (id?: string) => {
  //   if (!id) {
  //     message.error('Error occurred, can not delete');
  //     console.log('Can not get id')
  //     return;
  //   } 
  //   const response = await propertyCategoriesService.deleteCategory(id);

  //   if (response?.code === 200) {
  //     message.success(response.message, 3);
  //     setCategoryList(prevCategoryList => prevCategoryList.filter(category => category._id !== id));

  //   } else {
  //     message.error('Error occurred, can not delete');
  //   }
  // };
  
  //keyword, status, sorting, currentPage

  return (
    <>
    
      <>
        {accessAllowed ? (
          <>
            <div className='title-wrapper'>
              <h1 className="main-content-title">Property categories:</h1>
              <Breadcrumb
                className='mt-1 mb-1'
                items={[
                  { title: <Link to="/admin">Admin</Link> },
                  { title: <Link to="/admin/property-categories">Categories</Link> },
                ]}
              />
            </div>

            <FilterBox
              resetFilterLink='/admin/property-categories'
              createPostLink='/admin/property-categories/create' 
              createAllowed={permissions?.propertyCategoriesCreate}
              statusFilter
            />

            { error ? (
              <div>{error}</div>
            ) : (
              <>
                <Skeleton loading={loading} active style={{ padding: '3.5rem' }}>
                  {categoryList?.length > 0 ? (
                    categoryList.map((category, index) => {
                      const cleanHtml = sanitizeHtml(category.description, {
                        allowedStyles: {
                          '*': {
                            'color': [/^rgb\(\d+,\s*\d+,\s*\d+\)$/, /^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$/],
                          },
                        },                
                      });
                      
                      return (
                        <div className='item-wrapper' key={index} data-id={category.categoryId}>  
                          <Row className='item-wrapper__custom-row'>
                            <div className='item-wrapper__upper-content' key={index}>
                              <Col
                                className='d-flex flex-column justify-content-center'  
                                span={1}
                              >
                                {category.categoryId ?
                                  <Tooltip title={
                                    <span>
                                      Category at <span style={{ color: 'orange' }}>#{category.categoryId}</span> position
                                    </span>
                                  }>
                                    <InputNumber
                                      min={0}
                                      className='item-wrapper__upper-content--position' 
                                      defaultValue={category.categoryId} 
                                      onChange={(value) => onChangePosition(category.categoryId, value)}
                                      data-id={category.categoryId}
                                    />
                                  </Tooltip>
                                : <Tooltip title='Please add the position of property'>No data</Tooltip>    
                                }
                                <Checkbox
                                  onChange={handleCheckboxChange(category.categoryId)}
                                  className='item-wrapper__upper-content--checkbox'
                                  id={category.categoryId.toString()}
                                ></Checkbox>
                              </Col>

                              <Col xxl={4} xl={4} lg={4} md={4} sm={4}>
                                {category.imageUrls?.length ? 
                                  <Image
                                    src={category.imageUrls ?? ""} 
                                    alt='category img' 
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
                                    {category.title}
                                  </h3>
                                  {cleanHtml ? (
                                    <div 
                                      key={index} 
                                      data-id={category.categoryId}
                                      dangerouslySetInnerHTML={{ __html: cleanHtml }}
                                    />
                                  ) : (
                                    <div>No description</div>
                                  )}
                                </div>
                              </Col>
                              <Col
                                className='item-wrapper__custom-col-two'  
                                xxl={6} xl={6} lg={6} md={6} sm={6}
                              >
                                <div style={{marginLeft: "2rem"}}>
                                  {category.status && category.categoryId ? (
                                    <StatusButton typeofChange='changePropertyCategoriesStatus' itemId={category.categoryId.toString()} status={category.status} />
                                  ) : (
                                    <Tooltip title='Please add property status or id'>No data</Tooltip>
                                  )}
                                </div>
                              </Col>
                              <Col xxl={3} xl={3} lg={3} md={3} sm={3}></Col>
                              <Col
                                className='item-wrapper__custom-col-two'  
                                xxl={2} xl={2} lg={2} md={2} sm={2}
                              >
                                <div className='button-wrapper'>
                                  <Link to={`/admin/property-categories/detail/${category.categoryId}`}> 
                                    <Button className='detail-btn'>Detail</Button> 
                                  </Link>
                                  <Link to={`/admin/property-categories/edit/${category.categoryId}`}> 
                                    <Button className='edit-btn'>Edit</Button> 
                                  </Link>
                                  <Popconfirm
                                    title="Delete the task"
                                    description="Are you sure to delete this property category?"
                                    // onConfirm={() => confirmDelete(category._id)}
                                    okText="Yes"
                                    cancelText="No"
                                  >
                                    <Button type="primary" danger>Delete</Button> 
                                  </Popconfirm>
                                </div>
                              </Col>
                            </div>
                          </Row>      
                          <div className='line'></div>
                          <Row>
                            <Col span={24}>
                              <div className='item-wrapper__lower-content'>
                                <div className='item-wrapper__lower-content--date-created'>
                                  Created at: {category.createdDate ? new Date(category.createdDate).toLocaleString() : 'No data'}
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
              <Pagination
                // showSizeChanger
                showQuickJumper
                pageSize={paginationObj.maxPageItems}
                onChange={onPageChange}
                defaultCurrent={paginationObj.currentPage}
                total={paginationObj.totalItems}
              />
          </>
        ) : (
          <NoPermission permissionType='access' />
        )}
      </>
    </>
  );
};

export default PropertyCategories;
