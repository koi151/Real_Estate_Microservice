import React, { useEffect, useState } from 'react';
import { CheckboxChangeEvent } from 'antd/es/checkbox';
import { Link, useLocation } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { Breadcrumb, Button, Checkbox, Col, Image, InputNumber, Pagination, 
         PaginationProps, Popconfirm, Row, Skeleton, Space, Tag,  Tooltip,  message } from 'antd';

import * as standardizeData from '../../../helpers/standardizeData'

import propertiesService from '../../../services/admin/properties.service';

import ViewCount from '../../../components/shared/Counters/ViewCount/viewCount';
// import RoomCountTooltip from '../../../components/shared/Counters/RoomCounter/roomCount';
// import FilterBox from '../../../components/admin/FilterBox/filterBox';
import StatusButton from '../../../components/admin/StatusButton/statusButton';
import NoPermission from '../../../components/admin/NoPermission/noPermission';

// import { RootState } from '../../../redux/stores';
import './properties.scss';
import FilterBoxSlide from '../../../components/shared/FilterComponents/FilterBoxSlide/filterBoxSlide';
// import { selectClientUser } from '../../../redux/reduxSlices/clientUserSlice';
import FilterBox from '../../../components/FilterBox/filterBox';
import getPriceUnit from '../../../helpers/getPriceUnit';
import RoomCountTooltip from '../../../components/shared/Counters/RoomCounter/roomCount';
import { RootState } from '../../../redux/stores';
import { PaginationObj } from '../../../commonTypes';


const Properties: React.FC = () => {
  // const clientUser = useSelector(selectClientUser);
  const location = useLocation();

  const navigate = useNavigate();
  // const location = useLocation();
  const filters = useSelector((state: RootState) => state.filters);
  // const [permissions, setPermissions] = useState<AdminPermissions | undefined>(undefined);
  const [accessAllowed, setAccessAllowed] = useState(true);
  const [loading, setLoading] = useState(true);
  // const [propertyList, setPropertyList] = useState<PropertyType[]>([]);
  const [propertyList, setPropertyList] = useState<any[]>([]);
  const [error, setError] = useState<string | null>(null); 

  const { listingType, keyword, status, category, priceRange, sorting,
          direction, bedrooms, bathrooms, areaRange } = filters;

  const [checkedList, setCheckedList] = useState<string[]>([]);
  const [currentPage, setCurrentPage] = useState<number>(1);
  
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
    const fetchData = async () => {
      try {
        setLoading(true);

        const query = location.search;

        const response = await propertiesService.getAllProperties(query);
        console.log('RES PROPERTIES:', response);

        if (response?.status >= 200 && response?.status < 300) {
          setPropertyList(response.data.data);
          setPaginationObj({
            maxPageItems: response.data.maxPageItems,
            totalItems: response.data.totalItems,
            totalPages: response.data.totalPages,
            currentPage: response.data.currentPage
          });
          
        } else if (response?.status === 401) {
          message.error('Unauthorized - Please log in to access this feature.', 3);
          navigate('/admin/auth/login');
        } else {
          message.error(response.desc || 'Failed to fetch properties.', 4);
        }
      } catch (error: any) {
        if ((error.response && error.response.status === 401) || error.message === 'Unauthorized') {
          message.error('Unauthorized - Please log in to access this feature.', 3);
          navigate('/admin/auth/login');
        } else {
          message.error('Error occurred while fetching properties data.', 2);
          setError('No property found.');
          console.error('Error occurred:', error);
        }
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [location.search, navigate, status, keyword, priceRange]);
 


  // update url
  // useEffect(() => {
  //   navigate(buildURL());
  // // eslint-disable-next-line react-hooks/exhaustive-deps
  // }, [status, listingType, keyword, sorting, priceRange, areaRange,
  //   category, bedrooms, direction, bathrooms])

  // const buildURL = () => {
  //   const params: { [key: string]: string } = {};
  
  //   if (keyword) params['keyword'] = keyword;
  //   if (listingType) params['listingType'] = listingType;
  //   if (status) params['status'] = status;
  //   if (category) params['category'] = category;
  //   if (bedrooms) params['bedrooms'] = bedrooms;
  //   if (bathrooms) params['bathrooms'] = bathrooms;
  //   if(direction) params['direction'] = direction;

  //   if (sorting.sortKey && sorting.sortValue) {
  //     params['sortKey'] = sorting.sortKey;
  //     params['sortValue'] = sorting.sortValue;
  //   }
    
  //   const [minPrice, maxPrice] = priceRange ?? [];
  //   if (minPrice) params['minPrice'] = String(minPrice);
  //   if (maxPrice) params['maxPrice'] = String(maxPrice); 

  //   const [minArea, maxArea] = areaRange ?? [];
  //   if (minArea) params['minArea'] = String(minArea);
  //   if (maxArea) params['maxArea'] = String(maxArea);    
  
  //   // Short-circuiting for performance
  //   const queryString = Object.keys(params).length > 0 ? `?${new URLSearchParams(params)}` : '';
    
  //   return `${location.pathname}${queryString}`;
  // };

  
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

  const onChangePosition = (id: string | undefined, position: number | null) => {
    if (position === null || id === undefined){
      message.error("Error occurred, can not change position of property");
      console.log('id or value parameter is undefined')
    }

    const currentCheckBox = document.querySelector(`.item-wrapper__upper-content--checkbox span input[id="${id}"]`) as HTMLInputElement;
    if (currentCheckBox?.checked) {
      setCheckedList([...checkedList, `${id}-${position}`]);
    }
  }

  const renderTag = (value: string, colorMap: Record<string, string>) => (
    <Tag className="listing-type-tag" color={colorMap[value]}>
      {standardizeData.listingTypeFormatted(value)}
    </Tag>
  );

  // Delete item
  // const confirmDelete = async (id?: string) => {
  //   if (!id) {
  //     message.error('Error occurred, can not delete');
  //     console.log('Can not get id')
  //     return;
  //   } 
  //   const response = await propertiesService.deleteProperty(id);

  //   if (response?.code === 200) {
  //     message.success(response.message, 3);
  //     setPropertyList(prevPropertyList => prevPropertyList.filter(property => property._id !== id));

  //   } else {
  //     message.error('Error occurred, can not delete');
  //   }
  // };
  const formatPrice = (price: number | undefined): string => {
    if (!price) return '0';
    const formattedPrice = price > 1000 ? price / 1000 : price;
    return formattedPrice.toFixed(2);
  };
  
  return (
    <>
      {accessAllowed ? (
        <>
          <div className='title-wrapper'>
            <h1 className="main-content-title">Properties:</h1>
            <Breadcrumb
              className='mt-1 mb-1'
              items={[
                { title: <Link to="/admin">Admin</Link> },
                { title: <Link to="/admin/properties">Properties</Link> },
              ]}
            />
          </div>
    
          <FilterBox
            resetFilterLink='/admin/properties'
            createPostLink='/admin/properties/create' 
            priceRangeFilter
            categoryFilter
            statusFilter
            multipleChange
            // checkedList={checkedList}
          />
          
          <FilterBoxSlide slickWidth='100%' slideShow={5} userType='admin'/>
    
          {error ? (
            <div>{error}</div>
          ) : (
            <>
            <Skeleton loading={loading} active style={{ padding: '3.5rem' }}>
              {propertyList?.length > 0 ? (
                propertyList.map((property, index) => {
                  return (
                    <div className='item-wrapper' key={index} data-id={property._id}>  
                      <Row className='item-wrapper__custom-row'>
                        <div className='item-wrapper__upper-content' key={index}>
                            <Col
                              className='d-flex flex-column justify-content-center w-100'  
                              span={2}
                            >
                              {property.propertyId ?
                                <Tooltip title={
                                  <span>
                                    Property at <span style={{ color: 'orange' }}>#{property.propertyId}</span> position
                                  </span>
                                }>
                                  <InputNumber
                                    min={0}
                                    className='item-wrapper__upper-content--position' 
                                    defaultValue={property.propertyId} 
                                    // onChange={(value) => onChangePosition(property._id, value)}
                                    data-id={property.propertyId}
                                  />
                                </Tooltip>
                              : <Tooltip title='Please add the position of property'>No data</Tooltip>    
                              }
                              
                              <Checkbox
                                onChange={handleCheckboxChange(property._id)}
                                className='item-wrapper__upper-content--checkbox ml-2'
                                id={property._id}
                              ></Checkbox>
                            </Col>
      
                          <Col xxl={4} xl={4} lg={4} md={4} sm={4}>
                            {property.imageUrls?.length ? 
                              <Image
                                src={property.imageUrls?.[0] ?? ""} 
                                alt='property img' 
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
                                {property.title}
                              </h3>
                              <div className="item-wrapper__upper-content--location">
                                {property.address ? (
                                  <Tooltip title={property.address}>
                                    <span>
                                      {property.address.split(',').slice(0, 2).join(',')}.
                                    </span>
                                  </Tooltip>
                                ) : (
                                  "No information"
                                )}
                              </div>

                            </div>
                            <div>
                              {property.rentalPrice || property.salePrice
                                ? (
                                  <span className="item-wrapper__upper-content--price">
                                    <span className="price-number">
                                      {formatPrice(property.rentalPrice || property.salePrice)}
                                    </span>
                                    <span className="price-unit">
                                      {getPriceUnit(property.salePrice || property.rentalPrice)}
                                      <span style={{ margin: '0 .85rem' }}>/</span>
                                    </span>
                                  </span>
                                ) : (
                                  <Tooltip title="No data of price">...</Tooltip>
                                )
                              }
                              {property.area ? 
                                <span className='item-wrapper__upper-content--area'>
                                  <span className='area-number'>
                                    {(property.area).toFixed(0)}
                                  </span>
                                  <span className='area-unit'>m²</span>
                                </span>
                                : <Tooltip title='No data of area'>...</Tooltip>
                              }
                            </div>
                          </Col>

                          <Col xxl={3} xl={3} lg={3} md={3} sm={3}>
                            <div className="item-wrapper__upper-content--rooms">
                              {property.rooms ? (
                                <div className="d-flex flex-column justify-content-center">
                                  {property.rooms
                                    .filter((room: any) => room.roomType === "Bedroom" || room.roomType === "Bathroom")
                                    .map((room: any) => (
                                      <RoomCountTooltip
                                        key={room.roomType}
                                        roomType={room.roomType}
                                        quantity={room.quantity}
                                      />
                                    ))}
                                  {/* <ViewCount propertyView={property.view || 0} /> */}
                                </div>
                              ) : (
                                <>
                                  <RoomCountTooltip roomType="Bedroom" quantity={null} />
                                  <RoomCountTooltip roomType="Bathroom" quantity={null} />
                                  {/* <ViewCount propertyView={property.view || 0} /> */}
                                </>
                              )}
                            </div>
                          </Col>

                          
                          <Col
                            className='item-wrapper__custom-col-two'  
                            xxl={5} xl={5} lg={5} md={5} sm={5}
                          >
                            <div style={{marginLeft: "2rem"}}>
                              {property.status && property.propertyId ? (
                                <StatusButton typeofChange='changePropertyStatus' itemId={property.propertyId} status={property.status || undefined} />
                              ) : (
                                <Tooltip title='Please add property status or id'>No status data</Tooltip>
                              )}
                              <div className='item-wrapper__upper-content--listing-type'>
                                <p className='tag-text'>Tags: </p>
                                <Space size={[0, 8]} wrap>
                                  {(property.type === 'sale' || property.type === 'forRent') 
                                    && renderTag(property.type, { forSale: 'green', forRent: 'orange' })}
                                  {property.propertyDetails?.propertyCategory === 'house' 
                                  && renderTag(property.propertyDetails.propertyCategory, { house: 'purple', apartment: 'blue' })}
                                </Space>
                              </div>
                            </div>
                          </Col>
                          <Col
                            className='item-wrapper__custom-col-two'  
                            xxl={2} xl={2} lg={2} md={2} sm={2}
                          >
                            <div className='button-wrapper'>
                              <Link to={`/admin/properties/detail/${property.propertyId}`}> 
                                <Button className='detail-btn'>Detail</Button> 
                              </Link>
                              {/* {permissions?.propertiesEdit && ( */}
                                <Link to={`/admin/properties/edit/${property.propertyId}`}> 
                                  <Button className='edit-btn'>Edit</Button> 
                                </Link>
                              {/* )} */}
                              {/* {permissions?.propertiesDelete && ( */}
                                <Popconfirm
                                  title="Delete the task"
                                  description="Are you sure to delete this property?"
                                  // onConfirm={() => confirmDelete(property._id)}
                                  okText="Yes"
                                  cancelText="No"
                                >
                                  <Button type="primary" danger>Delete</Button> 
                                </Popconfirm>
                              {/* )} */}
                            </div>
                          </Col>
                        </div>
                      </Row>
                      <div className='line'></div>
                      <Row>
                        <Col span={24}>
                          <div className='item-wrapper__lower-content'>
                            <div className='item-wrapper__lower-content--date-created'>
                              Available from: {property.availableFrom ? new Date(property.availableFrom).toLocaleString() : 'No data'}
                            </div>
                            <div className='item-wrapper__lower-content--date-created'>
                              Expire: {property.expireTime ? new Date(property.expireTime).toLocaleString() : 'No data'}
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
        <NoPermission permissionType='view' />
      )}
    </>
  );
};

export default Properties;
